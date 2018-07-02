package de.htw_berlin.ai_for_games.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.board.Pair;

public class AStarMock {

    private static class MockGraph {
        private final int nodes[][];
        private final Color playerColor;
        private final int size;

        public MockGraph() {
            this.playerColor = Color.RED;
            this.size = 4;

            // init graph
            this.nodes = new int[this.size][this.size];

            // fill with white
            for (int[] rows : this.nodes) {
                Arrays.fill(rows, Color.WHITE.intValue);
            }

            // set obstacles
            this.nodes[2][1] = Color.BLACK.intValue;
            this.nodes[2][2] = Color.BLACK.intValue;

            // set enemy colors
            this.nodes[1][1] = Color.BLUE.intValue;
            this.nodes[1][2] = Color.BLUE.intValue;
            this.nodes[1][3] = Color.BLUE.intValue;
            this.nodes[2][3] = Color.BLUE.intValue;
        }

        public int getCost(int x, int y) {
            int targetNodeValue = this.nodes[x][y];

            if (targetNodeValue == Color.WHITE.intValue) {
                return 310;
            }

            if (targetNodeValue == Color.BLACK.intValue) {
                // leave room for heuristic
                return Integer.MAX_VALUE - 1024;
            }

            int blueValue = targetNodeValue & 0xFF;
            int greenValue = targetNodeValue >> 8 & 0xFF;
            int redValue = targetNodeValue >> 16 & 0xFF;

            int costs = 510;
            if (this.playerColor == Color.RED) { // we are red
                costs += redValue;
                costs -= greenValue;
                costs -= blueValue;
            } else if (this.playerColor == Color.GREEN) { // we are green
                costs -= redValue;
                costs += greenValue;
                costs -= blueValue;
            } else if (this.playerColor == Color.BLUE) { // we are blue
                costs -= redValue;
                costs -= greenValue;
                costs += blueValue;
            }

            return costs;
        }

        public List<Pair> getNeighbors(int x, int y) {
            List<Pair> neighbours = new ArrayList<>(8);
            for (int i = -1; i <= 1; i++) {

                int neighbourX = x + i;
                if (neighbourX < 0 || neighbourX >= this.size) { // OutOfBounds check
                    continue;
                }

                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }

                    int neighbourY = y + j;

                    if (neighbourY < 0 || neighbourY >= this.size) { // OutOfBounds check
                        continue;
                    }

                    neighbours.add(new Pair(neighbourX, neighbourY));
                }
            }
            return neighbours;
        }
    }

    private static class NodePriorityPair {
        public Pair node;
        public int priority;

        public NodePriorityPair(Pair node, int priority) {
            this.node = node;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "NodePriorityPair [Node: " + this.node.x + ", " + this.node.y + "; Prio " + this.priority + "]";
        }
    }

    private static List<Pair> addPredecessor(final List<Pair> path, final Map<Pair, Pair> cameFrom,
            final Pair startNode) {
        final Pair currentNode = path.get(0);
        if (currentNode.equals(startNode)) {
            return path;
        }

        path.add(0, cameFrom.get(currentNode));
        return addPredecessor(path, cameFrom, startNode);
    }

    private static int calculateHeuristic(final Pair node, final Pair targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

    public static void main(String[] args) {
        List<Pair> run = AStarMock.run();
        System.out.println(run);
    }

    private static List<Pair> reconstructPath(final Pair startNode, final Pair targetNode,
            final Map<Pair, Pair> cameFrom) {
        final ArrayList<Pair> path = new ArrayList<>();
        path.add(targetNode);
        return addPredecessor(path, cameFrom, startNode);
    }

    public static List<Pair> run() {
        final Pair startNode = new Pair(0, 0);
        final Pair targetNode = new Pair(3, 3);
        final MockGraph graph = new MockGraph();
        // openList -> nodes to search
        final PriorityQueue<NodePriorityPair> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.priority));
        openList.add(new NodePriorityPair(startNode, 0));

        // map of visited nodes - stores the best way found, comparable to a closed list
        final Map<Pair, Pair> cameFrom = new HashMap<>();
        cameFrom.put(startNode, startNode);

        // costs
        final Map<Pair, Integer> costs = new HashMap<>();
        costs.put(startNode, 0);

        // start search
        while (!openList.isEmpty()) {
            System.out.println(openList);
            Pair currentNode = openList.poll().node;
            if (currentNode.equals(targetNode)) {
                break;
            }

            for (final Pair nextNode : graph.getNeighbors(currentNode.x, currentNode.y)) {
                Integer newCost = costs.get(currentNode) + graph.getCost(nextNode.x, nextNode.y);
                if (!costs.containsKey(nextNode) || newCost < costs.get(nextNode)) {
                    costs.put(nextNode, newCost);
                    int priority = newCost + calculateHeuristic(nextNode, targetNode);
                    openList.add(new NodePriorityPair(nextNode, priority));
                    cameFrom.put(nextNode, currentNode);
                }
            }
        }

        // check if path was found
        if (!cameFrom.containsKey(targetNode)) {
            return new ArrayList<>();
        }

        // return path
        return reconstructPath(startNode, targetNode, cameFrom);
    }
}
