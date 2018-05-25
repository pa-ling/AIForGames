package de.htw_berlin.ai_for_games.player;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.board.GawihsBoard.FieldState;
import lenz.htw.gawihs.Move;

public class GawihsPlayer {

    protected final FieldState playerNumber;

    private final GawihsBoard board;

    public GawihsPlayer(GawihsPlayer sourcePlayer) {
        this.playerNumber = sourcePlayer.playerNumber;
        this.board = new GawihsBoard(sourcePlayer.board);
    }

    public GawihsPlayer(int playerNumber, GawihsBoard board) {
        this.playerNumber = FieldState.values()[playerNumber];
        this.board = board;
    }

    public FieldState getPlayerNumber() {
        return this.playerNumber;
    }

    public int getPlayerNumberAsOrdinal() {
        return this.playerNumber.ordinal();
    }

    public Move move() {
        throw new UnsupportedOperationException("This method is not implemented here.");
    }
}
