package de.htw_berlin.ai_for_games.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import lenz.htw.gawihs.Move;

class PlayerPerformanceTest {

    static GawihsBoard board;
    static GawihsPlayer player1;

    @BeforeAll
    static void setup() {
        board = new GawihsBoard();
        player1 = new GawihsPlayer(1, board);
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
