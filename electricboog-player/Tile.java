import bc.*;

public class Tile {
    private MapLocation mapLocation;
    private long karbonite;
    private UnitType unitType;
    private boolean isEnemy;
    private boolean isPassable;
    private Unit unit;

    public Tile(MapLocation location, PlanetMap map) {
        mapLocation = location;
        karbonite = map.initialKarboniteAt(location);
        isPassable = (map.isPassableTerrainAt(location) == 1);
        unit = null;
        isEnemy = false;
    }

    public MapLocation getMapLocation() {
        return mapLocation;
    }

    public long getKarbonite() {
        return karbonite;
    }

    public void setKarbonite(long karbonite) {
        this.karbonite = karbonite;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public Unit getUnit() {
        return unit;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        unitType = unit.unitType();
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setIsEnemy(boolean enemy) {
        isEnemy = enemy;
    }
}