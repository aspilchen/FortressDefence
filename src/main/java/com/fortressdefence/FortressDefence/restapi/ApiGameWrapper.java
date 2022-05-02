package com.fortressdefence.FortressDefence.restapi;

import com.fortressdefence.FortressDefence.model.Game;

/**
 * Wrapper class for the REST API to define object structures required by the front-end.
 * HINT: Create static factory methods (or constructors) which help create this object
 *       from the data stored in the model, or required by the model.
 */
public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int fortressHealth;
    public int numTanksAlive;

    // Amount of damage that the tanks did on the last time they fired.
    // If tanks have not yet fired, then it should be an empty array (0 size).
    public int[] lastTankDamages;

    public ApiGameWrapper() {}

    public static ApiGameWrapper fromGame(Game game, int gameNumber) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = gameNumber;
        wrapper.isGameWon = game.hasUserWon();
        wrapper.isGameLost = game.hasUserLost();
        wrapper.fortressHealth = game.getFortressHealth();
        wrapper.numTanksAlive = game.getLiveTankCount();
        wrapper.lastTankDamages = game.getLatestTankDamages();
        return wrapper;
    }
}
