package de.htw_berlin.ai_for_games.player.strategies;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.AbstractMoveStrategy;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import lenz.htw.gawihs.Move;

/**
 * Chooses the best possible move by calculating all possible boards, assessing
 * them and choosing the move which lead to the board with the best score.
 *
 * @author paling
 *
 */
public class AssessedMoveStrategy extends AbstractMoveStrategy {
    private class AssessmentConfig {
        public int multiplier;
    }

    private AssessmentConfig config;

    public AssessedMoveStrategy() {
        try (Reader reader = new FileReader(new File("res/config.json"))) {
            Gson gson = new GsonBuilder().create();
            this.config = gson.fromJson(reader, AssessmentConfig.class);
        } catch (IOException e) {
            this.config = new AssessmentConfig();
            e.printStackTrace();
        }
        System.out.println(this.config.multiplier);
    }

    private int assessBoard(GawihsBoard board, GawihsPlayer player) {
        // check for win
        // check for loose
        int score = 0;

        score += this.enemies.size() // Anzahl der Gegner
                + board.getUnoccupiedFieldsCount() // Anzahl der noch leeren Felder
                + player.getAvailablePlayerStonePositions().size()// Anzahl blockierter eigener Steine
                + getPossibleMoves(board, player).size(); // mögliche Züge

        for (GawihsPlayer enemy : this.enemies) {
            score -= enemy.getAvailablePlayerStonePositions().size(); // Anzahl bewegbarer gegnerischer Steine
            score -= getPossibleMoves(board, enemy).size(); // mögliche Züge der Gegner
        }

        return score;
    }

    @Override
    public Move getBestMove() {
        Move bestMove = null;
        int bestAssessment = Integer.MIN_VALUE;
        for (Move possibleMove : getPossibleMoves()) {
            final GawihsBoard boardWithAppliedMove = new GawihsBoard(this.board);
            boardWithAppliedMove.applyMove(possibleMove);

            final GawihsPlayer playerWithAppliedMove = new GawihsPlayer(this.player);
            playerWithAppliedMove.applyMove(possibleMove);

            int assessment = assessBoard(boardWithAppliedMove, playerWithAppliedMove);
            if (assessment > bestAssessment) {
                bestMove = possibleMove;
                bestAssessment = assessment;
            }
        }

        return bestMove;
    }

}
