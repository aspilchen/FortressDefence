package com.fortressdefence.FortressDefence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent a tank, including its location and shape.
 * Able to describe tank's health and damage done by using
 * the GameBoard to find out where the user has shot.
 */
public class Tank {
    private final GameBoard board;
    private CellLocation startCell;
    private final Polyomino shape = new Polyomino();
    private final int tankNumber;


    // Exception to return when placing tank on board fails
    public static class UnableToCreateTankException extends RuntimeException {
    }

    // Game designed to have damage fall off very quickly.
    private final static int[] DAMAGE_DONE_PER_UNDAMAGED_CELLS = {0, 1, 2, 5, 20, 20};

    public Tank(GameBoard board, int tankNumber) {
        this.board = board;
        this.tankNumber = tankNumber;
        placeOnBoard();
    }

    private void placeOnBoard() {
        List<CellLocation> positions = getAllPossibleLocations();
        for (CellLocation position : positions) {
            if (fitsOnBoardAtPosition(position)) {
                placeOnBoardAtPosition(position);
                return;
            }
        }
        throw new UnableToCreateTankException();
    }

    private List<CellLocation> getAllPossibleLocations() {
        List<CellLocation> list = new ArrayList<>();
        for (int row = 0; row < GameBoard.NUMBER_ROWS; row++) {
            for (int col = 0; col < GameBoard.NUMBER_COLS; col++) {
                list.add(new CellLocation(row, col));
            }
        }
        Collections.shuffle(list);
        return list;
    }

    private boolean fitsOnBoardAtPosition(CellLocation position) {
        for (CellLocation shapeCell : shape.getCellLocations()) {

            CellLocation realCell = position.add(shapeCell);

            if (!board.cellOpenForTank(realCell)) {
                return false;
            }
        }
        return true;
    }

    private void placeOnBoardAtPosition(CellLocation position) {
        startCell = position;
        for (CellLocation shapeCell : shape.getCellLocations()) {

            CellLocation realCell = position.add(shapeCell);

            board.recordTankInCell(realCell, tankNumber);
        }
    }

    public int getUndamagedCellCount() {
        int count = 0;
        for (CellLocation shapeCell : shape.getCellLocations()) {
            CellLocation realCell = startCell.add(shapeCell);
            if (!board.hasCellBeenShot(realCell)) {
                count++;
            }
        }
        return count;
    }

    public int getShotDamage() {
        return DAMAGE_DONE_PER_UNDAMAGED_CELLS[getUndamagedCellCount()];
    }

    public boolean isDestroyed() {
        return getUndamagedCellCount() == 0;
    }
}
