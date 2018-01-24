import bc.*;

public class RangerUnit extends BoogUnit {

    private GameController gc;
    private Unit unit;
    private Grid grid;

    public RangerUnit(Unit unit, GameController gc, Grid grid) {
        super(unit, gc, grid);
    }

    /*
        decides whether or not the unit's tag
        needs to be changed.
    */
    public void adjustTag(UnitManager unitManager) {

    }

    /*
        decides what movement a unit should make.
    */
    public void move(UnitManager unitManager) {
        int id = unit.id();
        Direction[] allDirections = Direction.values();
        VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());

        for (int k = 0; k < nearbyUnits.size(); k++) {
            Unit enemy = nearbyUnits.get(k);
            UnitType enemyType = enemy.unitType();
            if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket)) {
                if (enemy.attackRange() >= unit.location().mapLocation().distanceSquaredTo(enemy.location().mapLocation())) {
                    if (gc.isMoveReady(id)) {
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
        if (gc.isMoveReady(id)) {
            Direction enemy = grid.getEnemyDirection();
            for (int j = 0; j < allDirections.length; j++) {
                if (enemy.equals(allDirections[j])) {
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

    /*
        decides if and where a unit should perform
        an action.
    */
    public void action(UnitManager unitManager) {
        int id = unit.id();
        VecUnit nearbyUnits = gc.senseNearbyUnitsByTeam(unit.location().mapLocation(), unit.visionRange(), getEnemy());
        if (nearbyUnits.size() != 0) {
            System.out.println("Found nearby unit");
            long minDistance = 10;
            int min = 0;
            int minNum = 100;
            long maxDistance = 0;
            int max = 0;
            for (int k = 0; k < nearbyUnits.size(); k++) {
                Unit enemyUnit = nearbyUnits.get(k);
                UnitType enemyType = enemyUnit.unitType();
                if (!enemyType.equals(UnitType.Worker) && !enemyType.equals(UnitType.Factory) && !enemyType.equals(UnitType.Rocket) && enemyUnit.location().mapLocation().
                    distanceSquaredTo(unit.location().mapLocation()) < enemyUnit.attackRange()) {
                    minDistance = enemyUnit.location().mapLocation().distanceSquaredTo(unit.location().mapLocation());
                    min = k;
                    minNum = 0;
                } else {
                    if (enemyUnit.location().mapLocation().
                            distanceSquaredTo(unit.location().mapLocation()) < unit.attackRange()) {
                        if (enemyType.equals(UnitType.Ranger) && minNum > 1) {
                            min = k;
                            minNum = 1;
                        } else if (minNum > 1) {
                            min = k;
                            minNum = 1;
                        }
                    }
                }
            }
            if (gc.isAttackReady(unit.id()) && gc.canAttack(unit.id(), nearbyUnits.get(min).id())) {
                gc.attack(unit.id(), nearbyUnits.get(min).id());
            }
        }
    }
}