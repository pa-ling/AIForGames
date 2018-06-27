package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private static final int GRAPH_WIDTH = 10;

    private final List<Node> nodes;

    public Graph() {
        this.nodes = new ArrayList<>();
        for (int j = 0; j < GRAPH_WIDTH; j++) {
            for (int i = 0; i < GRAPH_WIDTH; i++) {
                this.nodes.add(new Node(i, j));
            }
        }
    }

    /**
     * Returns all neighboring nodes of the given node. Must not contain
     * {@code null} values.
     *
     * @param node
     *            central node
     * @return a list containing all neighboring nodes
     */
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        // TODO actually find the neighbors
        neighbors.removeIf(e -> e == null);
        return neighbors;
    }

    /**
     * Returns the node with the given coordinates.
     *
     * @param x
     *            x coordinate of the node
     * @param y
     *            y coordinate of the node
     * @return a {@link Node} or {@code null} if the node is not inside this graph
     */
    public Node getNode(int x, int y) {
        int index = GRAPH_WIDTH * y + x;
        if (index > GRAPH_WIDTH * GRAPH_WIDTH - 1) {
            return null;
        }

        return this.nodes.get(index);
    }

}
