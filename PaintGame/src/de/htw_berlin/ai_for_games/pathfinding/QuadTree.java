package de.htw_berlin.ai_for_games.pathfinding;

import de.htw_berlin.ai_for_games.Pair;
import lenz.htw.zpifub.net.NetworkClient;

public class QuadTree {

    private static final int PATH_LAYER_NUMBER = 6;
    private static final int UPDATE_LAYER_NUMBER = 2;

    private PathLayer pathLayer;
    private Layer updateLayer;

    public QuadTree(Color playerColor, NetworkClient networkClient) {
        Layer currentLayer = new Layer(Layer.ROCK_BOTTOM_LAYER_NUMBER, playerColor, networkClient);
        for (int i = Layer.ROCK_BOTTOM_LAYER_NUMBER - 1; i >= UPDATE_LAYER_NUMBER; i--) {
            if (i == PATH_LAYER_NUMBER) {
                this.pathLayer = new PathLayer(i, playerColor, currentLayer);
                currentLayer = this.pathLayer;
            } else {
                currentLayer = new Layer(i, playerColor, currentLayer);
                if (i == UPDATE_LAYER_NUMBER) {
                    this.updateLayer = currentLayer;
                }
            }

        }
    }

    public PathLayer getPathLayer() {
        return this.pathLayer;
    }

    public Pair getTargetOnPathLayer() {
        return this.updateLayer.getNodePositionWithHighestCosts(this.pathLayer.getNumber());
    }

    public void initNodes() {
        for (int i = 0; i < this.updateLayer.size; i++) {
            for (int j = 0; j < this.updateLayer.size; j++) {
                this.updateLayer.updateNode(i, j);
            }
        }
        this.pathLayer.setInitalized(true);
    }

    public void updateQuad(int x, int y) {
        Pair nodeToUpdate = this.updateLayer.getNodeForPixelPosition(x, y);
        this.updateLayer.updateNode(nodeToUpdate.x, nodeToUpdate.y);
    }

    public void removeObstacleFromPathLayer(int x, int y) {
        Pair obstacle = this.pathLayer.getNodeForPixelPosition(x, y);
        pathLayer.removeObstacle(obstacle.x, obstacle.y);
    }

    public void addObstacleToPathLayer(int x, int y) {
        Pair obstacle = this.pathLayer.getNodeForPixelPosition(x, y);
        pathLayer.addObstacle(obstacle.x, obstacle.y);
    }

}
