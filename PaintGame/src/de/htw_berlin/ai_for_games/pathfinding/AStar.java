package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import de.htw_berlin.ai_for_games.Pair;

public final class AStar {

    private static int calculateHeuristic(final Pair node, final Pair targetNode) {
        // direct distance between node and targetNode
        return Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y);
    }

    private static Pair getNodeWithLowestFScore(final Set<Pair> openSet, final Map<Pair, Integer> fScore) {
        Pair currentNodeWithLowestFScore = null;
        int currentLowestFScore = Integer.MAX_VALUE;
        for (final Pair node : openSet) {
            final int currentFScore = fScore.get(node);
            if (currentFScore < currentLowestFScore) {
                currentNodeWithLowestFScore = node;
                currentLowestFScore = currentFScore;
            }
        }
        return currentNodeWithLowestFScore;
    }

    public static List<Pair> getPath(final Layer pathLayer, final BiFunction<Integer, Integer, Integer> costFunction,
            final Pair start, final Pair target) {
        final Set<Pair> closedSet = new HashSet<>();
        final Set<Pair> openSet = new HashSet<>();
        final Map<Pair, Pair> cameFrom = new HashMap<>();
        final Map<Pair, Integer> gScore = new HashMap<>();
        final Map<Pair, Integer> fScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, calculateHeuristic(start, target));

        while (!openSet.isEmpty()) {
            final Pair current = getNodeWithLowestFScore(openSet, fScore);
            if (current.equals(target)) {
                return reconstructPath(current, cameFrom);
            }

            openSet.remove(current);
            closedSet.add(current);

            final List<Pair> neighbours = pathLayer.getNeighbors(current.x, current.y);
            for (final Pair neighbour : neighbours) {
                if (closedSet.contains(neighbour)) {
                    continue;
                }

                if (!openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                }

                final int tentativeGScore = gScore.get(current) + costFunction.apply(neighbour.x, neighbour.y);
                final Integer currentGScore = gScore.get(neighbour);
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

    private static List<Pair> reconstructPath(Pair current, final Map<Pair, Pair> cameFrom) {
        final List<Pair> totalPath = new ArrayList<>();
        totalPath.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(0, current); // prepend
        }
        return totalPath;
    }

}
