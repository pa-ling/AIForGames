package de.htw_berlin.ai_for_games.player.strategies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.MoveStrategy;
import lenz.htw.gawihs.Move;

public class ThreadedAlphaBetaPruningStrategy implements MoveStrategy {

    private class AlphaBetaPruningStrategyTask extends AssessedMoveStrategy implements Runnable {

        private class GameTreeNode {
            /** Nummer des Spielers, für den der Zug gefunden werden. */
            public int ourPlayerNumber;

            /** Spieler, der in diesem Node die Züge generiert (aka der dran ist). */
            public GawihsPlayer currentPlayer;

            /** State des ersten Spieler, nach dem Zug. */
            public GawihsPlayer playerOne;

            /** State des zweiten Spieler, nach dem Zug. */
            public GawihsPlayer playerTwo;

            /** State des dritten Spieler, nach dem Zug. */
            public GawihsPlayer playerThree;

            /** Das Board, nachdem der Move des Zuges darauf angewendet wurde. */
            public GawihsBoard boardState;

        }

        private final ThreadedAlphaBetaPruningStrategy caller;

        public AlphaBetaPruningStrategyTask(String configPath, ThreadedAlphaBetaPruningStrategy caller) {
            super(configPath);
            this.caller = caller;
        }

        /**
         * Bewertet den übergebenen Node. Basiert auf:
         * https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning#Pseudocode
         */
        private long alphabeta(GameTreeNode currentNode, int currentDepth, long alpha, long beta,
                boolean isMaximizingPlayer) {
            if (currentDepth == 0) {
                // this is a terminal node - assess it
                List<GawihsPlayer> enemies = new ArrayList<>();
                enemies.add(currentNode.playerOne);
                enemies.add(currentNode.playerTwo);
                enemies.add(currentNode.playerThree);
                enemies.remove(currentNode.currentPlayer);
                return assessBoard(currentNode.boardState, this.player, enemies);
            }

            List<Move> possibleMoves = getPossibleMoves(currentNode.boardState, currentNode.currentPlayer);

            if (possibleMoves.size() == 0) {
                // this is a node where one player dies
                if (currentNode.currentPlayer.getPlayerNumberAsOrdinal() == currentNode.ourPlayerNumber) {
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
                    alpha = Math.max(alpha, v);
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
                        child.currentPlayer.getPlayerNumberAsOrdinal() == currentNode.ourPlayerNumber));
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

            // copy players and determine next player
            Queue<GawihsPlayer> playerQueue = new LinkedList<>();
            if (parent.playerOne != null) {
                node.playerOne = new GawihsPlayer(parent.playerOne, node.boardState);
                playerQueue.add(node.playerOne);
            }
            if (parent.playerTwo != null) {
                node.playerTwo = new GawihsPlayer(parent.playerTwo, node.boardState);
                playerQueue.add(node.playerTwo);
            }
            if (parent.playerThree != null) {
                node.playerThree = new GawihsPlayer(parent.playerThree, node.boardState);
                playerQueue.add(node.playerThree);
            }

            // determine the nextPlayer through a queue
            GawihsPlayer currentPlayer;
            do {
                currentPlayer = playerQueue.poll();
                playerQueue.add(currentPlayer);
            } while (currentPlayer.getPlayerNumberAsOrdinal() != parent.currentPlayer.getPlayerNumberAsOrdinal());

            // apply move to board and player
            currentPlayer.applyMove(moveToApply);
            node.boardState.applyMove(moveToApply);

            // hand over turn to next player
            node.currentPlayer = playerQueue.poll();
            return node;
        }

        @Override
        public Move getBestMove() {
            throw new UnsupportedOperationException("This method must be called on the caller of the task.");
        }

        @Override
        public void run() {
            startAlphaBetaSearch();
        }

        private void startAlphaBetaSearch() {
            // generate and assess GameTree
            // more kills performance
            int targetDepth = 3;
            GameTreeNode root = new GameTreeNode();
            root.boardState = new GawihsBoard(this.board);
            root.currentPlayer = new GawihsPlayer(this.player, root.boardState);
            root.playerOne = root.playerTwo = root.playerThree = root.currentPlayer;
            root.ourPlayerNumber = this.player.getPlayerNumberAsOrdinal();

            for (GawihsPlayer enemy : this.enemies) {
                if (enemy.getPlayerNumberAsOrdinal() == 0) {
                    root.playerOne = new GawihsPlayer(enemy);
                }
                if (enemy.getPlayerNumberAsOrdinal() == 1) {
                    root.playerTwo = new GawihsPlayer(enemy);
                }
                if (enemy.getPlayerNumberAsOrdinal() == 2) {
                    root.playerThree = new GawihsPlayer(enemy);
                }
            }

            List<Move> possibleMoves = getPossibleMoves(root.boardState, root.currentPlayer);

            if (possibleMoves.size() == 0) {
                // check if we can move at all
                throw new IllegalStateException("No moves left");
            }

            // fallback -> set first move found as best move to avoid having no moves at all
            this.caller.setBestMove(possibleMoves.get(0));

            // our first node is always a maximizing player
            long v = Long.MIN_VALUE;
            long alpha = Long.MIN_VALUE;
            long beta = Long.MAX_VALUE;
            for (Move possibleMove : possibleMoves) {
                GameTreeNode child = generateChild(root, possibleMove);
                v = Math.max(v, alphabeta(child, targetDepth - 1, alpha, beta, false));
                if (v > alpha) {
                    alpha = v;
                    this.caller.setBestMove(possibleMove);
                }

                if (beta <= alpha || Thread.interrupted()) {
                    // beta cut-off
                    break;
                }
            }
        }

    }

    private Move bestMove;

    private final AlphaBetaPruningStrategyTask task;
    private final long timeout;
    private long expectedNetworkLatency;

    public ThreadedAlphaBetaPruningStrategy(String configPath, int timeout) {
        this.task = new AlphaBetaPruningStrategyTask(configPath, this);
        this.timeout = timeout * 1000;
    }

    @Override
    public Move getBestMove() {
        Thread thread = new Thread(this.task);
        thread.start();
        if (this.expectedNetworkLatency > this.timeout) {
            this.expectedNetworkLatency = 0;
        }
        long timeLimit = (long) (0.95 * (this.timeout - this.expectedNetworkLatency));
        long elapsedTime = 0;
        do {
            long startTime = System.nanoTime();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                thread.interrupt();
                e.printStackTrace();
            }
            elapsedTime += (System.nanoTime() - startTime) / 1000000L;
        } while (elapsedTime < timeLimit && thread.isAlive());

        Move move = this.bestMove;
        if (move == null) {
            System.out.println("[ERROR] No move could be found in time.");
        }
        this.bestMove = null;
        thread.interrupt();
        return move;
    }

    @Override
    public List<Move> getPossibleMoves() {
        throw new UnsupportedOperationException("This method is not supported by this strategy.");
    }

    protected void setBestMove(Move bestMove) {
        System.out.println("Got a new move: " + bestMove);
        this.bestMove = bestMove;
    }

    @Override
    public void setBoard(GawihsBoard board) {
        this.task.setBoard(board);
    }

    @Override
    public void setEnemies(List<GawihsPlayer> enemies) {
        this.task.setEnemies(enemies);
    }

    public void setExpectedNetworkLatency(long networkLatency) {
        this.expectedNetworkLatency = networkLatency;
    }

    @Override
    public void setPlayer(GawihsPlayer player) {
        this.task.setPlayer(player);
    }

}
