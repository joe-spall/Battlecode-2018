import bc.*;
import java.util.ArrayList;
class ResearchManager {

    private ArrayList<UnitType> offensiveUnits;
    private ArrayList<UnitType> defensiveUnits;

    private GameController gameController;
    private ResearchTag tag;

    /*
        Constructor 
    */

    ResearchManager(GameController gameController, ResearchTag _tag) {
        // TODO Set priority of units
        offensiveUnits = new ArrayList<UnitType>();
        offensiveUnits.add(UnitType.Ranger);
        offensiveUnits.add(UnitType.Healer);
        offensiveUnits.add(UnitType.Knight);

        defensiveUnits = new ArrayList<UnitType>();
        defensiveUnits.add(UnitType.Mage);
        defensiveUnits.add(UnitType.Knight);
        defensiveUnits.add(UnitType.Healer);

        tag = _tag;
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
            Works on Rocket. If completed, works on Worker. If completed, switches to Offensive.
        */

        if (tag == ResearchTag.ROCKET) {
            if (resInfo.getLevel(UnitType.Rocket) < 3) {
                gameController.queueResearch(UnitType.Rocket);
                System.out.println("Research: Starting, Type: Rocket, Level: " + resInfo.getLevel(UnitType.Rocket));
                return;
            } else {
                 if (resInfo.getLevel(UnitType.Worker) < 3) {
                    setCurrentTags(ResearchTag.WORKER);
                    return;
                 }
                 else {
                    setCurrentTags(ResearchTag.OFFENSIVE);
                    return;
                 }
            }
        }

        if (tag == ResearchTag.WORKER) {
            if (resInfo.getLevel(UnitType.Worker) < 3) {
                gameController.queueResearch(UnitType.Worker);
                System.out.println("Research: Starting, Type: Worker, Level: " + resInfo.getLevel(UnitType.Worker));
                return;
            } else {
                setCurrentTags(ResearchTag.DEFENSIVE);
                return;
            }
        }

        if (tag == ResearchTag.DEFENSIVE) {

            int minIndx = findMinIdxUnits(resInfo, offensiveUnits);
            if(resInfo.getLevel(offensiveUnits.get(minIndx)) == 3){
                setCurrentTags(ResearchTag.WORKER);
                return;
            }
            gameController.queueResearch(offensiveUnits.get(minIndx));
            System.out.println("Research: Starting, Type: Offensive, Unit: " + offensiveUnits.get(minIndx) + ", Level: " + resInfo.getLevel(offensiveUnits.get(minIndx)));
            return;

        } else if (tag == ResearchTag.DEFENSIVE) {
                
            int minIndx = findMinIdxUnits(resInfo, defensiveUnits);
            if (resInfo.getLevel(defensiveUnits.get(minIndx)) == 3) {
                setCurrentTags(ResearchTag.WORKER);
                return;
            }
            gameController.queueResearch(defensiveUnits.get(minIndx));
            System.out.println("Research: Starting, Type: Defensive, Unit: " + defensiveUnits.get(minIndx) + ", Level: " + resInfo.getLevel(defensiveUnits.get(minIndx)));
            return;
        }
    }

    public void setCurrentTags(ResearchTag newTag) {
        // TODO Calculate tag strategy
        tag = newTag;
        System.out.println("Research: Switching, Type: " + tag);
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