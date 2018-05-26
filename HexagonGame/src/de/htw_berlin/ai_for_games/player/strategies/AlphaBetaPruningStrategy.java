package de.htw_berlin.ai_for_games.player.strategies;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.board.GawihsBoard.FieldState;
import lenz.htw.gawihs.Move;

public class AlphaBetaPruningStrategy extends AssessedMoveStrategy {
    private class GameTreeNode {
        // TODO remove
        /** Nummer des Spielers, für den der Zug gefunden werden soll. */
        public int ourPlayerNumber;

        /**
         * Nummer des Spieler, der in diesem Node die Züge generiert (aka der dran ist).
         */
        public int currentPlayerNumber;

        /** Das Board, nachdem der Move des Zuges darauf angewendet wurde. */
        public GawihsBoard boardState;

    }

    public AlphaBetaPruningStrategy(String configPath) {
        super(configPath);
    }

    /**
     * Bewertet den übergebenen Node. Basiert auf:
     * https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning#Pseudocode
     */
    private long alphabeta(GameTreeNode currentNode, int currentDepth, long alpha, long beta,
            boolean isMaximizingPlayer) {
        if (currentDepth == 0) {
            // this is a terminal node - assess it
            // TODO get state of current enemies
            return assessBoard(currentNode.boardState, this.player, this.enemies);
        }

        List<Move> possibleMoves = getPossibleMoves(currentNode.boardState, currentNode.currentPlayerNumber);

        if (possibleMoves.size() == 0) {
            // this is a node where one player dies
            if (currentNode.currentPlayerNumber == currentNode.ourPlayerNumber) {
                // we die - assess lowest possible rating
                return Long.MIN_VALUE;
            }
            // someone else dies - assess high rating
            return Long.MAX_VALUE;

        }

        if (isMaximizingPlayer) {
            long v = Long.MIN_VALUE;
            for (Move possibleMove : possibleMoves) {
                GameTreeNode child = generateChild(currentNode, possibleMove);
                v = Math.max(v, alphabeta(child, currentDepth - 1, alpha, beta, false));
                v = Math.max(alpha, v);
                if (beta <= alpha) {
                    // beta cut-off
                    break;
                }
            }
            return v;
        }
        // it's a minimizing player
        long v = Long.MAX_VALUE;
        for (Move possibleMove : possibleMoves) {
            GameTreeNode child = generateChild(currentNode, possibleMove);
            // a player is maximizing if it is our player
            v = Math.min(v, alphabeta(child, currentDepth - 1, alpha, beta,
                    child.currentPlayerNumber == currentNode.ourPlayerNumber));
            beta = Math.min(beta, v);
            if (beta <= alpha) {
                // alpha cut-off
                break;
            }
        }
        return v;
    }

    private GameTreeNode generateChild(GameTreeNode parent, Move moveToApply) {
        GameTreeNode node = new GameTreeNode();
        node.ourPlayerNumber = parent.ourPlayerNumber;
        node.boardState = new GawihsBoard(parent.boardState);

        Queue<Integer> playerNumberQueue = new LinkedList<>();
        playerNumberQueue.add(FieldState.PLAYER_0.ordinal());
        playerNumberQueue.add(FieldState.PLAYER_1.ordinal());
        playerNumberQueue.add(FieldState.PLAYER_2.ordinal());

        // determine the nextPlayer through a queue
        Integer currentPlayerNumber;
        do {
            currentPlayerNumber = playerNumberQueue.poll();
            playerNumberQueue.add(currentPlayerNumber);
        } while (currentPlayerNumber != parent.currentPlayerNumber);

        // apply move to board and player
        node.boardState.applyMove(currentPlayerNumber, moveToApply);

        // hand over turn to next player
        node.currentPlayerNumber = playerNumberQueue.poll();
        return node;
    }

    @Override
    public Move getBestMove() {
        // check if we can move at all
        if (getPossibleMoves(this.board, this.player).size() == 0) {
            throw new IllegalStateException("No moves left");
        }

        // generate and assess GameTree
        int numberOfPlayers = this.enemies.size() + 1;
        // FIXME tweak me
        int numberOfPlies = 1;
        // Zahl der Stufen bei 2 zwei Spielern = 3 (+ die Wurzel, bei 3 Spielern = 4 (+
        // die Wurzel)
        int targetDepth = (numberOfPlayers + 1) * numberOfPlies;
        GameTreeNode root = new GameTreeNode();
        root.boardState = new GawihsBoard(this.board);
        root.currentPlayerNumber = root.ourPlayerNumber = this.player.getPlayerNumberAsOrdinal();

        return startAlphaBetaSearch(root, targetDepth);
    }

    private Move startAlphaBetaSearch(GameTreeNode root, int currentDepth) {
        List<Move> possibleMoves = getPossibleMoves(root.boardState, root.currentPlayerNumber);

        if (possibleMoves.size() == 0) {
            // check if we can move at all
            throw new IllegalStateException("No moves left");
        }

        // our first node is always a maximizing player
        long currentBestValue = Long.MIN_VALUE;
        long alpha = Long.MIN_VALUE;
        long beta = Long.MAX_VALUE;
        Map<Long, Move> generatedMoves = new HashMap<>();
        for (Move possibleMove : possibleMoves) {
            GameTreeNode child = generateChild(root, possibleMove);
            currentBestValue = Math.max(currentBestValue, alphabeta(child, currentDepth - 1, alpha, beta, false));
            currentBestValue = Math.max(alpha, currentBestValue);
            generatedMoves.put(currentBestValue, possibleMove);
            if (beta <= alpha) {
                // beta cut-off
                break;
            }
        }
        return generatedMoves.get(currentBestValue);
    }

}
