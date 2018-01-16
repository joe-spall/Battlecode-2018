public class Grid {
    private Tile[][] grid;

    public Grid(PlanetMap map) {
        for (int w = 0; w < map.width(); w++) {
            for (int h = 0; h < map.height(); h++) {
                grid[w][h] = new Tile(new MapLocation(map.getPlanet(), w, h), map);
            }
        }
    }

    public Tile getTileAt(int x, int y) {
        return grid[x][y];
    }
}