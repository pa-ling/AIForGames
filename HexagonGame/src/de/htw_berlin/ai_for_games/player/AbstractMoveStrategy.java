package de.htw_berlin.ai_for_games.player;

import de.htw_berlin.ai_for_games.board.GawihsBoard;

public abstract class AbstractMoveStrategy implements MoveStrategy {

    protected GawihsBoard board;
    protected GawihsPlayer player;

    @Override
    public void setBoard(GawihsBoard board) {
        this.board = board;
    }

    @Override
    public void setPlayer(GawihsPlayer player) {
        this.player = player;
    }

}