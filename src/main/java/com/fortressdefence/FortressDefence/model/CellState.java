package com.fortressdefence.FortressDefence.model;

/**
 * Represent the state of a game-board cell.
 * An immutable class.
 */
public class CellState {
    private boolean hasBeenShot;
    private int tankNumberAtCell;

    public CellState(boolean isShot, int tankNumberAtCell) {
        this.hasBeenShot = isShot;
        this.tankNumberAtCell = tankNumberAtCell;
    }

    public boolean hasTank() {
        return tankNumberAtCell != 0;
    }

    public boolean hasBeenShot() {
        return hasBeenShot;
    }

    public boolean isHidden() {
        return !hasBeenShot;
    }

    // Create new instance based on current state (Immutable)
    public CellState makeHasBeenShot() {
        return new CellState(true, tankNumberAtCell);
    }

    public CellState makeContainTank(int tankNumber) {
        return new CellState(hasBeenShot, tankNumber);
    }

    public int getTankNumberAtCell() {
        return tankNumberAtCell;
    }
}
