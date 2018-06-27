package de.htw_berlin.ai_for_games.pathfinding;

public class Subgraph extends Graph {

    private final Maingraph maingraph;

    private final Node topLeftNode;

    private final Node bottomRightNode;

    public Subgraph(Maingraph maingraph, int xTopLeft, int yTopLeft, int xBottomRight, int yBottomRight) {
        // get actual first & last node as bound definition
        Node tempNode;
        while ((tempNode = maingraph.getNode(xTopLeft, yTopLeft)) == null) {
            xTopLeft--;
        }
        this.topLeftNode = tempNode;

        tempNode = null;
        while ((tempNode = maingraph.getNode(xBottomRight, yBottomRight)) == null) {
            xBottomRight--;
        }
        this.bottomRightNode = tempNode;

        // set reference to maingraph
        this.maingraph = maingraph;
    }

    @Override
    public Graph createSubgraph(int xTopLeft, int yTopLeft, int xBottomRight, int yBottomRight) {
        // FIXME restrict this?
        return this.maingraph.createSubgraph(xTopLeft, yTopLeft, xBottomRight, yBottomRight);
    }

    @Override
    public Node getNode(int x, int y) {
        return isOutOfBounds(x, y) ? null : this.maingraph.getNode(x, y);
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < this.topLeftNode.x || x > this.bottomRightNode.x //
                || y < this.topLeftNode.y || y > this.bottomRightNode.y;
    }

}
