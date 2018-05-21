package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
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

    @Override
    public List<Move> getPossibleMoves() {
        Set<Field> targetFields = new HashSet<>();

        List<Field> availablePlayerStones = new ArrayList<>();

        // get possible target fields and remove unavailable player stones
        for (Field playerStone : this.player.getPlayerStonePositions()) {
            if (!this.board.isPlayerOnTopOfField(playerStone, this.player)) {
                continue;
            }
            availablePlayerStones.add(playerStone);
            targetFields.addAll(this.board.getAvailableFieldsForPlayerAround(playerStone, this.player));
        }

        // compute possible moves
        List<Move> possibleMoves = new ArrayList<>();
        for (Field stoneToMove : availablePlayerStones) {
            for (Field targetField : targetFields) {
                for (Field fieldAroundTarget : GawihsBoard.getFieldsAround(targetField)) {
                    if (this.player.getPlayerStonePositions().contains(fieldAroundTarget)
                            && !stoneToMove.equals(fieldAroundTarget)) {
                        possibleMoves.add(new Move(stoneToMove.x, stoneToMove.y, targetField.x, targetField.y));
                    }
                }
            }
        }

        return possibleMoves;
    }

}
