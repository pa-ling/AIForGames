package de.htw_berlin.ai_for_games.player.strategies;

import java.util.concurrent.ThreadLocalRandom;

import de.htw_berlin.ai_for_games.player.AbstractMoveStrategy;
import de.htw_berlin.ai_for_games.player.MoveStrategy;
import lenz.htw.gawihs.Move;

/**
 * A {@link MoveStrategy} which computes all possible moves and chooses one of
 * them randomly.
 *
 */
public class RandomMoveStrategy extends AbstractMoveStrategy {

    @Override
    public Move getBestMove() {
        int size = getPossibleMoves().size();
        if (size == 0) {
            return null;
        }
        return getPossibleMoves().get(ThreadLocalRandom.current().nextInt(size));
    }

}
