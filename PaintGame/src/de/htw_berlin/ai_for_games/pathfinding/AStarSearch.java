package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.pathfinding.board.Layer;
import de.htw_berlin.ai_for_games.pathfinding.board.Pair;

public final class AStarSearch {

    private class NodePriorityPair {
        public Pair node;
        public int priority;

        public NodePriorityPair(Pair node, int priority) {
            this.node = node;
            this.priority = priority;
        }
    }

    private static List<Pair> addPredecessor(final List<Pair> path, final Map<Pair, Pair> cameFrom,
            final Pair startNode) {
        final Pair currentNode = path.get(0);
        path.add(0, cameFrom.get(currentNode));
        if (currentNode.equals(startNode)) {
            return path;
        }
        return addPredecessor(path, cameFrom, startNode);
    }

    private static int calculateHeuristic(final Pair node, final Pair targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

    private static List<Pair> reconstructPath(final Pair startNode, final Pair targetNode,
            final Map<Pair, Pair> cameFrom) {
        final ArrayList<Pair> path = new ArrayList<>();
        path.add(targetNode);
        return addPredecessor(path, cameFrom, startNode);
    }

    public List<Pair> getPath(Layer pathLayer, Pair startNode, Pair targetNode, BotType bot) {
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
            Pair currentNode = openList.poll().node;
            if (currentNode.equals(targetNode)) {
                break;
            }

            for (final Pair nextNode : pathLayer.getNeighbors(currentNode.x, currentNode.y)) {
                Integer newCost = costs.get(currentNode) + pathLayer.getCost(nextNode.x, nextNode.y);
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
