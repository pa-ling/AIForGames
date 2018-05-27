package de.htw_berlin.ai_for_games.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsAIPlayer;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.strategies.ThreadedAlphaBetaPruningStrategy;

class AlphaBetaPlayerRemoveTest {

    static GawihsBoard board;
    static GawihsAIPlayer player1;
    static GawihsPlayer player2;
    static GawihsPlayer player3;

    @BeforeAll
    static void setup() {
        board = new GawihsBoard();
        player1 = new GawihsAIPlayer(0, new ThreadedAlphaBetaPruningStrategy("res/configP.json", 5), board);
        player2 = new GawihsPlayer(1, board);
        player3 = new GawihsPlayer(2, board);
        List<GawihsPlayer> enemies = new ArrayList<>();
        enemies.add(player2);
        enemies.add(player3);
        player1.setEnemies(enemies);
    }

    @Test
    void test() {
        System.out.println(player1.move());
        player1.removeEnemy(player2);
        System.out.println(player1.move());

    }

}
