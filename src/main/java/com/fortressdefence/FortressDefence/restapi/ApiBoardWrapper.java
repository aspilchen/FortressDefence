package com.fortressdefence.FortressDefence.restapi;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fortressdefence.FortressDefence.model.CellLocation;
import com.fortressdefence.FortressDefence.model.CellState;
import com.fortressdefence.FortressDefence.model.Game;
import com.fortressdefence.FortressDefence.model.GameBoard;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public String[][] cellStates;

    public ApiBoardWrapper() {
        cellStates = new String[GameBoard.NUMBER_ROWS][GameBoard.NUMBER_COLS];
    }

    public static ApiBoardWrapper fromGame(Game game, boolean cheat) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.boardHeight = GameBoard.NUMBER_COLS;
        wrapper.boardWidth = GameBoard.NUMBER_ROWS;
        for (int row = 0; row < wrapper.boardHeight; row++) {
            for (int col = 0; col < wrapper.boardWidth; col++) {
                CellState state = game.getCellState(new CellLocation(row, col));
                wrapper.cellStates[row][col] = cellStateToString(state, cheat);
            }
        }
        return wrapper;
    }

    public static String cellStateToString(CellState state, boolean cheat) {
        if(state.hasBeenShot() && state.hasTank()) {
            return "hit";
        } else if(state.hasBeenShot() && !state.hasTank()){
            return "miss";
        } else if(cheat && !state.hasBeenShot() && !state.hasTank()) {
            return "field";
        } else if(cheat && !state.hasBeenShot() && state.hasTank()) {
            return "tank";
        } else {
            return "fog";
        }
    }
}