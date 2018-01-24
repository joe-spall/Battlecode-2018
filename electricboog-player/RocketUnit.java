import bc.*;

public class RocketUnit extends BoogUnit {
    private static long MAX_HEALTH = 0;
    private Unit unit;
    private GameController gc;

    public RocketUnit(Unit unit, GameController gc, Grid grid) {
        super(unit, gc, grid);
        this.unit = unit;
        this.gc = gc;
    }

    /*
        DO NOT EDIT. There is only one type of rocket.
    */
    public void adjustTag(UnitManager unitManager) {

    }

    /*
        DO NOT EDIT. Rocket's don't move.
    */
    public void move(UnitManager unitManager) {

    }

    /*
        decides if and where a unit should perform
        an action.
    */
    public void action(UnitManager unitManager) {
        if (unit.structureIsBuilt() == 0) {
            MAX_HEALTH = unit.health();
        } else if (unit.health() < MAX_HEALTH 
            || unit.structureGarrison().size() >= unit.structureMaxCapacity() 
            || gc.round() >= 745) {
            boolean sent = false;
            int count = 0;
            while(count < 16 && !sent) {
                MapLocation temp = new MapLocation(Planet.Mars, (int) (Math.random() * 21), (int) (Math.random() * 21));
                if (gc.canLaunchRocket(unit.id(), temp)) {
                    gc.launchRocket(unit.id(), temp);
                    sent = true;
                    count = 0;
                }
                count++;
            }
        }
    }
}