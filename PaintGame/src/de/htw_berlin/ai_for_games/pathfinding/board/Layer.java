package de.htw_berlin.ai_for_games.pathfinding.board;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.pathfinding.Color;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class Layer {

    private final int number;
    private final int size;
    private final int[][] nodes;
    private final Layer bottomLayer;

    private final NetworkClient networkClient;
    private final Color playerColor;

    public Layer(int number, Color playerColor, Layer bottomLayer) {
        this.bottomLayer = bottomLayer;
        this.number = number;
        this.size = (int) Math.pow(2, this.number + 1);
        this.nodes = new int[this.size][this.size];
        this.playerColor = playerColor;
        this.networkClient = null;
    }

    public Layer(int number, Color playerColor, NetworkClient networkClient) {
        this.bottomLayer = null;
        this.number = number;
        this.size = 1024;
        this.nodes = null;
        this.playerColor = playerColor;
        this.networkClient = networkClient;
    }

    public int getCost(int x, int y) {
        int targetNodeValue = this.nodes[x][y];

        if (targetNodeValue == Color.WHITE.intValue) {
            return 310;
        }

        if (targetNodeValue == Color.BLACK.intValue) {
            // leave room for heuristic
            return Integer.MAX_VALUE - 5000;
        }

        int blueValue = targetNodeValue & 0xFF;
        int greenValue = targetNodeValue >> 8 & 0xFF;
        int redValue = targetNodeValue >> 16 & 0xFF;

        int costs = 510;
        if (this.playerColor == Color.RED) { // we are red
            costs += redValue;
            costs -= greenValue;
            costs -= blueValue;
        } else if (this.playerColor == Color.GREEN) { // we are green
            costs -= redValue;
            costs += greenValue;
            costs -= blueValue;
        } else if (this.playerColor == Color.BLUE) { // we are blue
            costs -= redValue;
            costs -= greenValue;
            costs += blueValue;
        }

        return costs;
    }

    public List<Pair> getNeighbors(int x, int y) {
        List<Pair> neighbours = new ArrayList<>(8);
        for (int i = -1; i <= 1; i++) {

            int neighbourX = x + i;
            if (neighbourX < 0 || neighbourX >= this.size) { // OutOfBounds check
                continue;
            }

            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int neighbourY = y + j;

                if (neighbourY < 0 || neighbourY >= this.size) { // OutOfBounds check
                    continue;
                }

                neighbours.add(new Pair(neighbourX, neighbourY));
            }
        }
        return neighbours;
    }

    public Pair getNodeForPixelPosition(int x, int y) {
        int layerDifference = 10 - this.number;
        int power = (int) Math.pow(2, layerDifference);
        return new Pair(x / power, y / power);
    }

    public Pair getNodePositionWithHighestCosts(int layerNumber) {
        Pair currentTargetWithHighestCost = new Pair(0, 0);
        int currentHighestCost = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int currentCost = getCost(i, j);
                if (currentHighestCost < currentCost) {
                    currentHighestCost = currentCost;
                    currentTargetWithHighestCost = new Pair(i, j);
                }
            }
        }

        if (this.number == layerNumber) {
            return currentTargetWithHighestCost;
        }

        return this.bottomLayer.getNodePositionWithHighestCostsRecursive(
                getSubnodesPositions(currentTargetWithHighestCost.x, currentTargetWithHighestCost.y), layerNumber);

    }

    private Pair getNodePositionWithHighestCostsRecursive(List<Pair> nodePositions, int layerNumber) {
        Pair currentTargetWithHighestCost = new Pair(0, 0);
        int currentHighestCost = 0;
        for (Pair nodePosition : nodePositions) {
            int currentCost = getCost(nodePosition.x, nodePosition.y);
            if (currentHighestCost < currentCost) {
                currentHighestCost = currentCost;
                currentTargetWithHighestCost = nodePosition;
            }
        }

        if (this.number == layerNumber) {
            return currentTargetWithHighestCost;
        }

        return this.bottomLayer.getNodePositionWithHighestCostsRecursive(
                getSubnodesPositions(currentTargetWithHighestCost.x, currentTargetWithHighestCost.y), layerNumber);

    }

    public int getNumber() {
        return this.number;
    }

    public Pair getPixelPositionForNode(int x, int y) {
        int layerDifference = 10 - this.number;
        int power = (int) Math.pow(2, layerDifference);
        // TODO: get center of node
        return new Pair(x * power, y * power);
    }

    private List<Pair> getSubnodesPositions(int x, int y) {
        List<Pair> subnodes = new ArrayList<Pair>(4);

        if (this.bottomLayer == null) {
            return subnodes;
        }

        int rootX = x * 2;
        int rootY = y * 2;

        subnodes.add(new Pair(rootX, rootY));
        subnodes.add(new Pair(rootX + 1, rootY));
        subnodes.add(new Pair(rootX, rootY + 1));
        subnodes.add(new Pair(rootX + 1, rootY + 1));

        return subnodes;
    }

    public void removeObstacleItem(Update update) {
        // TODO Auto-generated method stub
    }

    public void setItemAsObstacle(Update update) {
        // TODO Auto-generated method stub
    }

    public int updateNode(int x, int y) {
        if (this.bottomLayer == null) {
            return this.networkClient.getBoard(x, y);
        }

        List<Pair> subnodesIndices = getSubnodesPositions(x, y);
        int nodeValue = 0;
        for (Pair subnode : subnodesIndices) {
            nodeValue += this.bottomLayer.updateNode(subnode.x, subnode.y);
        }
        nodeValue = nodeValue / subnodesIndices.size();

        this.nodes[x][y] = nodeValue;

        return nodeValue;
    }

}
