package de.htw_berlin.ai_for_games.player.strategies;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

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

    private AssessmentConfig config;

    public AssessedMoveStrategy(String configPath) {
        try (Reader reader = new FileReader(new File(configPath))) {
            Gson gson = new GsonBuilder().create();
            this.config = gson.fromJson(reader, AssessmentConfig.class);
        } catch (IOException e) {
            this.config = new AssessmentConfig();
            System.err.println("There was a problem loading the config file at '" + configPath + "'");
            e.printStackTrace();
        }
        System.out.println("Assessment strategy loaded with the following multipliers:" + "\nunoccupiedFields: "
                + this.config.unoccupiedFieldsMultiplier + "\nplayerStones: " + this.config.playerStonesMultiplier
                + "\npossibleMoves: " + this.config.possibleMovesMultiplier + "\nenemyCount: "
                + this.config.enemyCountMultiplier + "\nenemyStonesMultiplier: " + this.config.enemyStonesMultiplier
                + "\nenemyPossibleMoves: " + this.config.enemyPossibleMovesMultiplier);
    }

    private int assessBoard(GawihsBoard board, GawihsPlayer player, List<GawihsPlayer> enemies) {
        if (enemies.isEmpty()) {
            return Integer.MAX_VALUE; // win
        }

        int possibleMoves = getPossibleMoves(board, player).size();
        if (possibleMoves == 0) {
            return Integer.MIN_VALUE; // loose
        }

        int score = 0;
        // + empty fields + available player stones + possible moves
        score += this.config.unoccupiedFieldsMultiplier * board.getUnoccupiedFieldsCount()
                + this.config.playerStonesMultiplier * player.getAvailablePlayerStonePositions().size()
                + this.config.possibleMovesMultiplier * possibleMoves;

        // - enemies - enemy stones - possible moves of enemies
        for (GawihsPlayer enemy : enemies) {
            score -= this.config.enemyCountMultiplier * enemies.size()
                    - this.config.enemyStonesMultiplier * enemy.getAvailablePlayerStonePositions().size()
                    - this.config.enemyPossibleMovesMultiplier * getPossibleMoves(board, enemy).size();
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

            int assessment = assessBoard(boardWithAppliedMove, playerWithAppliedMove, this.enemies);
            if (assessment > bestAssessment) {
                bestMove = possibleMove;
                bestAssessment = assessment;
            }
        }

        return bestMove;
    }

}
