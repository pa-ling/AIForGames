package de.htw_berlin.ai_for_games.player.strategies;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.player.AbstractMoveStrategy;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.MoveStrategy;
import lenz.htw.gawihs.Move;

/**
 * A {@link MoveStrategy} which does not return any move and therefore will make
 * any {@link GawihsPlayer} using it fail.
 *
 */
public class FailAlwaysMoveStrategy extends AbstractMoveStrategy {

    @Override
    public Move getBestMove() {
        return null;
    }

    @Override
    public List<Move> getPossibleMoves() {
        return new ArrayList<>();
    }

}
