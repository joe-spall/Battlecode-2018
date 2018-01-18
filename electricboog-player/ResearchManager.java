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
    }

    public void checkCurrentQueue() {
        ResearchInfo resInfo = gameController.researchInfo();
        if (resInfo.roundsLeft() == 1 || !resInfo.hasNextInQueue()) {
            setQueueForTags();
            System.out.println("Finished research, changing queue");
        }
    }

    public void setQueueForTags() {
        ResearchInfo resInfo = gameController.researchInfo();
        if(objectiveTag == ROCKET_RESEARCH && resInfo.getLevel(UnitType.Rocket) < 3){
            gameController.queueResearch(UnitType.Rocket);
            return; 
        }

    	if (locationTag == EARTH_RESEARCH) {
            if (objectiveTag == OFFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> offensiveUnits = new ArrayList<UnitType>();
                offensiveUnits.add(UnitType.Knight);
                offensiveUnits.add(UnitType.Ranger);
                offensiveUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveUnits);
                if(resInfo.getLevel(offensiveUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveUnits.get(minIndx));
                
            } else if (objectiveTag == DEFENSIVE_RESEARCH) {
                ArrayList<UnitType> offensiveUnits = new ArrayList<UnitType>();
                offensiveUnits.add(UnitType.Knight);
                offensiveUnits.add(UnitType.Ranger);
                offensiveUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveUnits);
                if(resInfo.getLevel(offensiveUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveUnits.get(minIndx));

            }
        } else if (locationTag == MARS_RESEARCH) {
            if (objectiveTag == OFFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> offensiveUnits = new ArrayList<UnitType>();
                offensiveUnits.add(UnitType.Knight);
                offensiveUnits.add(UnitType.Ranger);
                offensiveUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveUnits);
                if(resInfo.getLevel(offensiveUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveUnits.get(minIndx));

            } else if (objectiveTag == DEFENSIVE_RESEARCH) {
                // TODO Set priority of units
                ArrayList<UnitType> offensiveUnits = new ArrayList<UnitType>();
                offensiveUnits.add(UnitType.Knight);
                offensiveUnits.add(UnitType.Ranger);
                offensiveUnits.add(UnitType.Healer);
                int minIndx = findMinIdxUnits(resInfo, offensiveUnits);
                if(resInfo.getLevel(offensiveUnits.get(minIndx)) == 3){
                    // TODO Add second tier
                }
                gameController.queueResearch(offensiveUnits.get(minIndx));
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