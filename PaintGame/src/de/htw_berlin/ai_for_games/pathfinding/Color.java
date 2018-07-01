package de.htw_berlin.ai_for_games.pathfinding;

public enum Color {

    WHITE(-1, 0x00FFFFFF), BLACK(-1, 0x00000000), RED(0, 0x00FF0000), GREEN(1, 0x0000FF00), BLUE(2, 0x000000FF);

    public final int playerNumber;
    public final int intValue;

    private Color(int playerNumber, int intValue) {
        this.playerNumber = playerNumber;
        this.intValue = intValue;
    }

}
