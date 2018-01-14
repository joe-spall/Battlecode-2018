// import the API.
// See xxx for the javadocs.
import bc.*;
import java.util.HashMap;
import java.util.Random;

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
        Team enemy = null;
        if (gc.team().equals(Team.Blue)) {
            enemy = Team.Red;
        } else {
            enemy = Team.Blue;
        }
        // Direction is a normal java enum.
        Direction[] directions = Direction.values();
        HashMap<Integer, Direction> unitDirections = new HashMap();
        gc.queueResearch(UnitType.Rocket);
        while (true) {
            System.out.println("Current round: "+gc.round());
            System.out.println("Current Karb: "+gc.karbonite());
            // VecUnit is a class that you can think of as similar to ArrayList<Unit>, but immutable.
            VecUnit units = gc.myUnits();

            for (int i = 0; i < units.size(); i++) {
                Unit unit = units.get(i);
                int id = unit.id();
                if (unit.unitType().equals(UnitType.Worker)) {


                    boolean harvested = false;

                    if (!unit.location().isInGarrison()) {
                        VecUnit nearbyRockets = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Rocket);
                        for (int k = 0; k < nearbyRockets.size(); k++) {
                            Unit rocket = nearbyRockets.get(k);
                            if (gc.canBuild(id, rocket.id())) {
                                gc.build(id, rocket.id());
                            }
                        }

                        VecUnit nearbyUnits = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Factory);
                        for (int k = 0; k < nearbyUnits.size(); k++) {
                            Unit factory = nearbyUnits.get(k);
                            if (gc.canBuild(id, factory.id())) {
                                gc.build(id, factory.id());
                            }
                        }

                        //if over 150 karbonite, build a factory
                        if (gc.karbonite() > 150) {
                            for (Direction direction: directions) {
                                if (gc.canBlueprint(id, UnitType.Rocket, direction) && nearbyRockets.size() == 0) {
                                    gc.blueprint(id, UnitType.Rocket, direction);
                                    break;
                                }
                                if (gc.canBlueprint(id, UnitType.Factory, direction) && nearbyUnits.size() < 2) {
                                    gc.blueprint(id, UnitType.Factory, direction);
                                    break;
                                }
                            }
                        }
                    }


                    for (Direction direction: directions) {
                        if (gc.canBlueprint(id, UnitType.Factory, direction)) {
                            gc.blueprint(id, UnitType.Factory, direction);
                            break;
                        }
                    }
                    if(gc.canHarvest(id,Direction.North)){
                        gc.canHarvest(id,Direction.North);
                        System.out.println("Harvested N");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.South)){
                        gc.canHarvest(id,Direction.South);
                        System.out.println("Harvested S");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.West)){
                        gc.canHarvest(id,Direction.West);
                        System.out.println("Harvested W");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.East)){
                        gc.canHarvest(id,Direction.East);
                        System.out.println("Harvested E");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.Southeast)){
                        gc.canHarvest(id,Direction.Southeast);
                        System.out.println("Harvested SE");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.Southwest)){
                        gc.canHarvest(id,Direction.Southwest);
                        System.out.println("Harvested SW");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.Northeast)){
                        gc.canHarvest(id,Direction.Northeast);
                        System.out.println("Harvested NE");
                        harvested = true;
                    }

                    if(gc.canHarvest(id,Direction.Northwest)){
                        gc.canHarvest(id,Direction.Northwest);
                        System.out.println("Harvested NW");
                        harvested = true;
                    }
                    // Most methods on gc take unit IDs, instead of the unit objects themselves.
                    if (gc.isMoveReady(id) && gc.canMove(id, Direction.North) && !harvested) {
                        gc.moveRobot(id, Direction.North);

                    }
                } else if (unit.unitType().equals(UnitType.Factory)) {
                    //if it can produce a worker, it does
                    if (gc.canProduceRobot(id, UnitType.Worker) && units.size() < 10) {
                        gc.produceRobot(id, UnitType.Worker);
                        System.out.println("Worker Created");
                    }
                    //if it can produce a mage, it does
                    if (gc.canProduceRobot(id, UnitType.Mage) && units.size() < 15 && false) {
                        gc.produceRobot(id, UnitType.Mage);
                        System.out.println("Mage Created");
                    }
                    //if it can unload the contents of the factory,
                    //it does in the first direction it can.
                    for (Direction direction: directions) {
                        if (gc.canUnload(id, direction)) {
                            System.out.println("Unloaded");
                            gc.unload(id, direction);

                            break;
                        }
                    }

                } else if (unit.unitType().equals(UnitType.Mage)) {

                    if (unitDirections.containsKey(id)) {
                        System.out.println(unit.attackRange());
                        VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.attackRange(), enemy);
                        if (nearbyUnits.size() != 0) {
                            System.out.println("Found nearby unit");
                            long minDistance = unit.attackRange();
                            int min = 0;
                            long maxDistance = 0;
                            int max = 0;
                            for (int k = 0; k < nearbyUnits.size(); k++) {
                                Unit enemyUnit = nearbyUnits.get(k);
                                if (enemyUnit.location().mapLocation().
                                    distanceSquaredTo(unit.location().mapLocation()) < minDistance) {
                                    minDistance = enemyUnit.location().mapLocation().distanceSquaredTo(unit.location().mapLocation());
                                    min = k;
                                }
                                if (enemyUnit.location().mapLocation().
                                    distanceSquaredTo(unit.location().mapLocation()) > maxDistance) {
                                    maxDistance = enemyUnit.location().mapLocation().distanceSquaredTo(unit.location().mapLocation());
                                    max = k;
                                }
                            }
                            Unit minUnit = nearbyUnits.get(min);
                            Unit maxUnit = nearbyUnits.get(max);
                            if (gc.isMoveReady(id) && gc.canMove(

                                id, bc.bcDirectionOpposite(minUnit.location().mapLocation().directionTo(unit.location().mapLocation())))) {
                                System.out.println("Attacking close");
                                gc.moveRobot(id, bc.bcDirectionOpposite(minUnit.location().mapLocation().directionTo(unit.location().mapLocation())));
                                if (gc.canAttack(id, minUnit.id()) && gc.isAttackReady(id)) {
                                    gc.attack(id, minUnit.id());
                                }
                            } else {
                                System.out.println("Attacking far");
                                if (gc.canAttack(id, maxUnit.id()) && gc.isAttackReady(id)) {
                                    gc.attack(id, maxUnit.id());
                                }
                            }
                        } else if (gc.canMove(id,unitDirections.get(id)) && gc.isMoveReady(id)) {
                            gc.moveRobot(id,unitDirections.get(id));
                        } else {
                            Direction newDirection = unitDirections.get(id);
                            int count = 0;
                            while (!gc.canMove(id, newDirection) && count < 15) {
                                int rnd = new Random().nextInt(directions.length);
                                newDirection = directions[rnd];
                                count++;
                            }
                            if (gc.canMove(id, newDirection) && gc.isMoveReady(id)) {
                                unitDirections.replace(id, newDirection);
                                gc.moveRobot(id, newDirection);
                            }
                        }

                    } else {
                        int rnd = new Random().nextInt(directions.length);
                        Direction newDirection = directions[rnd];

                        int count = 0;
                        while (!gc.canMove(id, newDirection) && count < 15) {
                            rnd = new Random().nextInt(directions.length);
                            newDirection = directions[rnd];
                            count++;
                        }
                        if (gc.canMove(id, newDirection) && gc.isMoveReady(id)) {
                            unitDirections.put(id, newDirection);
                            gc.moveRobot(id, newDirection);
                        }
                    }
                }


            }
            // Submit the actions we've done, and wait for our next turn.
            gc.nextTurn();
        }
    }
}