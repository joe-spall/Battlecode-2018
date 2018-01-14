import bc.*;

class BoogUnit {
    private Unit unit;
    private char tag;
    BoogUnit(Unit unit) {
        this.unit = unit;
        tag = '0';
    }

    public void setTag(char tagName) {
        tag = tagName;
    }

    public char getTag() {
        return tag;
    }

    public Unit getUnit() {
        return unit;
    }
}