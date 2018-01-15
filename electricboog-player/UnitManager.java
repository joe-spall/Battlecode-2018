import bc.*;
import java.util.ArrayList;

class UnitManager {
    private ArrayList<BoogUnit>[] units;
    private HashMap<Character, ArrayList<BoogUnit>> tagUnits;


    UnitManager() {
        units = new ArrayList[7];
        for (int k = 0; k < 7; k++) {
            units[k] = new ArrayList<>();
        }
        tagUnits = new ArrayList[17];
        for (int k = 0; k < 17; k++) {
            tagUnits[k] = new ArrayList<>();
        }
    }

    public int getNumWorkers() {
        return units[0].size();
    }

    public ArrayList<BoogUnit> getWorkers() {
        return units[0];
    }

    public int getNumRangers() {
        return units[2].size();
    }

    public ArrayList<BoogUnit> getRangers() {
        return units[2];
    }

    public int getNumKnights() {
        return units[1].size();
    }

    public ArrayList<BoogUnit> getKnights() {
        return units[1];
    }

    public int getNumMages() {
        return units[3].size();
    }

    public ArrayList<BoogUnit> getMages() {
        return units[3];
    }

    public int getNumHealers() {
        return units[4].size();
    }

    public ArrayList<BoogUnit> getHealers() {
        return units[4];
    }

    public int getNumRockets() {
        return units[5].size();
    }

    public ArrayList<BoogUnit> getRockets() {
        return units[5];
    }

    public int getNumFactories() {
        return units[6].size();
    }

    public ArrayList<BoogUnit> getFactories() {
        return units[6];
    }

    public ArrayList<BoogUnit> getTagWorkers(char tag) {
        return tagUnits.get(tag);
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

    public void changeTag(BoogUnit unit, char tag) {
        if (unit.getTag() == '0') {
            if (tagUnits.containsKey(tag)) {
                tagUnits.replace(tag, tagUnits.get(char).add(unit));
            }
        } else {
            if (tagUnits.containsKey(tag)) {
                tagUnits.replace(tag, tagUnits.get(char).add(unit));
                tagUnits.replace(unit.getTag(), tagUnits.get(unit.getTag().remove(unit)));
            }
        }
        unit.setTag(tag);
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