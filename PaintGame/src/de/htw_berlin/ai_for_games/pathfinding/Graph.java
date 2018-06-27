package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

import lenz.htw.zpifub.Update;

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

    /**
     * Returns the {@link Node} which contains the given pixel.
     *
     * @param x
     *            x coordinate of the pixel
     * @param y
     *            y coordinate of the pixel
     * @return a node or {@code null} if the pixel is not found in this graph
     */
    public Node getNodeForPixelPosition(int x, int y) {
        // TODO use function which maps a pixel to a node
        return null;
    }

    public void setItemAsObstacle(Update update) {
        // TODO set item as obstacle
        // we know the item is bad
        // set node value for pixel position to -infinity to mark it as obstacle
    }

    /**
     * Update the color values inside the graph according to the update.
     *
     * @param update
     *            update to apply to the graph
     */
    public void update(Update update) {
        // TODO update graph color values
        // check if update is relevant
        // get the affected area
        // update node and pixel values
    }

}
