import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
//        Set<Long> masks = new HashSet<>();
//        GraphDB.Node[] nodeList = new GraphDB.Node[g.numVertices()];
        long stId = -1;
        long destId = -1;
        double stDist = Double.MAX_VALUE;
        double destDist = Double.MAX_VALUE;
//        int i = 0;
        for (long id : g.vertices()) {
            GraphDB.Node node = g.getNode(id);
            node.resetNode();
//            nodeList[i++] = node;
            double newStDist = GraphDB.distance(stlon, stlat, node.longitude, node.latitude);
            double newDestDist = GraphDB.distance(destlon, destlat,
                                                  node.longitude, node.latitude);
            if (newStDist < stDist) {
                stDist = newStDist;
                stId = id;
            }
            if (newDestDist < destDist) {
                destDist = newDestDist;
                destId = id;
            }
        }
//        MinPQ<GraphDB.Node> heap = new MinPQ<>(nodeList);
        MinPQ<GraphDB.Node> heap = new MinPQ<>();
        heap.setHasHandle(true);

        GraphDB.Node curNode = g.getNode(stId);
        long curId = stId;
        curNode.dist = 0;
        curNode. priority = g.distance(curId, destId);
        heap.insert(curNode);

        LinkedList<Long> result = new LinkedList<>();
        while (true) {
            if (heap.isEmpty()) {
                return result;
            }
            curNode = heap.delMin();
            curId = curNode.id;
            curNode.mask = true;
            if (curId == destId) {
                break;
            }
            for (long id : curNode.neighborIds.keySet()) {
                GraphDB.Node node = g.getNode(id);
                if (!node.mask) {
                    double parentDist = g.distance(curId, id);
                    double hDist = g.distance(id, destId);
                    double newPriority = curNode.dist + parentDist + hDist;
                    if (newPriority < node.priority) {
                        node.dist = curNode.dist + parentDist;
                        node.priority = newPriority;
                        node.parentId = curId;
                        if (node.heapId == -1) {
                            heap.insert(node);
                        } else {
                            heap.decreaseKey(node.heapId);
                        }
                    }
                }
            }
        }

        while (curId != stId) {
            result.addFirst(curId);
            curId = g.getNode(curId).parentId;
        }
        result.addFirst(stId);
        return result;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> result = new ArrayList<>();
        if (route.size() <= 1) {
            return result;
        }
        long node0 = route.get(0);
        double[] distPtr = new double[1];
        int nextIdx = findNextJunctionNodeIdx(g, route, 0, distPtr);
        long node1 = route.get(nextIdx);
//        double prevAngle = g.bearing(node0, node1);
        result.add(new NavigationDirection(NavigationDirection.START,
                                           getWayName(g, route, 0), distPtr[0]));
        int curIdx = nextIdx;
        while (-1 != (nextIdx = findNextJunctionNodeIdx(g, route, curIdx, distPtr))) {
            node0 = node1;
            node1 = route.get(nextIdx);
            long node_ = route.get(curIdx + 1);
            double dAngle = g.bearing(node0, node_) - g.bearing(route.get(curIdx - 1), node0);
            dAngle = clampedAngle(dAngle);
            result.add(new NavigationDirection(NavigationDirection.chooseDirection(dAngle),
                                               getWayName(g, route, curIdx), distPtr[0]));
            result.get(result.size() - 1).angle = dAngle;
//            prevAngle = newAngle;
            curIdx = nextIdx;
        }
        return result;
    }
    private static int findNextJunctionNodeIdx(GraphDB g, List<Long> route, int curIdx, double[] distPtr) {
        if (curIdx == route.size() - 1) {
            return -1;
        }
        long curId = route.get(curIdx);
        long nextId = route.get(++curIdx);
        String curWay = g.getNode(curId).neighborIds.get(nextId);
        distPtr[0] = g.distance(curId, nextId);
        curId = nextId;
        for (; curIdx < route.size() - 1; curIdx++) {
            nextId = route.get(curIdx + 1);
            String newWay = g.getNode(curId).neighborIds.get(nextId);
            if (!newWay.equals(curWay)) {
                break;
            }
            distPtr[0] += g.distance(curId, nextId);
            curId = nextId;
        }
        return curIdx;
    }

    private static double clampedAngle(double dAngle) {
        while (dAngle >= 180) {
            dAngle -= 360;
        }
        while (dAngle < -180) {
            dAngle += 360;
        }
        return dAngle;
    }

    private static String getWayName(GraphDB g, List<Long> route, int idx) {
        String wayName = g.getNode(route.get(idx)).neighborIds.get(route.get(idx + 1));
//        if (wayName.length() == 0) {
//            wayName = "unknown road";
//        }
        return wayName;
    }
    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        double angle = 0;
        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public NavigationDirection(int direction, String way, double distance) {
            this.direction = direction;
            this.way = way;
            this.distance = distance;
        }

        public String toString() {
//            return String.format("%s on %s and continue for %.3f miles.  angle = %.3f",
//                    DIRECTIONS[direction], way, distance, angle);
            return String.format("%s on %s and continue for %.3f miles",
                    DIRECTIONS[direction], way, distance);
        }
        private static int chooseDirection(double bearingAngle) {
            bearingAngle = clampedAngle(bearingAngle);
            if (bearingAngle < 15 && bearingAngle >= -15) {
                return STRAIGHT;
            } else if (bearingAngle >= 0) {
                if (bearingAngle < 30) {
                    return SLIGHT_RIGHT;
                } else if (bearingAngle < 100) {
                    return RIGHT;
                } else {
                    return SHARP_RIGHT;
                }
            } else {
                if (bearingAngle >= -30) {
                    return SLIGHT_LEFT;
                } else if (bearingAngle >= -100) {
                    return LEFT;
                } else {
                    return SHARP_LEFT;
                }
            }
        }
        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
