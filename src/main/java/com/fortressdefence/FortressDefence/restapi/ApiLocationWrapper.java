package com.fortressdefence.FortressDefence.restapi;

import com.fortressdefence.FortressDefence.model.CellLocation;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiLocationWrapper {
    public int row;
    public int col;

    public ApiLocationWrapper() {}

    public CellLocation toCellLocation() {
        return new CellLocation(row, col);
    }
}
