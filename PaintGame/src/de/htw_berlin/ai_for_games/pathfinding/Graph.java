package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

public abstract class Graph {

    /**
     * Creates a new independent subgraph of this graph with the given points as
     * frame.
     *
     * @param xTopLeft
     * @param yTopLeft
     * @param xBottomRight
     * @param yBottomRight
     */
    public abstract Graph createSubgraph(int xTopLeft, int yTopLeft, int xBottomRight, int yBottomRight);

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
        // leave offset calculation to getNode method
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
    public abstract Node getNode(int x, int y);

}
