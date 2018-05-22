package de.htw_berlin.ai_for_games.player.strategies;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.AbstractMoveStrategy;
import de.htw_berlin.ai_for_games.player.AssessmentConfig;
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

    private AssessmentConfig config;

    public AssessedMoveStrategy() {
        File configFile = new File("res/config.json");
        Reader configReader;
        try {
            configReader = new FileReader(configFile);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            this.config = gson.fromJson(configReader, AssessmentConfig.class);
        } catch (FileNotFoundException e) {
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
                + board.unoccupiedFieldsCount() // Anzahl der noch leeren Felder
                + player.getAvailablePlayerStonePositions().size()// Anzahl blockierter eigener Steine
                + getPossibleMoves(board, player).size(); // mögliche Züge
        // Anzahl blockierter gegnerischer Steine?
        // mögliche Züge der Gegner?
        return score;
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
            GawihsPlayer playerWithAppliedMove = new GawihsPlayer(this.player);
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
