import bc.*;

abstract class BoogUnit {
    private Unit unit;
    private char tag;
    private char status;
    BoogUnit(Unit unit) {
        this.unit = unit;
        tag = '0';
        status = '0';
    }

    public void setTag(char tagName) {

        tag = tagName;
    }

    public char getTag() {
        return tag;
    }

    public void setStatus(char statusName) {
        status = statusName;
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

    }

    public abstract void adjustTag();

    public abstract void move();

    public abstract void action();
}