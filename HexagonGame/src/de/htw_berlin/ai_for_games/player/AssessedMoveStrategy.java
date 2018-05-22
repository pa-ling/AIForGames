package de.htw_berlin.ai_for_games.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

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

    private int assessBoard(GawihsBoard board) {

        // check for win
        // check for loose
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
