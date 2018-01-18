import bc.*;

abstract class BoogUnit {
    private Unit unit;
    private char tag;
    private char status;
    private Grid grid;
    private GameController gc;
    private Team enemy;

    BoogUnit(Unit unit, GameController gc, Grid grid) {
        this.unit = unit;
        tag = '0';
        status = '0';
        this.gc = gc;
        this.grid = grid;
        enemy = null;
        if (gc.team().equals(Team.Blue)) {
            enemy = Team.Red;
        } else {
            enemy = Team.Blue;
        }
    }

    public void setTag(char tagName) {

        tag = tagName;
    }

    public Team getEnemy() {
        return enemy;
    }

    public char getTag() {
        return tag;
    }

    public void setStatus(char tagName) {
        status = tagName;
    }

    public char getStatus() {
        return status;
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
            if (gc.canSenseLocation(location)) {
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
    }

    public abstract void adjustTag(UnitManager unitManager);

    public abstract void move();

    public abstract void action(UnitManager unitManager);
}