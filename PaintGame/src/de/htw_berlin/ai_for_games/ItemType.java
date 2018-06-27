package de.htw_berlin.ai_for_games;

import lenz.htw.zpifub.PowerupType;

public enum ItemType {

    BOMB(PowerupType.a, true), //
    RAIN(PowerupType.b, true), //
    CLOCK(PowerupType.c, false);

    public final PowerupType powerUpType;
    public final boolean isGoodItem;

    private ItemType(PowerupType puType, boolean isGoodItem) {
        this.powerUpType = puType;
        this.isGoodItem = isGoodItem;
    }

}
