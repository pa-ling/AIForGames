package de.htw_berlin.ai_for_games.pathfinding;

import de.htw_berlin.ai_for_games.Pair;
import lenz.htw.zpifub.net.NetworkClient;

public class QuadTree {

    private static final int PATH_LAYER_NUMBER = 6;
    private static final int UPDATE_LAYER_NUMBER = 2;

    private Layer pathLayer;
    private Layer updateLayer;

    public QuadTree(Color playerColor, NetworkClient networkClient) {
        Layer currentLayer = new Layer(Layer.ROCK_BOTTOM_LAYER_NUMBER, playerColor, networkClient);
        for (int i = Layer.ROCK_BOTTOM_LAYER_NUMBER - 1; i >= UPDATE_LAYER_NUMBER; i--) {
            currentLayer = new Layer(i, playerColor, currentLayer);
            if (i == UPDATE_LAYER_NUMBER) {
                this.updateLayer = currentLayer;
            }
            if (i == PATH_LAYER_NUMBER) {
                this.pathLayer = currentLayer;
            }
        }
    }

    public Layer getPathLayer() {
        return this.pathLayer;
    }

    public Pair getTargetOnPathLayer() {
        return this.updateLayer.getNodePositionWithHighestCosts(this.pathLayer.getNumber());
    }

    public void initNodes() {
        this.updateLayer.updateNode(0, 0);
        this.updateLayer.updateNode(1, 0);
        this.updateLayer.updateNode(0, 1);
        this.updateLayer.updateNode(1, 1);
    }

    public void updateQuad(int x, int y) {
        Pair nodeToUpdate = this.updateLayer.getNodeForPixelPosition(x, y);
        this.updateLayer.updateNode(nodeToUpdate.x, nodeToUpdate.y);
    }

}
