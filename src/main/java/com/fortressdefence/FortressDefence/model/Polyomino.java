package com.fortressdefence.FortressDefence.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An N-cell connected shape (a polyomino). Handles creation and
 * access to the cells the shape covers. Starts at a relative (0,0), and grows
 * randomly from there (+ or -).
 */
public class Polyomino {
    public static final int NUM_CELLS = 5;
    private static final int NUM_DIRECTIONS = 4;

    private final List<CellLocation> cells = new ArrayList<>();

    public Polyomino() {
        cells.add(new CellLocation(0, 0));
        for (int i = 1; i < NUM_CELLS; i++) {
            growPolyomino();
        }
    }

    private void growPolyomino() {
        // Pick random cell to grow from
        List<Integer> cellsToGrowFrom = generatePermutationOf0ToNMinus1(cells.size());
        for (int cellGrowFormIdx : cellsToGrowFrom) {
            int growFromRow = cells.get(cellGrowFormIdx).getRowIndex();
            int growFromCol = cells.get(cellGrowFormIdx).getColIndex();

            // Pick a random direction:
            List<Integer> directions = generatePermutationOf0ToNMinus1(NUM_DIRECTIONS);
            for (int direction : directions) {
                int newRow = growFromRow;
                int newCol = growFromCol;

                // Numbers 0-3 have no meaning, they are just random possibilities.
                switch (direction) {
                    case 0: newRow++; break;
                    case 1: newRow--; break;
                    case 2: newCol++; break;
                    case 3: newCol--; break;
                    default:
                        assert false;
                }

                CellLocation newLoc = new CellLocation(newRow, newCol);
                if (!cells.contains(newLoc)) {
                    cells.add(newLoc);
                    return;
                }
            }
        }
        assert false;
    }

    private List<Integer> generatePermutationOf0ToNMinus1(int n) {
        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            permutation.add(i);
        }

        Collections.shuffle(permutation);
        return permutation;
    }

    public CellLocation[] getCellLocations() {
        CellLocation[] array = new CellLocation[cells.size()];
        cells.toArray(array);
        return array;
    }
}