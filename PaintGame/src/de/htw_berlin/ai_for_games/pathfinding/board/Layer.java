package de.htw_berlin.ai_for_games.pathfinding.board;

import java.util.ArrayList;
import java.util.List;

import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.Node;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class Layer {

    private final int number;
    private final int size;
    private int[][] nodes;
    private Layer bottomLayer;

    private NetworkClient networkClient;
    private Color playerColor;

    public Layer(int number, Color playerColor, NetworkClient networkClient) {
        this.bottomLayer = null;
        this.number = number;
        this.size = 1024;
        this.nodes = null;
        this.playerColor = playerColor;
        this.networkClient = networkClient;
    }

    public Layer(int number, Color playerColor, Layer bottomLayer) {
        this.bottomLayer = bottomLayer;
        this.number = number;
        this.size = (int) Math.pow(2, this.number + 1);
        this.nodes = new int[size][size];
        this.playerColor = playerColor;
        this.networkClient = null;
    }

    public int getNumber() {
        return this.number;
    }

    public int getCost(int x, int y) {
        int targetNodeValue = nodes[x][y];
        int blueValue = targetNodeValue & 0xFF;
        int greenValue = (targetNodeValue >> 8) & 0xFF;
        int redValue = (targetNodeValue >> 16) & 0xFF;

        if (redValue == 255 && greenValue == 255 && blueValue == 255) { // Field is white
            return 128;
        }

        int costs = 510;
        if (playerColor == Color.RED) { // we are red
            costs += redValue;
            costs -= greenValue;
            costs -= blueValue;
        } else if (playerColor == Color.GREEN) { // we are green
            costs -= redValue;
            costs += greenValue;
            costs -= blueValue;
        } else if (playerColor == Color.BLUE) { // we are blue
            costs -= redValue;
            costs -= greenValue;
            costs += blueValue;
        }

        return costs;
    }

    public List<Node> getNeighbors(int x, int y) {
        List<Node> neighbours = new ArrayList<>(8);
        for (int i = -1; i <= 1; i++) {

            int neighbourX = x + i;
            if (neighbourX < 0 || neighbourX > this.size) { // OutOfBounds check
                continue;
            }

            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                int neighbourY = y + j;

                if (neighbourY < 0 || neighbourY > this.size) { // OutOfBounds check
                    continue;
                }

                neighbours.add(new Node(neighbourX, neighbourY, nodes[neighbourX][neighbourY]));
            }
        }
        return neighbours;
    }

    public Node getNodeForPixelPosition(int x, int y) {
        return new Node(x / (size / 2), y / (size / 2)); // ???
    }

    public void removeObstacleItem(Update update) {
        // TODO Auto-generated method stub
    }

    public void setItemAsObstacle(Update update) {
        // TODO Auto-generated method stub
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

        return bottomLayer.getNodePositionWithHighestCostsRecursive(
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

        return bottomLayer.getNodePositionWithHighestCostsRecursive(
                getSubnodesPositions(currentTargetWithHighestCost.x, currentTargetWithHighestCost.y), layerNumber);

    }

    private List<Pair> getSubnodesPositions(int x, int y) {
        List<Pair> subnodes = new ArrayList<Pair>(4);

        if (bottomLayer == null) {
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
        if (bottomLayer == null) {
            return networkClient.getBoard(x, y);
        }

        List<Pair> subnodesIndices = getSubnodesPositions(x, y);
        int nodeValue = 0;
        for (Pair subnode : subnodesIndices) {
            nodeValue += bottomLayer.updateNode(subnode.x, subnode.y);
        }
        nodeValue = nodeValue / subnodesIndices.size();

        this.nodes[x][y] = nodeValue;

        return nodeValue;
    }

}
