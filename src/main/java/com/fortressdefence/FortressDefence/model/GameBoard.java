package com.fortressdefence.FortressDefence.model;

/**
 * Manage the game board, tracking which cells are occupied by tanks, and
 * which cells have been hit by the user's shots.
 */
public class GameBoard {
    public static final int NUMBER_ROWS = 10;
    public static final int NUMBER_COLS = 10;

    private CellState[][] board = new CellState[NUMBER_ROWS][NUMBER_COLS];

    public GameBoard() {
        for (int row = 0; row < NUMBER_ROWS; row++) {
            for (int col = 0; col < NUMBER_COLS; col++) {
                board[row][col] = new CellState(false, 0);
            }
        }
    }

    public CellState getCellState(CellLocation cell) {
        int row = cell.getRowIndex();
        int col = cell.getColIndex();
        return board[row][col];
    }

    public boolean hasCellBeenShot(CellLocation cell) {
        int row = cell.getRowIndex();
        int col = cell.getColIndex();
        return board[row][col].hasBeenShot();
    }

    public boolean cellContainsTank(CellLocation cell) {
        int row = cell.getRowIndex();
        int col = cell.getColIndex();
        return board[row][col].hasTank();
    }

    public void recordUserShot(CellLocation pos) {
        CellState current = board[pos.getRowIndex()][pos.getColIndex()];
        board[pos.getRowIndex()][pos.getColIndex()] = current.makeHasBeenShot();

    }

    public boolean cellOpenForTank(CellLocation cell) {
        int row = cell.getRowIndex();
        int col = cell.getColIndex();
        // Row out of bounds?
        if (row < 0 || row >= NUMBER_ROWS) {
            return false;
        }
        // Column out of bounds?
        if (col < 0 || col >= NUMBER_COLS) {
            return false;
        }
        // Has tank?
        return !cellContainsTank(cell);
    }

    public void recordTankInCell(CellLocation cell, int tankNumberAtCell) {
        assert cellOpenForTank(cell);

        int row = cell.getRowIndex();
        int col = cell.getColIndex();
        board[row][col] = board[row][col].makeContainTank(tankNumberAtCell);
    }
}
