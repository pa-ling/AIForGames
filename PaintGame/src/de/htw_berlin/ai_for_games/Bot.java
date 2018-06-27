package de.htw_berlin.ai_for_games;

public enum Bot {

    SPRAYCAN(0, 1),

    SMALL_BRUSH(1, 2),

    LARGE_BRUSH(2, 5);

    public final int number;

    public final int graphModifier;

    private Bot(int number, int graphModifier) {
        this.number = number;
        this.graphModifier = graphModifier;
    }

}
