import bc.*;
import java.util.ArrayList;

class UnitManager {
    private ArrayList<BoogUnit>[] units;
    UnitManager() {
        units = new ArrayList[7];
        for (int k = 0; k < 7; k++) {
            units[k] = new ArrayList<>();
        }
    }

    public int getNumWorkers() {
        return units[0].size();
    }

    public int getNumRangers() {
        return units[2].size();
    }

    public int getNumKnights() {
        return units[1].size();
    }

    public int getNumMages() {
        return units[3].size();
    }

    public int getNumHealers() {
        return units[4].size();
    }

    public int getNumRockets() {
        return units[5].size();
    }

    public int getNumFactories() {
        return units[6].size();
    }

    public void add(BoogUnit unit) {
        UnitType type = unit.getUnit().unitType();
        if (type.equals(UnitType.Worker)) {
            units[0].add(unit);
        } else if (type.equals(UnitType.Knight)) {
            units[1].add(unit);
        } else if (type.equals(UnitType.Ranger)) {
            units[2].add(unit);
        } else if (type.equals(UnitType.Mage)) {
            units[3].add(unit);
        } else if (type.equals(UnitType.Healer)) {
            units[4].add(unit);
        } else if (type.equals(UnitType.Rocket)) {
            units[5].add(unit);
        } else if (type.equals(UnitType.Factory)) {
            units[6].add(unit);
        }
    }

    public void update() {
        for (ArrayList<BoogUnit> unitArray : units) {
            for (BoogUnit unit : unitArray) {
                if (unit.getUnit().health() == 0 && unit.getUnit().location().isInGarrison() == false) {
                    unitArray.remove(unit);
                }
            }
        }
    }

    public void printList() {
        System.out.println("[");
        for (ArrayList<BoogUnit> unitArray : units) {
            System.out.print("New Unit: ");
            for (BoogUnit unit : unitArray) {
                System.out.print(unit.getUnit().unitType() + ": " + unit.getUnit().id() + ", ");
            }
            System.out.print(".");
        }
    }
}