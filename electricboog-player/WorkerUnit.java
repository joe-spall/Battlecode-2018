import bc.*;
import java.util.ArrayList;

public class WorkerUnit extends BoogUnit {

    private final static char ROCKET_WORKER = 'a';
    private final static char FACTORY_WORKER = 'b';
    private final static char EARTH_FARM_WORKER = 'c';
    private final static char MARS_FARM_WORKER = 'd';
    private Unit unit;
    private GameController gc;
    private Grid grid;

    public WorkerUnit(Unit unit, GameController gc, Grid grid) {
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
        int id = unit.id();
        if (getTag() == FACTORY_WORKER) {
            Direction[] directions = new Direction[4];
            directions[0] = Direction.North;
            directions[1] = Direction.East;
            directions[2] = Direction.South;
            directions[3] = Direction.West;
            boolean canBuild = false;
            for (Direction direction : directions) {
                if (gc.karbonite() < 100 || gc.canBlueprint(id, UnitType.Factory, direction)) {
                    canBuild = true;
                }
            }
            if (!canBuild || unitManager.getNumFactories() > 8) {

                unitManager.changeTag(this, EARTH_FARM_WORKER);
            }
            if ((unitManager.getTagWorkers(ROCKET_WORKER) == null || unitManager.getTagWorkers(ROCKET_WORKER).size() <= 2) && gc.round() > 500) {
                unitManager.changeTag(this, ROCKET_WORKER);
            }
        }

        if (getTag() == ROCKET_WORKER) {
            VecUnit nearbyUnits = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Rocket);
            int count = 0;
            for (int k = 0; k < nearbyUnits.size(); k++) {
                if (nearbyUnits.get(k).team().equals(unit.team())) {
                    count++;
                }
            }
            if (count > 0) {
                Unit rocket = nearbyUnits.get(0);
                if (rocket.health() == rocket.maxHealth()) {
                    unitManager.changeTag(this, MARS_FARM_WORKER);
                }
            }

        }

        if (getTag() == MARS_FARM_WORKER) {
            VecUnit nearbyUnits = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Rocket);
            int count = 0;
            for (int k = 0; k < nearbyUnits.size(); k++) {
                if (nearbyUnits.get(k).team().equals(unit.team())) {
                    count++;
                }
            }
            if (unitManager.getNumRockets() == 0 && unitManager.getTagWorkers(ROCKET_WORKER).size() == 0 && gc.round() > 750) {
                unitManager.changeTag(this, ROCKET_WORKER);
            } else if (unitManager.getNumRockets() == 0) {
                unitManager.changeTag(this, EARTH_FARM_WORKER);
            }
        }

        if (getTag() == EARTH_FARM_WORKER) {
            if (gc.round() > 500 && (unitManager.getTagWorkers(ROCKET_WORKER) == null || unitManager.getTagWorkers(ROCKET_WORKER).size() <= 2) && unitManager.getNumRockets() < 2) {
                unitManager.changeTag(this, ROCKET_WORKER);
            } else if (gc.round() > 725) {
                unitManager.changeTag(this, MARS_FARM_WORKER);
            } else if (unitManager.getTagWorkers(FACTORY_WORKER).size() <= 2 && unitManager.getNumFactories() < 8) {
                unitManager.changeTag(this, FACTORY_WORKER);
            }
        }
    }

    /*
        decides what movement a unit should make.
    */
    public void move(UnitManager unitManager) {
        int id = unit.id();
        Direction[] allDirections = Direction.values();
        if (getTag() == FACTORY_WORKER) {


            Direction[] directions = new Direction[4];
            directions[0] = Direction.Northwest;
            directions[1] = Direction.Northeast;
            directions[2] = Direction.Southwest;
            directions[3] = Direction.Southeast;

            if (gc.isMoveReady(id)) {
                VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());

                for (int k = 0; k < nearbyUnits.size(); k++) {
                    Unit enemy = nearbyUnits.get(k);
                    UnitType enemyType = enemy.unitType();
                    if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                        if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                            Direction opposite = unit.location().mapLocation().directionTo(enemy.location().mapLocation());
                            opposite = bc.bcDirectionOpposite(opposite);
                            for (int j = 0; j < allDirections.length; j++) {
                                if (opposite.equals(allDirections[j])) {
                                    if (gc.canMove(id, allDirections[j])) {
                                        gc.moveRobot(id, allDirections[j]);
                                    } else if (gc.canMove(id, allDirections[j + 1])) {
                                        gc.moveRobot(id, allDirections[j + 1]);
                                    } else if (gc.canMove(id, allDirections[j - 1])) {
                                        gc.moveRobot(id, allDirections[j - 1]);
                                    }
                                }
                            }
                        }
                    }
                }

            }
            // if (gc.isMoveReady(id)) {
            //     VecUnit nearbyUnits = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Factory);
            //     Direction nearestDamaged = null;
            //     long nearDistance = 15;
            //     for (int k = 0; k < nearbyUnits.size(); k++) {
            //         Unit factory = nearbyUnits.get(k);
            //         long distanceToFactory = unit.location().mapLocation().distanceSquaredTo(factory.location().mapLocation());
            //         if (factory.health() < factory.maxHealth() && gc.canRepair(id, factory.id())) {
            //             nearestDamaged = null;
            //             break;
            //         }
            //         if (factory.health() < factory.maxHealth() && !gc.canRepair(id, factory.id()) && distanceToFactory < nearDistance) {
            //             nearestDamaged = unit.location().mapLocation().directionTo(factory.location().mapLocation());
            //             nearDistance = distanceToFactory;
            //         }
            //     }
            //     if (nearestDamaged != null) {
            //         for (int k = 0; k < allDirections.length; k++) {
            //             if (nearestDamaged.equals(allDirections[k])) {
            //                 if (gc.canMove(id, allDirections[k])) {
            //                     gc.moveRobot(id, allDirections[k]);
            //                 } else if (gc.canMove(id, allDirections[k + 1])) {
            //                     gc.moveRobot(id, allDirections[k + 1]);
            //                 } else if (gc.canMove(id, allDirections[k - 1])) {
            //                     gc.moveRobot(id, allDirections[k - 1]);
            //                 }
            //             }
            //         }
            //     }
            // }
            if (gc.isMoveReady(id) && !gc.canMove(id, Direction.North) && !gc.canMove(id, Direction.East) &&
                !gc.canMove(id, Direction.South) && !gc.canMove(id, Direction.West)) {

                for (int k = 0; k < 4; k++) {
                    if (gc.isMoveReady(id) && gc.canMove(id, directions[k])) {
                        gc.moveRobot(id, directions[k]);
                    }
                }


            }
        } else if (getTag() == ROCKET_WORKER) {
            VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());

            for (int k = 0; k < nearbyUnits.size(); k++) {
                Unit enemy = nearbyUnits.get(k);
                UnitType enemyType = enemy.unitType();
                if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                    if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                        Direction opposite = unit.location().mapLocation().directionTo(enemy.location().mapLocation());
                        opposite = bc.bcDirectionOpposite(opposite);
                        for (int j = 0; j < allDirections.length; j++) {
                            if (opposite.equals(allDirections[j]) && gc.isMoveReady(id)) {
                                if (gc.canMove(id, allDirections[j])) {
                                    gc.moveRobot(id, allDirections[j]);
                                } else if (gc.canMove(id, allDirections[j + 1])) {
                                    gc.moveRobot(id, allDirections[j + 1]);
                                } else if (gc.canMove(id, allDirections[j - 1])) {
                                    gc.moveRobot(id, allDirections[j - 1]);
                                }
                            }
                        }
                    }
                }
            }
        } else if (getTag() == MARS_FARM_WORKER) {
            if (gc.planet().equals(Planet.Earth)) {
                if (!unit.location().isInGarrison()) {
                    ArrayList<BoogUnit> rockets = unitManager.getRockets();
                    BoogUnit minRocket = rockets.get(0);

                    long minDistance = rockets.get(0).getUnit().location().mapLocation().distanceSquaredTo(unit.location().mapLocation());
                    for (BoogUnit rocket : rockets) {
                        if (rocket.getUnit().structureGarrison().size() != (int) rocket.getUnit().structureMaxCapacity()) {
                            long distance = rocket.getUnit().location().mapLocation().distanceSquaredTo(unit.location().mapLocation());
                            if (distance < minDistance) {
                                minDistance = distance;
                                minRocket = rocket;
                            }
                        }

                    }
                    Direction direction = nextMoveTo(minRocket.getUnit().location().mapLocation());
                    if (gc.isMoveReady(id) && gc.canMove(id, direction)) {
                        gc.moveRobot(id, direction);
                    }
                }
            } else {
                if (gc.isMoveReady(id)) {
                    VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());
                    boolean found = false;
                    if (nearbyUnits.size() > 0) {
                        for (int k = 0; k < nearbyUnits.size(); k++) {
                            Unit enemy = nearbyUnits.get(k);
                            UnitType enemyType = enemy.unitType();
                            if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                                if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                                    found = true;
                                    Direction opposite = unit.location().mapLocation().directionTo(enemy.location().mapLocation());
                                    opposite = bc.bcDirectionOpposite(opposite);
                                    for (int j = 0; j < allDirections.length; j++) {
                                        if (opposite.equals(allDirections[j])) {
                                            if (gc.canMove(id, allDirections[j])) {
                                                gc.moveRobot(id, allDirections[j]);
                                            } else if (gc.canMove(id, allDirections[j + 1])) {
                                                gc.moveRobot(id, allDirections[j + 1]);
                                            } else if (gc.canMove(id, allDirections[j - 1])) {
                                                gc.moveRobot(id, allDirections[j - 1]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!found) {
                        if (grid.getTileAt(unit.location().mapLocation()).getKarbonite() == 0) {
                            long maxKarb = 0;
                            Direction maxDirection = allDirections[0];
                            for (Direction direction: allDirections) {
                                MapLocation newLocation = unit.location().mapLocation().add(direction);

                                    long karb = grid.getTileAt(unit.location().mapLocation()).getKarbonite();
                                    if (karb > maxKarb) {
                                        maxKarb = karb;
                                        maxDirection = direction;
                                    }

                            }
                            if (maxKarb == 0) {
                                for (Direction direction: allDirections) {
                                    MapLocation newLocation = unit.location().mapLocation().add(direction);
                                    MapLocation karbLocation = unit.location().mapLocation().add(direction).add(direction);
                                    if (gc.isOccupiable(newLocation) != 0) {
                                        long karb = grid.getTileAt(karbLocation).getKarbonite();
                                        if (karb > maxKarb) {
                                            maxKarb = karb;
                                            maxDirection = direction;
                                        }
                                    }
                                }
                                if (gc.canMove(id, maxDirection)) {
                                    gc.moveRobot(id, maxDirection);
                                }
                            }

                        }
                    }



                }
            }
        } else if (getTag() == EARTH_FARM_WORKER) {
            if (gc.isMoveReady(id)) {
                VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());
                boolean found = false;
                if (nearbyUnits.size() > 0) {
                    for (int k = 0; k < nearbyUnits.size(); k++) {
                        Unit enemy = nearbyUnits.get(k);
                        UnitType enemyType = enemy.unitType();
                        if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                            if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                                found = true;
                                Direction opposite = unit.location().mapLocation().directionTo(enemy.location().mapLocation());
                                opposite = bc.bcDirectionOpposite(opposite);
                                for (int j = 0; j < allDirections.length; j++) {
                                    if (opposite.equals(allDirections[j])) {
                                        if (gc.canMove(id, allDirections[j])) {
                                            gc.moveRobot(id, allDirections[j]);
                                        } else if (gc.canMove(id, allDirections[j + 1])) {
                                            gc.moveRobot(id, allDirections[j + 1]);
                                        } else if (gc.canMove(id, allDirections[j - 1])) {
                                            gc.moveRobot(id, allDirections[j - 1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!found) {
                    if (grid.getTileAt(unit.location().mapLocation()).getKarbonite() == 0) {
                        long maxKarb = 0;
                        Direction maxDirection = allDirections[0];
                        for (Direction direction: allDirections) {
                            MapLocation newLocation = unit.location().mapLocation().add(direction);

                                long karb = grid.getTileAt(unit.location().mapLocation()).getKarbonite();
                                if (karb > maxKarb) {
                                    maxKarb = karb;
                                    maxDirection = direction;
                                }

                        }
                        if (maxKarb == 0) {
                            for (Direction direction: allDirections) {
                                MapLocation newLocation = unit.location().mapLocation().add(direction);
                                MapLocation karbLocation = unit.location().mapLocation().add(direction).add(direction);
                                if (gc.isOccupiable(newLocation) != 0) {
                                    long karb = grid.getTileAt(karbLocation).getKarbonite();
                                    if (karb > maxKarb) {
                                        maxKarb = karb;
                                        maxDirection = direction;
                                    }
                                }
                            }
                            if (gc.canMove(id, maxDirection)) {
                                gc.moveRobot(id, maxDirection);
                            }
                        }

                    }
                }



            }
        }

    }

    /*
        decides if and where a unit should perform
        an action.
    */
    public void action(UnitManager unitManager) {
        int id = unit.id();
        Direction[] allDirections = Direction.values();
        Direction[] directions = new Direction[4];
        directions[0] = Direction.North;
        directions[1] = Direction.East;
        directions[2] = Direction.South;
        directions[3] = Direction.West;
        //if over 150 karbonite, build a factory
        if (getTag() == FACTORY_WORKER) {
            VecUnit nearbyUnits = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Factory);
            for (int k = 0; k < nearbyUnits.size(); k++) {
                Unit factory = nearbyUnits.get(k);
                if (gc.canBuild(id, factory.id())) {
                    gc.build(id, factory.id());
                } else if (factory.health() < factory.maxHealth() && gc.canRepair(id, factory.id())) {
                    gc.repair(id, factory.id());
                }
            }
            if (gc.karbonite() > 150) {
                for (Direction direction: directions) {
                    if (gc.canBlueprint(id, UnitType.Factory, direction) ) {
                        gc.blueprint(id, UnitType.Factory, direction);
                        MapLocation workerLocation = unit.location().mapLocation();
                        MapLocation factoryLocation = workerLocation.add(direction);
                        Unit newUnit = gc.senseUnitAtLocation(factoryLocation);
                        BoogUnit boog = new FactoryUnit(newUnit, gc, grid);
                        unitManager.add(boog);
                        unitManager.printList();
                        break;
                    }
                }
            }
        } else if (getTag() == ROCKET_WORKER) {
            VecUnit nearbyRocket = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Rocket);
            for (int k = 0; k < nearbyRocket.size(); k++) {
                Unit rocket = nearbyRocket.get(k);
                if (gc.canBuild(id, rocket.id())) {
                    gc.build(id, rocket.id());
                }
            }
            VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());
            for (int k = 0; k < nearbyUnits.size(); k++) {
                Unit enemy = nearbyUnits.get(k);
                boolean canBuild = true;
                UnitType enemyType = enemy.unitType();
                if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                    if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                        canBuild = false;
                    }
                }
                if (canBuild) {
                    for (Direction direction: directions) {
                        if (gc.canBlueprint(id, UnitType.Rocket, direction) ) {
                            gc.blueprint(id, UnitType.Rocket, direction);
                            MapLocation workerLocation = unit.location().mapLocation();
                            MapLocation rocketLocation = workerLocation.add(direction);
                            Unit newUnit = gc.senseUnitAtLocation(rocketLocation);
                            BoogUnit boog = new RocketUnit(newUnit, gc, grid);
                            unitManager.add(boog);
                            unitManager.printList();
                            break;
                        }
                    }
                }
            }
        } else if (getTag() == MARS_FARM_WORKER) {
            if (gc.planet() == Planet.Earth) {
                VecUnit nearbyRocket = gc.senseNearbyUnitsByType(unit.location().mapLocation(), 5, UnitType.Rocket);
                for (int k = 0; k < nearbyRocket.size(); k++) {
                    Unit rocket = nearbyRocket.get(k);
                    if (gc.canLoad(rocket.id(), id)) {
                        gc.load(rocket.id(), id);
                    }
                }
            } else {
                long maxKarb = 0;
                Direction maxDirection = allDirections[0];
                if (gc.karboniteAt(unit.location().mapLocation()) != 0) {
                    for (Direction direction: allDirections) {
                        MapLocation newLocation = unit.location().mapLocation().add(direction);

                        if (grid.isOnMap(newLocation)) {
                            long karb = grid.getTileAt(newLocation).getKarbonite();
                            if (karb > maxKarb) {
                                maxKarb = karb;
                                maxDirection = direction;
                            }
                        }

                    }
                }
                if (gc.canHarvest(id, maxDirection)) {
                    gc.harvest(id, maxDirection);
                }
            }
        } else if (getTag() == EARTH_FARM_WORKER) {
            long maxKarb = 0;
            Direction maxDirection = allDirections[0];
            if (gc.karboniteAt(unit.location().mapLocation()) != 0) {
                for (Direction direction: allDirections) {
                    MapLocation newLocation = unit.location().mapLocation().add(direction);

                    if (grid.isOnMap(newLocation)) {
                        long karb = grid.getTileAt(newLocation).getKarbonite();
                        if (karb > maxKarb) {
                            maxKarb = karb;
                            maxDirection = direction;
                        }
                    }

                }
            }
            if (gc.canHarvest(id, maxDirection)) {
                gc.harvest(id, maxDirection);
            }
        }
    }
}