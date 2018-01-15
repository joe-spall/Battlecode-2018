import bc.*;

abstract class BoogUnit {
    private Unit unit;
    private char movementTag;
    private char statusTag;
    BoogUnit(Unit unit) {
        this.unit = unit;
        movementTag = '0';
        statusTag = '0';
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

    }

    public abstract void adjustTag();

    public abstract void move();

    public abstract void action();
}