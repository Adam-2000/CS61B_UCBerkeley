
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final int MAX_LOD = 7;
    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        double desiredUp = params.get("ullat");
        double desiredLeft = params.get("ullon");
        double desiredLow = params.get("lrlat");
        double desiredRight = params.get("lrlon");
        double desiredHeight = params.get("h");
        double desiredWidth = params.get("w");
        double desiredLonDPP = (desiredRight - desiredLeft) / desiredWidth;
        int depth;
        boolean querySuccess = false;
        double left = MapServer.ROOT_ULLON;
        double right = MapServer.ROOT_LRLON;
        double up = MapServer.ROOT_ULLAT;
        double low = MapServer.ROOT_LRLAT;
        double lonDpp = (right - left) / MapServer.TILE_SIZE;
        int xl, xr;
        int yu, yl;
        String[][] grid;
        results.put("render_grid", null);
        results.put("raster_ul_lon", left);
        results.put("raster_ul_lat", up);
        results.put("raster_lr_lon", right);
        results.put("raster_lr_lat", low);
        results.put("depth", 0);
        results.put("query_success", false);
        if (desiredLeft >= right || desiredRight < left || desiredUp < low || desiredLow >= up) {
            return results;
        }
        for (depth = 0; depth <= MAX_LOD; depth++) {
            if (lonDpp < desiredLonDPP) {
                break;
            }
            lonDpp /= 2.0;
        }
        depth = Math.min(depth, MAX_LOD);
        int gridMax = (int) Math.pow(2, depth);
        double gridW = (right - left) / gridMax;
        double gridH = (up - low) / gridMax;
        if (desiredLeft < left) {
            xl = 0;
        } else {
            xl = (int) Math.floor((desiredLeft - left) / gridW);
        }
        if (desiredRight >= right) {
            xr = gridMax - 1;
        } else {
            xr = (int) Math.floor((desiredRight - left) / gridW);
        }
        if (desiredUp > up) {
            yu = 0;
        } else {
            yu = (int) Math.floor((up - desiredUp) / gridH);
        }
        if (desiredLow <= low) {
            yl = gridMax - 1;
        } else {
            yl = (int) Math.floor((up - desiredLow) / gridH);
        }
        grid = new String[yl - yu + 1][xr - xl + 1];
        for (int i = yu; i <= yl; i++) {
            for (int j = xl; j <= xr; j++) {
                grid[i - yu][j - xl] = "d" + depth + "_x" + j + "_y" + i + ".png";
            }
        }
        right = left + (xr + 1) * gridW;
        left = left + xl * gridW;
        low = up - (yl + 1) * gridH;
        up = up - yu * gridH;
        results.put("render_grid", grid);
        results.put("raster_ul_lon", left);
        results.put("raster_ul_lat", up);
        results.put("raster_lr_lon", right);
        results.put("raster_lr_lat", low);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }
}
