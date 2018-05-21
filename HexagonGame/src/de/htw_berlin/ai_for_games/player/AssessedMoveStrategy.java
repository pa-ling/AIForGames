package de.htw_berlin.ai_for_games.player;

import java.util.List;

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

}
