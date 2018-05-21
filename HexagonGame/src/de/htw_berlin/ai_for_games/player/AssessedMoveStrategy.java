package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

public class AssessedMoveStrategy extends AbstractMoveStrategy {

    private int assessBoard(GawihsBoard board) {
        // Anzahl der Gegner?
        // Anzahl der noch leeren Felder?
        // Anzahl blockierter eigener Steine?
        // Anzahl blockierter gegnerischer Steine?
        // mögliche Züge?
        // mögliche Züge der Gegner?
        return 0;
    }

    @Override
    public Move getBestMove() {
        List<Move> possibleMoves = getPossibleMoves();
        if (possibleMoves.size() == 0) {
            return null;
        }

        Move bestMove = null;
        int bestAssessment = Integer.MIN_VALUE;
        for (Move possibleMove : possibleMoves) {
            GawihsBoard boardWithAppliedMove = new GawihsBoard(this.board);
            boardWithAppliedMove.applyMove(possibleMove);
            int assessment = assessBoard(boardWithAppliedMove);
            if (assessment > bestAssessment) {
                bestMove = possibleMove;
                bestAssessment = assessment;
            }
        }

        return bestMove;
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
