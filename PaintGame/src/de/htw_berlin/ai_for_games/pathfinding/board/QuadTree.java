package de.htw_berlin.ai_for_games.pathfinding.board;

import de.htw_berlin.ai_for_games.pathfinding.Color;
import lenz.htw.zpifub.net.NetworkClient;

public class QuadTree {

    private final Layer[] layers;
    private final Layer pathLayer;
    private final Layer updateLayer;

    public QuadTree(Color playerColor, NetworkClient networkClient) {
        this.layers = new Layer[Layer.ROCK_BOTTOM_LAYER_NUMBER + 1];
        this.layers[Layer.ROCK_BOTTOM_LAYER_NUMBER] = new Layer(Layer.ROCK_BOTTOM_LAYER_NUMBER, playerColor,
                networkClient);
        Layer previousLayer = this.layers[Layer.ROCK_BOTTOM_LAYER_NUMBER];
        for (int i = this.layers.length - 2; i >= 0; i--) {
            this.layers[i] = new Layer(i, playerColor, previousLayer);
            previousLayer = this.layers[i];
        }

        this.updateLayer = this.layers[1]; // Layer 0 is never updated, but this is fine.
        this.pathLayer = this.layers[6];
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
