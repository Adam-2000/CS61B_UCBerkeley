package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class HexagonWorldDemo {

    private static final int mapHexagonSize = 3;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static void addHexagon(int n, int x, int y, TETile t, TETile[][] world) {
        for (int yy = 0; yy < n; yy++) {
            for (int xx = n - 1 - yy; xx < 2 * n - 1 + yy; xx++) {
                world[x + xx][y + yy] = new TETile(t);
                world[x + xx][y + 2 * n - 1 - yy] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        return switch (tileNum) {
            case 0: yield Tileset.GRASS;
            case 1: yield Tileset.FLOWER;
            case 2: yield Tileset.WATER;
            case 3: yield Tileset.SAND;
            case 4: yield Tileset.MOUNTAIN;
            case 5: yield Tileset.TREE;
            default: yield Tileset.NOTHING;
        };
    }

    public static void main (String args[]) {

        int tileHexagonSize = 3; //Integer.parseInt(args[0]);
        int width = (2 * mapHexagonSize - 1) * (2 * tileHexagonSize - 1) + tileHexagonSize - 1 + 2;
        int height = (2 * mapHexagonSize - 1) * 2 * tileHexagonSize + 2;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        // initialize tiles
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int tile_unit_x = 2 * tileHexagonSize - 1;
        for (int i = 0; i < 2 * mapHexagonSize - 1; i++) {
            int x = i * tile_unit_x + 1;
            int y = Math.abs(mapHexagonSize - 1 - i) * tileHexagonSize + 1;
            for (int j = 0; j < 2 * mapHexagonSize - 1 - Math.abs(mapHexagonSize - 1 - i); j++) {
                addHexagon(tileHexagonSize, x, y, randomTile(), world);
                y += 2 * tileHexagonSize;
            }
        }

        ter.renderFrame(world);
    }
}
