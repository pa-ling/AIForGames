package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the gaming field (aka Board).
 *
 * @author Maxmilian Schulze
 *
 */
public class Maingraph extends Graph {

    private static final int GRAPH_WIDTH = 10;

    private final List<Node> nodes;

    public Maingraph() {
        this.nodes = new ArrayList<>();
        for (int j = 0; j < GRAPH_WIDTH; j++) {
            for (int i = 0; i < GRAPH_WIDTH; i++) {
                this.nodes.add(new Node(i, j));
            }
        }
    }

    @Override
    public Subgraph createSubgraph(int xTopLeft, int yTopLeft, int xBottomRight, int yBottomRight) {
        return new Subgraph(this, xTopLeft, yTopLeft, xBottomRight, yBottomRight);
    }

    @Override
    public Node getNode(int x, int y) {
        int index = GRAPH_WIDTH * y + x;
        if (index > GRAPH_WIDTH * GRAPH_WIDTH - 1) {
            return null;
        }

        return this.nodes.get(index);
    }

}
