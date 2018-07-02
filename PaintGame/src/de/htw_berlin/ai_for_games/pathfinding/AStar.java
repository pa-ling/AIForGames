package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.htw_berlin.ai_for_games.pathfinding.board.Layer;
import de.htw_berlin.ai_for_games.pathfinding.board.Pair;

public class AStar {

    private Set<Pair> closedSet = new HashSet<>();
    private Set<Pair> openSet = new HashSet<>();
    private Map<Pair, Pair> cameFrom = new HashMap<>();
    private Map<Pair, Integer> gScore = new HashMap<>();
    private Map<Pair, Integer> fScore = new HashMap<>();

    public List<Pair> getPath(Layer pathLayer, Pair start, Pair target) {
        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, calculateHeuristic(start, target));

        while (!openSet.isEmpty()) {
            Pair current = getNodeWithLowestFScore();

            if (current.equals(target)) {
                return reconstructPath(current);
            }

            openSet.remove(current);

            closedSet.add(current);

            List<Pair> neighbours = pathLayer.getNeighbors(current.x, current.y);
            for (Pair neighbour : neighbours) {
                if (closedSet.contains(neighbour)) {
                    continue;
                }

                if (openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                }

                int tentativeGScore = gScore.get(current) + pathLayer.getCost(neighbour.x, neighbour.y);
                Integer currentGScore = gScore.get(neighbour);
                if (currentGScore != null && tentativeGScore >= currentGScore) {
                    continue;
                }

                cameFrom.put(neighbour, current);
                gScore.put(neighbour, tentativeGScore);
                fScore.put(neighbour, tentativeGScore + calculateHeuristic(neighbour, target));
            }

        }

        System.out.println("Das war wohl nichts mit dem A* :(");
        return new ArrayList<>();
    }

    private List<Pair> reconstructPath(Pair current) {
        List<Pair> totalPath = new ArrayList<>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(0, current); // prepend
        }
        return totalPath;
    }

    private Pair getNodeWithLowestFScore() {
        Pair currentNodeWithLowestFScore = null;
        int currentLowestFScore = Integer.MAX_VALUE;
        for (Pair node : openSet) {
            int currentFScore = this.fScore.get(node);
            if (currentFScore < currentLowestFScore) {
                currentNodeWithLowestFScore = node;
                currentLowestFScore = currentFScore;
            }
        }
        return currentNodeWithLowestFScore;
    }

    private static int calculateHeuristic(final Pair node, final Pair targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

}
