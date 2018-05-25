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
            // this.config = new AssessmentConfig();
            System.err.println("There was a problem loading the config file at '" + configPath + "'");
            e.printStackTrace();
        }
        System.out.println("Assessment strategy loaded from '" + configPath + "' with the following multipliers:"
                + "\nunoccupiedFields: " + this.config.getUnoccupiedFieldsMultiplier() + "\nplayerStones: "
                + this.config.getPlayerStonesMultiplier() + "\npossibleMoves: "
                + this.config.getPossibleMovesMultiplier() + "\nenemyCount: " + this.config.getEnemyCountMultiplier()
                + "\nenemyStonesMultiplier: " + this.config.getEnemyStonesMultiplier() + "\nenemyPossibleMoves: "
                + this.config.getEnemyPossibleMovesMultiplier());
    }

    protected long assessBoard(GawihsBoard board, GawihsPlayer player, List<GawihsPlayer> enemies) {
        if (enemies.isEmpty()) {
            return Long.MAX_VALUE; // win
        }

        int possibleMoves = getPossibleMoves(board, player).size();
        if (possibleMoves == 0) {
            return Long.MIN_VALUE; // loose
        }

        long score = 0;
        // + empty fields + available player stones + possible moves
        score += this.config.getUnoccupiedFieldsMultiplier() * board.getUnoccupiedFieldsCount()
                + this.config.getPlayerStonesMultiplier() * board.getAvailablePlayerStones(player).size()
                + this.config.getPossibleMovesMultiplier() * possibleMoves;

        // - enemies - enemy stones - possible moves of enemies
        for (GawihsPlayer enemy : enemies) {
            score -= this.config.getEnemyCountMultiplier() * enemies.size()
                    - this.config.getEnemyStonesMultiplier() * board.getAvailablePlayerStones(enemy).size()
                    - this.config.getEnemyPossibleMovesMultiplier() * getPossibleMoves(board, enemy).size();
        }

        return score;
    }

    @Override
    public Move getBestMove() {
        Move bestMove = null;
        long bestAssessment = Long.MIN_VALUE;
        for (Move possibleMove : getPossibleMoves()) {
            final GawihsBoard boardWithAppliedMove = new GawihsBoard(this.board);
            boardWithAppliedMove.applyMove(this.player, possibleMove);

            long assessment = assessBoard(boardWithAppliedMove, this.player, this.enemies);
            if (assessment > bestAssessment || bestMove == null) {
                bestMove = possibleMove;
                bestAssessment = assessment;
            }
        }

        return bestMove;
    }

}
