package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.board.Field;
import lenz.htw.gawihs.Move;

/**
 * A {@link MoveStrategy} which creates one valid move. All subsequent moves are
 * invalid.
 *
 */
public class FailAfterFirstMoveStrategy extends AbstractMoveStrategy {

    @Override
    public Move getBestMove() {
        return getPossibleMoves().get(0);
    }

    @Override
    public List<Move> getPossibleMoves() {
        Field firstStone = this.player.getPlayerStonePositions().get(0);
        Field targetField = new Field(firstStone.x, firstStone.y);

        switch (this.player.getPlayerNumber()) {
            case PLAYER_0:
                targetField.y += 1;
                break;
            case PLAYER_1:
                targetField.x += 1;
                break;
            case PLAYER_2:
                targetField.x -= 1;
            default:
                break;

        }

        List<Move> possibleMoves = new ArrayList<>();
        possibleMoves.add(new Move(firstStone.x, firstStone.y, targetField.x, targetField.y));
        return possibleMoves;
    }

}
