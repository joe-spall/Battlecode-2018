import bc.*;
import java.util.ArrayList;
class ResearchManager {


    private final static char EARTH_RESEARCH = 'a';
    private final static char MARS_RESEARCH = 'b';
    private final static char OFFENSIVE_RESEARCH = 'a';
    private final static char DEFENSIVE_RESEARCH = 'b';
    private final static char ROCKET_RESEARCH = 'c';
    private ArrayList<UnitType> finishedUnits;
    private GameController gameController;

    private char locationTag;
    private char objectiveTag;

    ResearchManager(GameController gameController, char _objectiveTag) {
        finishedUnits = new ArrayList<UnitType>();
        locationTag = EARTH_RESEARCH;
        objectiveTag = _objectiveTag;
        this.gameController = gameController;
        this.gameController.queueResearch(UnitType.Worker);
        System.out.println("Research: Initalized");
    }

    public void checkCurrentQueue() {
        ResearchInfo resInfo = gameController.researchInfo();
        if (!resInfo.hasNextInQueue()) {
        	System.out.println("Research: Finished");
            setQueueForTags();
        }
    }

    public void setQueueForTags() {
        ResearchInfo resInfo = gameController.researchInfo();
        if(objectiveTag == ROCKET_RESEARCH && resInfo.getLevel(UnitType.Rocket) < 3){
            gameController.queueResearch(UnitType.Rocket);
            System.out.println("Research: Starting, Type: Rocket, Level: " + resInfo.getLevel(UnitType.Rocket));
            return; 
        }

    	if (locationTag == EARTH_RESEARCH) {
            if (objectiveTag == OFFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> offensiveEarthUnits = new ArrayList<UnitType>();
                offensiveEarthUnits.add(UnitType.Knight);
                offensiveEarthUnits.add(UnitType.Ranger);
                offensiveEarthUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveEarthUnits);
                if(resInfo.getLevel(offensiveEarthUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveEarthUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Offensive Earth, Unit: " + offensiveEarthUnits.get(minIndx) + ", Level: " + resInfo.getLevel(offensiveEarthUnits.get(minIndx)));
                
            } else if (objectiveTag == DEFENSIVE_RESEARCH) {
            	// TODO Set priority of units
                ArrayList<UnitType> defensiveEarthUnits = new ArrayList<UnitType>();
                defensiveEarthUnits.add(UnitType.Knight);
                defensiveEarthUnits.add(UnitType.Ranger);
                defensiveEarthUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, defensiveEarthUnits);
                if(resInfo.getLevel(defensiveEarthUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(defensiveEarthUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Defensive Earth, Unit: " + defensiveEarthUnits.get(minIndx) + ", Level: " + resInfo.getLevel(defensiveEarthUnits.get(minIndx)));
            }
        } else if (locationTag == MARS_RESEARCH) {
            if (objectiveTag == OFFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> offensiveMarsUnits = new ArrayList<UnitType>();
                offensiveMarsUnits.add(UnitType.Knight);
                offensiveMarsUnits.add(UnitType.Ranger);
                offensiveMarsUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveMarsUnits);
                if(resInfo.getLevel(offensiveMarsUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveMarsUnits.get(minIndx));
                System.out.println("Research: Starting, Type: Offensive Mars, Unit: " + offensiveMarsUnits.get(minIndx) + ", Level: " + resInfo.getLevel(offensiveMarsUnits.get(minIndx)));
            } else if (objectiveTag == DEFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> defensiveMarsUnits = new ArrayList<UnitType>();
                defensiveMarsUnits.add(UnitType.Knight);
                defensiveMarsUnits.add(UnitType.Ranger);
                defensiveMarsUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, defensiveMarsUnits);
                if(resInfo.getLevel(defensiveMarsUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(defensiveMarsUnits.get(minIndx));
               	System.out.println("Research: Starting, Type: Defensive Mars, Unit: " + defensiveMarsUnits.get(minIndx) + ",Level: " + resInfo.getLevel(defensiveMarsUnits.get(minIndx)));

            }

        }

    }

    public void setCurrentTags(char newLocationTag, char newObjectiveTag) {
        // TODO Calculate tag strategy
        locationTag = newLocationTag;
        objectiveTag = newObjectiveTag;
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