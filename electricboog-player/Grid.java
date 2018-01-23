import bc.*;

public class Grid {
    private Tile[][] grid;

    public Grid(PlanetMap map) {
        grid = new Tile[(int)map.getWidth()][(int)map.getHeight()];
        for (int w = 0; w < map.getWidth(); w++) {
            for (int h = 0; h < map.getHeight(); h++) {
                grid[w][h] = new Tile(new MapLocation(map.getPlanet(), w, h), map);
            }
        }
    }

    public Tile getTileAt(int x, int y) {
        return grid[x][y];
    }

    public Tile getTileAt(MapLocation location) {

        return grid[location.getX()][location.getY()];
    }

    public boolean isOnMap(MapLocation location) {
        return location.getX() < grid.length && location.getX() >= 0 && location.getY() < grid[0].length && location.getY() >= 0;
    }
}