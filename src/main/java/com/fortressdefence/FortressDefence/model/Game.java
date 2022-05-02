package com.fortressdefence.FortressDefence.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the game Fortress Defense game state.
 */
public class Game {

    private final Fortress fortress = new Fortress();
    private final GameBoard board = new GameBoard();
    private final List<Tank> tanks = new ArrayList<>();

    private List<Integer> latestTankDamages;
    private boolean lastPlayerShotHit;

    public Game(int numberTanks) {
        for (int i = 0; i < numberTanks; i++) {
            int tankNumber = i + 1;
            tanks.add(new Tank(board, tankNumber));
        }
    }


    public boolean hasUserWon() {
        return tanks.stream().allMatch(Tank::isDestroyed);
        // Code doing same thing as the stream
//        for (Tank tank : tanks) {
//            if (!tank.isDestroyed()) {
//                return false;
//            }
//        }
//        return true;
    }

    public boolean hasUserLost() {
        return getFortressHealth() == 0;
    }


    public int getFortressHealth() {
        return fortress.getHealth();
    }

    public void recordPlayerShot(CellLocation cell) {
        board.recordUserShot(cell);
        lastPlayerShotHit = board.cellContainsTank(cell);
    }

    public boolean didLastPlayerShotHit() {
        return lastPlayerShotHit;
    }

    public CellState getCellState(CellLocation cell) {
        return board.getCellState(cell);
    }

    public void fireTanks() {
        latestTankDamages = new ArrayList<>();
        for (Tank tank : tanks) {
            int damage = tank.getShotDamage();
            if (damage > 0) {
                fortress.takeDamage(damage);
                latestTankDamages.add(damage);
            }
        }
    }

    public int getLiveTankCount() {
        return (int) tanks.stream().filter(tank -> !tank.isDestroyed()).count();
    }

    public int[] getLatestTankDamages() {
        if (latestTankDamages == null) {
            return new int[0];
        }

        int[] damages = new int[latestTankDamages.size()];
        for (int i = 0; i < latestTankDamages.size(); i++) {
            damages[i] = latestTankDamages.get(i);
        }
        return damages;
    }
}
