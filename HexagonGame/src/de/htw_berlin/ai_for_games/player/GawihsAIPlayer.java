package de.htw_berlin.ai_for_games.player;

import java.util.List;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

public class GawihsAIPlayer extends GawihsPlayer {

    private List<GawihsPlayer> enemies;
    private final MoveStrategy moveStrategy;

    public GawihsAIPlayer(int playerNumber, MoveStrategy moveStrategy, GawihsBoard board) {
        super(playerNumber, board);

        // set up move strategy
        this.moveStrategy = moveStrategy;
        this.moveStrategy.setBoard(board);
        this.moveStrategy.setPlayer(this);
    }

    @Override
    public Move move() {
        final Move move = this.moveStrategy.getBestMove();
        if (move == null) {
            throw new IllegalStateException(this.playerNumber + ": No moves are possible anymore!");
        }

        return move;
    }

    public void removeEnemy(GawihsPlayer enemy) {
        this.enemies.remove(enemy);
    }

    public void setEnemies(List<GawihsPlayer> enemies) {
        this.enemies = enemies;
        this.moveStrategy.setEnemies(this.enemies);
    }

}
