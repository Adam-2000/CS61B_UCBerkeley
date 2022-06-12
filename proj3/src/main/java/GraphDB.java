import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    public static class Node implements Comparable<Node>, MinPQ.HeapHandler {
        public final long id;
        public final double longitude;
        public final double latitude;
        public final TreeSet<Long> neighborIds;
        public String name;
        Node(long id, double lon, double lat) {
            this.id = id;
            latitude = lat;
            longitude = lon;
            neighborIds = new TreeSet<>();
        }
        public double dist = -1;
        public double priority = -1;
        public int heapId = -1;
        public long parentId = -1;
        public boolean mask = false;
        public void resetNode() {
            dist = Double.MAX_VALUE;
            priority = Double.MAX_VALUE;
            heapId = -1;
            parentId = -1;
            mask = false;
        }
        @Override
        public int compareTo(Node o) {
            if (priority < o.priority) {
                return -1;
            } else if (priority == o.priority) {
                return 0;
            }
            return 1;
        }

        @Override
        public void changeHeapId(int i) {
            heapId = i;
        }
    }
    private Map<Long, Node> nodeMap;
    public Node getNode(long id) {
        return nodeMap.get(id);
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);
            nodeMap = new HashMap<>();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void putNode(long id, double lon, double lat) {
        if (!nodeMap.containsKey(id)) {
            nodeMap.put(id, new Node(id, lon, lat));
        } else {
            throw new IllegalArgumentException("ID not found: " + id);
        }
    }

    public void setNodeName(long id, String name) {
        if (nodeMap.containsKey(id)) {
            nodeMap.get(id).name = name;
        } else {
            throw new IllegalArgumentException("ID not found: " + id + " " + name);
        }
    }

    public void connect(long id1, long id2) {
        if (nodeMap.containsKey(id1) && nodeMap.containsKey(id2)) {
            nodeMap.get(id1).neighborIds.add(id2);
            nodeMap.get(id2).neighborIds.add(id1);
        } else {
            throw new IllegalArgumentException("ID not found: " + id1 + " " + id2);
        }
    }
    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TOD: Your code here.
        TreeSet<Long> removeList = new TreeSet<>();
        for (long id : nodeMap.keySet()) {
            if (nodeMap.get(id).neighborIds.isEmpty()) {
                removeList.add(id);
            }
        }
        for (long id : removeList) {
            nodeMap.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodeMap.keySet();
    }
    int numVertices() {
        return nodeMap.size();
    }
    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return nodeMap.get(v).neighborIds;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        long minId = -1;
        for (long id : nodeMap.keySet()) {
            double dist = distance(lon, lat, nodeMap.get(id).longitude, nodeMap.get(id).latitude);
            if (dist < minDist) {
                minDist = dist;
                minId = id;
            }
        }
        return minId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodeMap.get(v).longitude;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodeMap.get(v).latitude;
    }
}
