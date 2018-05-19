package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

/**
 * A {@link MoveStrategy} which does not return any move and therefore will make
 * any {@link GawihsPlayer} using it fail.
 *
 */
public class FailAlwaysMoveStrategy implements MoveStrategy {

    @Override
    public Move getBestMove() {
        return null;
    }

    @Override
    public List<Move> getPossibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public void setBoard(GawihsBoard board) {
        // not needed for this strategy
    }

    @Override
    public void setPlayer(GawihsPlayer player) {
        // not needed for this strategy
    }

}
