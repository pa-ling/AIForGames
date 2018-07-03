package de.htw_berlin.ai_for_games.pathfinding;

import java.util.HashSet;
import java.util.Set;

import de.htw_berlin.ai_for_games.Pair;

public class PathLayer extends Layer {

    private final Set<Pair> obstacles;
    private boolean initalized;

    public PathLayer(int number, Color playerColor, Layer bottomLayer) {
        super(number, playerColor, bottomLayer);
        this.obstacles = new HashSet<>();
    }

    @Override
    public int updateNode(int x, int y) {
        if (!initalized) {
            int nodeValue = super.updateNode(x, y);
            int blueValue = nodeValue & 0xFF;
            int greenValue = nodeValue >> 8 & 0xFF;
            int redValue = nodeValue >> 16 & 0xFF;

            if (nodeValue != Color.WHITE.intValue && blueValue == greenValue && greenValue == redValue) {
                this.obstacles.add(new Pair(x, y));
            }
        }
        return super.updateNode(x, y);
    }

    @Override
    public int getCost(int x, int y) {
        if (this.obstacles.contains(new Pair(x, y))) {
            // this node contains an obstacle - avoid!
            return Integer.MAX_VALUE - 5000;
        }
        return super.getCost(x, y);
    }

    @Override
    public int getCostWithoutColors(int x, int y) {
        if (this.obstacles.contains(new Pair(x, y))) {
            // leave room for heuristic
            return Integer.MAX_VALUE - 5000;
        }

        return 1;
    }

    public void removeObstacle(int x, int y) {
        this.obstacles.remove(new Pair(x, y));
    }

    public void addObstacle(int x, int y) {
        this.obstacles.add(new Pair(x, y));
    }

    public void setInitalized(boolean initalized) {
        this.initalized = initalized;
    }

}
