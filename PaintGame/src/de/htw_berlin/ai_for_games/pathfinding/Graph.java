package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.BoardInterface;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class Graph implements BoardInterface {

    private static final int NODE_WIDTH = 1;
    private static final int NODE_HEIGHT = 1;
    private static final int GRAPH_WIDTH = 10;
    private static final int GRAPH_HEIGHT = 1000 / GRAPH_WIDTH / (NODE_HEIGHT * NODE_WIDTH);

    private final List<Node> nodes;
    private int ourColor;

    public Graph() {
        this.nodes = new ArrayList<>();
        for (int j = 0; j < GRAPH_WIDTH; j++) {
            for (int i = 0; i < GRAPH_WIDTH; i++) {
                this.nodes.add(new Node(i, j));
            }
        }
    }

    @Override
    public int getCost(final Node startNode, final Node targetNode) {
        // sanity check
        if (targetNode.x != startNode.x && targetNode.x != startNode.x + 1 && targetNode.x != startNode.x - 1
                || targetNode.y != startNode.y && targetNode.y != startNode.y + 1 && targetNode.y != startNode.y - 1) {
            throw new IllegalStateException(String.format(
                    "Cannot calculate cost. The node %d, %d is not a direct neighbor of this node (%d, %d).",
                    targetNode.x, targetNode.y, startNode.x, startNode.y));
        }

        // cost to oneself is zero
        if (startNode.equals(targetNode)) {
            return 0;
        }

        // FIXME cost function must be improved
        return targetNode.value;
    }

    @Override
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        // FIXME actually find the neighbors
        neighbors.removeIf(e -> e == null);
        return neighbors;
    }

    @Deprecated
    public Node getNode(int x, int y) {
        int index = GRAPH_WIDTH * y + x;
        if (index < 0 || index > GRAPH_WIDTH * GRAPH_HEIGHT - 1) {
            return null;
        }

        return this.nodes.get(index);
    }

    @Override
    public Node getNodeForPixelPosition(int x, int y) {
        // TODO use function which maps a pixel to a node
        return null;
    }

    @Override
    public void removeObstacleItem(Update update) {
        getNodeForPixelPosition(update.x, update.y).value = Color.WHITE.intValue;
    }

    @Override
    public void setItemAsObstacle(Update update) {
        getNodeForPixelPosition(update.x, update.y).value = Color.BLACK.intValue;
    }

    @Override
    public void setOurColor(int ourColor) {
        this.ourColor = ourColor;
    }

    /**
     * Update the color values inside the graph according to the update.
     *
     * @param update
     *            update to apply to the graph
     */
    @Deprecated
    public void update(Update update, NetworkClient client) {
        // check if update is relevant
        if (update.type == null) {
            // set color value of this node
            int colorValue = client.getBoard(update.x, update.y);
            getNodeForPixelPosition(update.x, update.y).value = colorValue;

            // check if radius is bigger than our node
            int influenceRadiusForBot = client.getInfluenceRadiusForBot(update.bot);
            if (influenceRadiusForBot > NODE_WIDTH * NODE_HEIGHT) {
                // update color value of adjacent nodes
                // how to compute adjacent nodes?
            }

        }
    }

}
