package com.fortressdefence.FortressDefence.model;

/**
 * Manage the health (structural health) of the fortress.
 */
public class Fortress {
    public static final int INITIAL_HEALTH = 2500;

    private int health = INITIAL_HEALTH;

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
}
