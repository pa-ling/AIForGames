package de.htw_berlin.ai_for_games;

import java.util.List;

import de.htw_berlin.ai_for_games.pathfinding.Node;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public interface BoardInterface {

    // needed for A* search
    /**
     * Returns the weight of the edge. The weight is determined by the
     * attractiveness of the node. That means a node with our own color or white is
     * less attractive than a node in an enemy number.
     *
     * @param startNode
     *            startNode of the edge
     * @param targetNode
     *            targetNode of the edge. Must be a neighbor of the startNode.
     * @return an integer value encoding the attractiveness
     */
    public int getCost(final Node startNode, final Node targetNode);

    // needed for A* search
    /**
     * Returns all neighboring nodes of the given node. Must not contain
     * {@code null} values.
     *
     * @param node
     *            central node
     * @return a list containing all neighboring nodes
     */
    public List<Node> getNeighbors(Node node);

    // needed in Bot to find node for item and to set current position of bot as
    // node
    /**
     * Returns the {@link Node} which contains the given pixel.
     *
     * @param x
     *            x coordinate of the pixel
     * @param y
     *            y coordinate of the pixel
     * @return a node or {@code null} if the pixel is not found in this graph
     */
    public Node getNodeForPixelPosition(int x, int y);

    // needed in controller to react to a bad item
    /**
     * Remove an item which was set as obstacle. Useful for {@link ItemType#CLOCK
     * clocks}.
     *
     * @param update
     *            update containing the former position of the item.
     */
    public void removeObstacleItem(Update update);

    // needed in controller to react to the consumption of a bad item
    /**
     * Set an item as obstacle. Useful for {@link ItemType#CLOCK clocks}.
     *
     * @param update
     *            update containing the position of the item.
     */
    public void setItemAsObstacle(Update update);

    // currently needed inside getCost method of the graph (not pushed) but I'm sure
    // it will be needed in the new class too.
    /**
     * Set the {@link NetworkClient} for the board. Used in color calculation and
     * obstacle detection.
     *
     * @param client
     *            NetworkClient to set for this board
     */
    public void setNetworkClient(NetworkClient client);

    // ourColor is needed in getCost function
    /**
     * Makes the color of the current player known to the board.
     *
     * @param ourColor
     *            ourColor coded as integer
     */
    public void setOurColor(int ourColor);

}
