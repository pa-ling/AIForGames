package de.htw_berlin.ai_for_games.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsAIPlayer;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.strategies.AlphaBetaPruningStrategy;
import lenz.htw.gawihs.Move;

class PlayerPerformanceTest {

    static GawihsBoard board;
    static GawihsAIPlayer player1;
    static GawihsPlayer player2;
    static GawihsPlayer player3;

    @BeforeAll
    static void setup() {
        board = new GawihsBoard();
        player1 = new GawihsAIPlayer(0, new AlphaBetaPruningStrategy("HexagonGame/res/configP.json"), board);
        player2 = new GawihsPlayer(1, board);
        player3 = new GawihsPlayer(2, board);
        List<GawihsPlayer> enemies = new ArrayList<>();
        enemies.add(player2);
        enemies.add(player3);
        player1.setEnemies(enemies);
    }

    @Test
    void test() {
        long computationTime = System.nanoTime();
        Move move = player1.move();
        computationTime = System.nanoTime() - computationTime;

        if (computationTime > 8000000000L) {
            fail("You're too slow!");
        }

        System.out.println("Computation took: " + computationTime + " ms.");
        System.out.println(
                "Move generated: (" + move.fromX + "," + move.fromY + ") -> (" + move.toX + "," + move.toY + ")");
    }

}
