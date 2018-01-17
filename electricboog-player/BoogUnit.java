import bc.*;

abstract class BoogUnit {
    private Unit unit;
    private char movementTag;
    private char statusTag;
    private Grid grid;
    private GameController gc;

    BoogUnit(Unit unit, GameController gc, Grid grid) {
        this.unit = unit;
        movementTag = '0';
        statusTag = '0';
        this.gc = gc;
        this.grid = grid;
    }

    public void setMovementTag(char tagName) {

        movementTag = tagName;
    }

    public char getMovementTag() {
        return movementTag;
    }

    public void setStatusTag(char tagName) {
        statusTag = tagName;
    }

    public char getStatusTag() {
        return statusTag;
    }

    public Unit getUnit() {
        return unit;
    }

    /*
        implement single vision method for all
        BoogUnits that updates the grid based on its current vision
    */
    public void vision() {
        VecMapLocation locations = gc.allLocationsWithin(unit.location().mapLocation(), unit.visionRange());
        for (int k = 0; k < locations.size(); k++) {
            MapLocation location = locations.get(k);
            long karbonite = gc.karboniteAt(location);
            Tile tile = grid.getTileAt(location.getX(), location.getY());
            tile.setKarbonite(karbonite);
            if (gc.hasUnitAtLocation(location)) {
                Unit unitAtLocation = gc.senseUnitAtLocation(location);
                tile.setUnit(unitAtLocation);
                if (unitAtLocation != null) {
                    tile.setIsEnemy(!unit.team().equals(unitAtLocation.team()));
                } else {
                    tile.setIsEnemy(false);
                }
            }



        }
    }

    public abstract void adjustTag();

    public abstract void move();

    public abstract void action();
}