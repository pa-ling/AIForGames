package de.htw_berlin.ai_for_games.pathfinding.board;

import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.Node;
import lenz.htw.zpifub.net.NetworkClient;

public class QuadTree {

    private Layer[] layers;
    private Layer pathLayer;
    private Layer updateLayer;

    public QuadTree(Color playerColor, NetworkClient networkClient) {
        this.layers = new Layer[10];
        this.layers[9] = new Layer(9, playerColor, networkClient);
        Layer previousLayer = this.layers[9];
        for (int i = this.layers.length - 2; i >= 0; i--) {
            this.layers[i] = new Layer(i, playerColor, previousLayer);
            previousLayer = this.layers[i];
        }

        this.updateLayer = layers[0];
        this.pathLayer = layers[2];
    }

    public Layer getPathLayer() {
        return this.pathLayer;
    }

    public void updateQuad(int x, int y) {
        Node nodeToUpdate = updateLayer.getNodeForPixelPosition(x, y);
        updateLayer.updateNode(nodeToUpdate.x, nodeToUpdate.y);
    }

    public Pair getTargetOnPathLayer() {
        return updateLayer.getNodePositionWithHighestCosts(pathLayer.getNumber());
    }

}
