// import the API.
// See xxx for the javadocs.
import bc.*;

public class Player {
    public static void main(String[] args) {
        // You can use other files in this directory, and in subdirectories.
        Extra extra = new Extra(27);
        System.out.println(extra.toString());

        // MapLocation is a data structure you'll use a lot.
        MapLocation loc = new MapLocation(Planet.Earth, 10, 20);
        System.out.println("loc: "+loc+", one step to the Northwest: "+loc.add(Direction.Northwest));
        System.out.println("loc x: "+loc.getX());

        // One slightly weird thing: some methods are currently static methods on a static class called bc.
        // This will eventually be fixed :/
        System.out.println("Opposite of " + Direction.North + ": " + bc.bcDirectionOpposite(Direction.North));

        // Connect to the manager, starting the game
        GameController gc = new GameController();

        // Direction is a normal java enum.
        Direction[] directions = Direction.values();

        while (true) {
            System.out.println("Current round: "+gc.round());
            System.out.println("Current Karb: "+gc.karbonite());
            // VecUnit is a class that you can think of as similar to ArrayList<Unit>, but immutable.
            VecUnit units = gc.myUnits();

            for (int i = 0; i < units.size(); i++) {
                Unit unit = units.get(i);

                boolean harvested = false;

                if(gc.canHarvest(unit.id(),Direction.North)){
                    gc.canHarvest(unit.id(),Direction.North);   
                    System.out.println("Harvested N");
                    harvested = true;  
                }

                if(gc.canHarvest(unit.id(),Direction.South)){
                    gc.canHarvest(unit.id(),Direction.South);
                    System.out.println("Harvested S");
                    harvested = true;         
                }

                if(gc.canHarvest(unit.id(),Direction.West)){
                    gc.canHarvest(unit.id(),Direction.West);
                    System.out.println("Harvested W");
                    harvested = true;         
                }

                if(gc.canHarvest(unit.id(),Direction.East)){
                    gc.canHarvest(unit.id(),Direction.East);
                    System.out.println("Harvested E");
                    harvested = true;         
                }

                if(gc.canHarvest(unit.id(),Direction.Southeast)){
                    gc.canHarvest(unit.id(),Direction.Southeast);
                    System.out.println("Harvested SE");
                    harvested = true;         
                }

                if(gc.canHarvest(unit.id(),Direction.Southwest)){
                    gc.canHarvest(unit.id(),Direction.Southwest);
                    System.out.println("Harvested SW");
                    harvested = true;         
                }

                if(gc.canHarvest(unit.id(),Direction.Northeast)){
                    gc.canHarvest(unit.id(),Direction.Northeast);  
                    System.out.println("Harvested NE");
                    harvested = true;       
                }

                if(gc.canHarvest(unit.id(),Direction.Northwest)){
                    gc.canHarvest(unit.id(),Direction.Northwest);
                    System.out.println("Harvested NW");   
                    harvested = true;    
                }
                // Most methods on gc take unit IDs, instead of the unit objects themselves.
                if (gc.isMoveReady(unit.id()) && gc.canMove(unit.id(), Direction.North) && !harvested) {
                    gc.moveRobot(unit.id(), Direction.North);

                }

                
            }
            // Submit the actions we've done, and wait for our next turn.
            gc.nextTurn();
        }
    }
}