import bc.*;
import java.util.ArrayList;
class ResearchManager {


    private final static char EARTH_RESEARCH = 'a';
    private final static char MARS_RESEARCH = 'b';
    private final static char OFFENSIVE_RESEARCH = 'a';
    private final static char DEFENSIVE_RESEARCH = 'b';
    private final static char ROCKET_RESEARCH = 'c';
    private final static char WORKER_RESEARCH = 'd';
    private ArrayList<UnitType> offensiveEarthUnits;
    private ArrayList<UnitType> defensiveEarthUnits;
    private ArrayList<UnitType> offensiveMarsUnits;
    private ArrayList<UnitType> defensiveMarsUnits;

    private GameController gameController;

    private char locationTag;
    private char objectiveTag;

    /*
        Constructor 
    */

    ResearchManager(GameController gameController, char _objectiveTag) {
        // TODO Set priority of units
        offensiveEarthUnits = new ArrayList<UnitType>();
        offensiveEarthUnits.add(UnitType.Knight);
        offensiveEarthUnits.add(UnitType.Ranger);
        offensiveEarthUnits.add(UnitType.Healer);

        defensiveEarthUnits = new ArrayList<UnitType>();
        defensiveEarthUnits.add(UnitType.Knight);
        defensiveEarthUnits.add(UnitType.Ranger);
        defensiveEarthUnits.add(UnitType.Healer);

        offensiveMarsUnits = new ArrayList<UnitType>();
        offensiveMarsUnits.add(UnitType.Knight);
        offensiveMarsUnits.add(UnitType.Ranger);
        offensiveMarsUnits.add(UnitType.Healer);

        defensiveMarsUnits = new ArrayList<UnitType>();
        defensiveMarsUnits.add(UnitType.Knight);
        defensiveMarsUnits.add(UnitType.Ranger);
        defensiveMarsUnits.add(UnitType.Healer);

        locationTag = EARTH_RESEARCH;
        objectiveTag = _objectiveTag;
        this.gameController = gameController;
        this.gameController.queueResearch(UnitType.Worker);
        System.out.println("Research: Initalized");
    }

    /*
        Called every turn in Player. Determines if a unit has to be
        added to the research queue as research is completed.
    */

    public void checkCurrentQueue() {
        ResearchInfo resInfo = gameController.researchInfo();
        if (!resInfo.hasNextInQueue()) {
        	System.out.println("Research: Finished");
            setQueueForTags();
        }
    }

    /*
        Called when research is compeleted. Determines, based on the current tag,
        what research should be started. Evenly splits research between unit lists
        until finish current tag. Defaults to research worker or rocket if current
        tag completed. Randomly selects another unti if else.
    */

    public void setQueueForTags() {
        ResearchInfo resInfo = gameController.researchInfo();

        /*
            Works on Rocket. If completed, works on Worker. If completed, switches to Offensive Earth.
        */

        if (objectiveTag == ROCKET_RESEARCH) {
            if (resInfo.getLevel(UnitType.Rocket) < 3) {
                gameController.queueResearch(UnitType.Rocket);
                System.out.println("Research: Starting, Type: Rocket, Level: " + resInfo.getLevel(UnitType.Rocket));
                return;
            } else {
                 if (resInfo.getLevel(UnitType.Worker) < 3) {
                    setCurrentTags(EARTH_RESEARCH, WORKER_RESEARCH);
                    return;
                 }
                 else {
                    setCurrentTags(EARTH_RESEARCH, OFFENSIVE_RESEARCH);
                    return;
                 }
            }
        }

        if (objectiveTag == WORKER_RESEARCH) {
            if (resInfo.getLevel(UnitType.Worker) < 3) {
                gameController.queueResearch(UnitType.Worker);
                System.out.println("Research: Starting, Type: Worker, Level: " + resInfo.getLevel(UnitType.Worker));
                return;
            } else {
                setCurrentTags(EARTH_RESEARCH, DEFENSIVE_RESEARCH);
                return;
            }


        }

    	if (locationTag == EARTH_RESEARCH) {
            /*
                If completed, works on Worker. If completed, works on Defensive Earth.
            */
            if (objectiveTag == OFFENSIVE_RESEARCH) {

                int minIndx = findMinIdxUnits(resInfo, offensiveEarthUnits);
                if(resInfo.getLevel(offensiveEarthUnits.get(minIndx)) == 3){
                    setCurrentTags(EARTH_RESEARCH, WORKER_RESEARCH);
                    return;
                }
                gameController.queueResearch(offensiveEarthUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Offensive Earth, Unit: " + offensiveEarthUnits.get(minIndx) + ", Level: " + resInfo.getLevel(offensiveEarthUnits.get(minIndx)));
                return;

            } else if (objectiveTag == DEFENSIVE_RESEARCH) {
                
                int minIndx = findMinIdxUnits(resInfo, defensiveEarthUnits);
                if (resInfo.getLevel(defensiveEarthUnits.get(minIndx)) == 3) {
                    setCurrentTags(EARTH_RESEARCH, WORKER_RESEARCH);
                    return;
                }
                gameController.queueResearch(defensiveEarthUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Defensive Earth, Unit: " + defensiveEarthUnits.get(minIndx) + ", Level: " + resInfo.getLevel(defensiveEarthUnits.get(minIndx)));
                return;

            }
        } else if (locationTag == MARS_RESEARCH) {
            if (objectiveTag == OFFENSIVE_RESEARCH) {

                int minIndx = findMinIdxUnits(resInfo, offensiveMarsUnits);
                if(resInfo.getLevel(offensiveMarsUnits.get(minIndx)) == 3){
                    setCurrentTags(EARTH_RESEARCH, WORKER_RESEARCH);
                    return;
                }
                gameController.queueResearch(offensiveMarsUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Offensive Mars, Unit: " + offensiveMarsUnits.get(minIndx) + ", Level: " + resInfo.getLevel(offensiveMarsUnits.get(minIndx)));
                return;
            } else if (objectiveTag == DEFENSIVE_RESEARCH) {

                int minIndx = findMinIdxUnits(resInfo, defensiveMarsUnits);
                if(resInfo.getLevel(defensiveMarsUnits.get(minIndx)) == 3){
                    setCurrentTags(EARTH_RESEARCH, WORKER_RESEARCH);
                    return;
                }
                gameController.queueResearch(defensiveMarsUnits.get(minIndx));
               	System.out.println("Research: Starting, Type: Defensive Mars, Unit: " + defensiveMarsUnits.get(minIndx) + ",Level: " + resInfo.getLevel(defensiveMarsUnits.get(minIndx)));
                return;

            }

        }

    }

    public void setCurrentTags(char newLocationTag, char newObjectiveTag) {
        // TODO Calculate tag strategy
        locationTag = newLocationTag;
        objectiveTag = newObjectiveTag;

        String currentType = "";
        if (newObjectiveTag == ROCKET_RESEARCH) {
            currentType = "Rocket";
        } else if (newObjectiveTag == WORKER_RESEARCH) { 
            currentType = "Worker";
        } else if (newLocationTag == EARTH_RESEARCH) {
            if (newObjectiveTag == OFFENSIVE_RESEARCH) {
                currentType = "Earth Offensive";
            } else if (newObjectiveTag == DEFENSIVE_RESEARCH) {
                currentType = "Earth Defensive";
            }
        } else if (newLocationTag == MARS_RESEARCH) {
            if (newObjectiveTag == OFFENSIVE_RESEARCH) {
                currentType = "Mars Offensive";
            } else if (newObjectiveTag == DEFENSIVE_RESEARCH) {
                currentType = "Mars Defensive";
            }
        } else {
            currentType = "WAT";
        }
        System.out.println("Research: Switching, Type: " + currentType);
        reset();
        setQueueForTags();
    }

    public void reset() {
        gameController.resetResearch();
    }

    public int findMinIdxUnits(ResearchInfo resInfo, ArrayList<UnitType> units) {
        long minVal = resInfo.getLevel(units.get(0));
        int minIdx = 0; 
        for(int idx = 1; idx < units.size(); idx++) {
            long currentUnitValue = resInfo.getLevel(units.get(idx));
            if (currentUnitValue < minVal) {
                minVal = currentUnitValue;
                minIdx = idx;
            }   
        }
        return minIdx;
    }
}