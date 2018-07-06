package de.htw_berlin.ai_for_games.Bot;

public enum BotType {

    SPRAY_CAN(0, 2.1f),

    SMALL_BRUSH(1, 1.0f),

    LARGE_BRUSH(2, 0.68f);

    public final int number;
    public final float speed;

    private BotType(int number, float speed) {
        this.number = number;
        this.speed = speed;
    }

}
