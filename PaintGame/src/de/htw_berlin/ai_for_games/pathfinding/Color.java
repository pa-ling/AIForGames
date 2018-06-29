package de.htw_berlin.ai_for_games.pathfinding;

public enum Color {

    // TODO correct color player numbers and intValues
    WHITE(-1, 16777215), //
    BLACK(-1, 0), //
    RED(0, 0), //
    GREEN(1, 0), //
    BLUE(2, 0);

    public final int playerNumber;
    public final int intValue;

    private Color(int playerNumber, int intValue) {
        this.playerNumber = playerNumber;
        this.intValue = intValue;
    }

}
