package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.htw_berlin.ai_for_games.Pair;

public class AStar {

    private static int calculateHeuristic(final Pair node, final Pair targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

    private final Set<Pair> closedSet = new HashSet<>();
    private final Set<Pair> openSet = new HashSet<>();
    private final Map<Pair, Pair> cameFrom = new HashMap<>();
    private final Map<Pair, Integer> gScore = new HashMap<>();
    private final Map<Pair, Integer> fScore = new HashMap<>();

    private Pair getNodeWithLowestFScore() {
        Pair currentNodeWithLowestFScore = null;
        int currentLowestFScore = Integer.MAX_VALUE;
        for (Pair node : this.openSet) {
            int currentFScore = this.fScore.get(node);
            if (currentFScore < currentLowestFScore) {
                currentNodeWithLowestFScore = node;
                currentLowestFScore = currentFScore;
            }
        }
        return currentNodeWithLowestFScore;
    }

    public List<Pair> getPath(Layer pathLayer, Pair start, Pair target) {
        this.openSet.add(start);
        this.gScore.put(start, 0);
        this.fScore.put(start, calculateHeuristic(start, target));

        while (!this.openSet.isEmpty()) {
            Pair current = getNodeWithLowestFScore();
            if (current.equals(target)) {
                return reconstructPath(current);
            }

            this.openSet.remove(current);
            this.closedSet.add(current);

            List<Pair> neighbours = pathLayer.getNeighbors(current.x, current.y);
            for (Pair neighbour : neighbours) {
                if (this.closedSet.contains(neighbour)) {
                    continue;
                }

                if (!this.openSet.contains(neighbour)) {
                    this.openSet.add(neighbour);
                }

                int tentativeGScore = this.gScore.get(current) + pathLayer.getCost(neighbour.x, neighbour.y);
                Integer currentGScore = this.gScore.get(neighbour);
                if (currentGScore != null && tentativeGScore >= currentGScore) {
                    continue;
                }

                this.cameFrom.put(neighbour, current);
                this.gScore.put(neighbour, tentativeGScore);
                this.fScore.put(neighbour, tentativeGScore + calculateHeuristic(neighbour, target));
            }

        }

        System.out.println("Das war wohl nichts mit dem A* :(");
        return new ArrayList<>();
    }

    private List<Pair> reconstructPath(Pair current) {
        List<Pair> totalPath = new ArrayList<>();
        totalPath.add(current);

        while (this.cameFrom.containsKey(current)) {
            current = this.cameFrom.get(current);
            totalPath.add(0, current); // prepend
        }
        return totalPath;
    }

}
