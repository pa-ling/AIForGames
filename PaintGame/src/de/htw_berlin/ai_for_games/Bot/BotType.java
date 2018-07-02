package de.htw_berlin.ai_for_games.Bot;

public enum BotType {

    SPRAY_CAN(0),

    SMALL_BRUSH(1),

    LARGE_BRUSH(2);

    public final int number;

    private BotType(int number) {
        this.number = number;
    }

}
