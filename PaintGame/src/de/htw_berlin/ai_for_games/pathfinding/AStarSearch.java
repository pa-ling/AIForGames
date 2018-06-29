package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;

public final class AStarSearch implements PathfindingStrategy {
    private class NodePriorityPair {
        public Node node;
        public int priority;

        public NodePriorityPair(Node node, int priority) {
            this.node = node;
            this.priority = priority;
        }
    }

    private static List<Node> addPredecessor(final List<Node> path, final Map<Node, Node> cameFrom,
            final Node startNode) {
        final Node currentNode = path.get(0);
        path.add(0, cameFrom.get(currentNode));
        if (currentNode.equals(startNode)) {
            return path;
        }
        return addPredecessor(path, cameFrom, startNode);
    }

    private static int calculateHeuristic(final Node node, final Node targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

    private static List<Node> reconstructPath(final Node startNode, final Node targetNode,
            final Map<Node, Node> cameFrom) {
        final ArrayList<Node> path = new ArrayList<>();
        path.add(targetNode);
        return addPredecessor(path, cameFrom, startNode);
    }

    @Override
    public List<Node> getPath(BoardInterface graph, Node startNode, Node targetNode, BotType bot) {
        // openList -> nodes to search
        final PriorityQueue<NodePriorityPair> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.priority));
        openList.add(new NodePriorityPair(startNode, 0));

        // map of visited nodes - stores the best way found, comparable to a closed list
        final Map<Node, Node> cameFrom = new HashMap<>();
        cameFrom.put(startNode, startNode);

        // costs
        final Map<Node, Integer> costs = new HashMap<>();
        costs.put(startNode, 0);

        // start search
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll().node;
            if (currentNode.equals(targetNode)) {
                break;
            }

            for (final Node nextNode : graph.getNeighbors(currentNode)) {
                Integer newCost = costs.get(currentNode) + graph.getCost(currentNode, nextNode);
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
