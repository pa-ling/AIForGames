package de.htw_berlin.ai_for_games.pathfinding;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.Pair;
import lenz.htw.zpifub.net.NetworkClient;

public class Layer {

    static final int ROCK_BOTTOM_LAYER_NUMBER = 10;

    public final int size;

    private final int number;
    private final int nodeSize;
    private final int[][] nodes;
    private final Layer bottomLayer;

    private final NetworkClient networkClient;
    private final Color playerColor;

    public Layer(int number, Color playerColor, Layer bottomLayer) {
        this.bottomLayer = bottomLayer;
        this.number = number;
        this.size = (int) Math.pow(2, this.number);
        this.nodeSize = (int) Math.pow(2, Layer.ROCK_BOTTOM_LAYER_NUMBER - this.number);
        this.nodes = new int[this.size][this.size];
        this.playerColor = playerColor;
        this.networkClient = null;

        // System.out.println(this.number + "{size: " + this.size + ", nodeSize:" +
        // this.nodeSize + "}");
    }

    public Layer(int number, Color playerColor, NetworkClient networkClient) {
        this.bottomLayer = null;
        this.number = number;
        this.size = 1024;
        this.nodeSize = 1;
        this.nodes = null;
        this.playerColor = playerColor;
        this.networkClient = networkClient;

        // System.out.println(this.number + "{size: " + this.size + ", nodeSize:" +
        // this.nodeSize + "}");
    }

    public int getCost(int x, int y) {
        int targetNodeValue = this.nodes[x][y];

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

    public int getCostWithoutColors(int x, int y) {
        int targetNodeValue = this.nodes[x][y];
        if (targetNodeValue == Color.BLACK.intValue) {
            // leave room for heuristic
            return Integer.MAX_VALUE - 5000;
        }

        return 1;
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

    public Pair getNodePositionWithLowestCosts(int layerNumber) {
        Pair currentTargetWithLowestCost = new Pair(0, 0);
        int currentLowestCost = Integer.MAX_VALUE;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int currentCost = getCost(i, j);
                if (currentLowestCost > currentCost) {
                    currentLowestCost = currentCost;
                    currentTargetWithLowestCost = new Pair(i, j);
                }
            }
        }

        if (this.number == layerNumber) {
            return currentTargetWithLowestCost;
        }

        return this.bottomLayer.getNodePositionWithLowestCostsRecursive(
                getSubnodesPositions(currentTargetWithLowestCost.x, currentTargetWithLowestCost.y), layerNumber);

    }

    private Pair getNodePositionWithLowestCostsRecursive(List<Pair> nodePositions, int layerNumber) {
        Pair currentTargetWithLowestCost = new Pair(0, 0);
        int currentLowestCost = Integer.MAX_VALUE;
        for (Pair nodePosition : nodePositions) {
            int currentCost = getCost(nodePosition.x, nodePosition.y);
            if (currentLowestCost > currentCost) {
                currentLowestCost = currentCost;
                currentTargetWithLowestCost = nodePosition;
            }
        }

        if (this.number == layerNumber) {
            return currentTargetWithLowestCost;
        }

        return this.bottomLayer.getNodePositionWithLowestCostsRecursive(
                getSubnodesPositions(currentTargetWithLowestCost.x, currentTargetWithLowestCost.y), layerNumber);

    }

    public int getNumber() {
        return this.number;
    }

    public Pair getPixelPositionForNode(int x, int y) {
        int layerDifference = 10 - this.number;
        int power = (int) Math.pow(2, layerDifference);
        return new Pair(x * power + nodeSize / 2, y * power + nodeSize / 2);
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
