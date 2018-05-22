/**
 *
 */
package de.htw_berlin.ai_for_games.player;

import java.util.List;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

/**
 * Interface for all move strategies.
 *
 */
public interface MoveStrategy {

    /**
     * Returns the best move possible according to the strategy implemented.
     *
     * @return a {@link Move} or {@code null} if no move is possible anymore
     */
    public Move getBestMove();

    /**
     * Returns a list of all moves which are possible according to the strategy
     * implemented.
     *
     * @return a {@link List} of {@link Move Moves} or an empty list if no move is
     *         possible anymore
     */
    public List<Move> getPossibleMoves();

    /**
     * Set the board used for this strategy.
     *
     * @param board
     *            board to use
     */
    public void setBoard(GawihsBoard board);

    public void setEnemies(List<GawihsPlayer> enemies);

    /**
     * Set the player on which the strategy shall be used.
     *
     * @param player
     *            player to use
     */
    public void setPlayer(GawihsPlayer player);

}
