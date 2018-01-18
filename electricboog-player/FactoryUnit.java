import bc.*;

public class FactoryUnit extends BoogUnit {

    public FactoryUnit(Unit unit, GameController gc, Grid grid) {
        super(unit, gc, grid);
    }

    /*
        decides whether or not the unit's tag
        needs to be changed.
    */
    public void adjustTag(UnitManager unitManager) {

    }

    /*
        DO NOT EDIT. Factories don't move.
    */
    public void move() {

    }

    /*
        decides if and where a unit should perform
        an action.
    */
    public void action(UnitManager unitManager) {

    }
}