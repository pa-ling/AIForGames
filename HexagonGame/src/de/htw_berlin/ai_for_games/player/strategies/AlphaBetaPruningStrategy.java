package de.htw_berlin.ai_for_games.player.strategies;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.board.GawihsBoard.FieldState;
import lenz.htw.gawihs.Move;

public class AlphaBetaPruningStrategy extends AssessedMoveStrategy {
    private class GameTreeNode {
        /**
         * TODO: Kann das weg? Das ist nur für den sanity check
         *
         * Zeigt auf den vorherigen Node, der den BoardState darstellt, bevor der in
         * diesem Node gespeicherte Move auf das Board angewendet wurde.
         */
        public GameTreeNode parent;

        /**
         * TODO: kann das weg? Ich muss die Referenz eigentlich nicht aufheben, ich
         * benötige sie nur einmal ganz am Ende
         *
         * Kinder, die die BoardStates darstellen, die nach diesem BoardState möglich
         * sind.
         */
        public List<GameTreeNode> children;

        /**
         * TODO: Kann das weg? ich bruache das nur einmal ganz am Ende, um den Zug zu
         * suchen, der das Beste Ergebnis bringt
         *
         * Bewertung des aktuellen Knotens.
         */
        public long assessment;

        /** Nummer des Spielers, für den der Zug gefunden werden soll. */
        public int ourPlayerNumber;

        /**
         * Nummer des Spieler, der in diesem Node die Züge generiert (aka der dran ist).
         */
        public int currentPlayerNumber;

        /** Das Board, nachdem der Move des Zuges darauf angewendet wurde. */
        public GawihsBoard boardState;

        /**
         * TODO: Kann der weg? Ich brauche den nur ganz am Ende, aber ich speichere das
         * für jedes Blatt
         *
         * Move, der in diesem Zug angewendet wurde.
         */
        public Move move;

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
            // sanity check - assert that last player is our player
            if (currentNode.parent.currentPlayerNumber != currentNode.ourPlayerNumber) {
                throw new IllegalStateException("Bewertung des Blattes findet nicht für unseren Spieler statt!");
            }
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
            currentNode.assessment = v;
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
        currentNode.assessment = v;
        return v;
    }

    private GameTreeNode generateChild(GameTreeNode parent, Move moveToApply) {
        GameTreeNode node = new GameTreeNode();
        node.parent = parent;
        // Tracking the children makes the RAM explode!
        // parent.children.add(node);
        node.children = new ArrayList<>();
        node.move = moveToApply;
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
        root.children = new ArrayList<>();
        root.boardState = new GawihsBoard(this.board);
        root.currentPlayerNumber = root.ourPlayerNumber = this.player.getPlayerNumberAsOrdinal();

        long bestValue = alphabeta(root, targetDepth, Long.MIN_VALUE, Long.MAX_VALUE, true);
        // get best move by iterating over direct children and return move of the child
        // with the best value
        for (GameTreeNode child : root.children) {
            if (child.assessment == bestValue) {
                return child.move;
            }
        }

        throw new IllegalStateException("No moves left");
    }

}
