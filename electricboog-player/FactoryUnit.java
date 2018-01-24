import bc.*;

public class FactoryUnit extends BoogUnit {

    private final static char ROCKET_WORKER = 'a';
    private final static char FACTORY_WORKER = 'b';
    private final static char EARTH_FARM_WORKER = 'c';
    private final static char MARS_FARM_WORKER = 'd';
    private final static char EARTH_DEFENSE_KNIGHT = 'e';
    private final static char MARS_DEFENSE_KNIGHT = 'f';
    private final static char EARTH_ATTACK_KNIGHT = 'g';
    private final static char MARS_ATTACK_KNIGHT = 'h';
    private final static char EARTH_MAGE = 'i';
    private final static char MARS_MAGE = 'j';
    private final static char EARTH_SCOUT_RANGER = 'k';
    private final static char MARS_SCOUT_RANGER = 'l';
    private final static char SNIPER_RANGER = 'm';
    private final static char EARTH_HEALER = 'n';
    private final static char MARS_HEALER = 'o';
    private final static Direction[] directions = Direction.values();

    private long turnShouldFinishProduce = 1000;
    private Unit unit;
    private GameController gc;
    private Grid grid;


    public FactoryUnit(Unit unit, GameController gc, Grid grid) {
        super(unit, gc, grid);
        this.unit = unit;
        this.gc = gc;
        this.grid = grid;
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
    public void move(UnitManager unitManager) {

    }

    /*
        decides if and where a unit should perform
        an action.
    */
    public void action(UnitManager unitManager) {
        if(unit.structureIsBuilt() == 1){
            int id = unit.id();
            int numberOfWorkers = unitManager.getNumWorkers();
            int numberOfRangers = unitManager.getNumRangers();
            int numberOfKnights = unitManager.getNumKnights();
            int numberOfUnits = numberOfWorkers + numberOfRangers + numberOfKnights;
            if((gc.canProduceRobot(id, UnitType.Worker) && numberOfWorkers < 2) || (numberOfWorkers/numberOfUnits < 0.2)){
                // Producing workers
                gc.produceRobot(id, UnitType.Worker);
                turnShouldFinishProduce = gc.round() + 6;
                System.out.println("Factory: Starting Production of Worker");
            } else if(gc.canProduceRobot(id, UnitType.Ranger)){
                // Producing Ranger
                gc.produceRobot(id, UnitType.Ranger);
                turnShouldFinishProduce = gc.round() + 6;
                System.out.println("Factory: Starting Production of Ranger");
            }


            // Tagging
            // TODO add the reset of the tags
            if(gc.round() >= turnShouldFinishProduce){
                turnShouldFinishProduce = 1000;
                VecUnitID currentGarrison = unit.structureGarrison();
                if(currentGarrison.size() > 0){
                    int newUnitID = currentGarrison.get(currentGarrison.size()-1);
                    Unit newUnit = gc.unit(newUnitID);
                    char setTag = 0;
                    if(newUnit.unitType() == UnitType.Worker){
                        setTag = EARTH_FARM_WORKER;
                        BoogUnit boog = new WorkerUnit(newUnit,gc,grid);
                        unitManager.add(boog);
                        unitManager.changeTag(boog, setTag);
                        System.out.println("Factory: Assigning Tag of Worker");

                    } else if(newUnit.unitType() == UnitType.Ranger){
                        setTag = SNIPER_RANGER;
                        BoogUnit boog = new RangerUnit(newUnit,gc,grid);
                        unitManager.add(boog);
                        unitManager.changeTag(boog, setTag);
                        System.out.println("Factory: Assigning Tag of Ranger");
                    }
                    

                }
            }


            // Removing from garrison
            // TODO Be smarter about garrison
            for (Direction direction: directions) {
                if (gc.canUnload(id, direction) ) {
                    gc.unload(id, direction);
                    System.out.println("Factory: Unloaded New Unit");
                    break;
                }
            }



        }

        



    }
}