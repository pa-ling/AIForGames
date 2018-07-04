package de.htw_berlin.ai_for_games.Bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.Pair;
import de.htw_berlin.ai_for_games.pathfinding.AStar;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;
import lenz.htw.zpifub.Update;

public abstract class Bot {

    private Pair currentPosition;

    private final BotType botType;

    private final QuadTree quadTree;

    private final Queue<Pair> path;

    private final Queue<Pair> priorityPath;

    public Bot(final BotType botType, final QuadTree quadTree) {
        this.botType = botType;
        this.quadTree = quadTree;

        this.path = new LinkedList<>();
        this.priorityPath = new LinkedList<>();
    }

    private boolean checkPointReached(int x, int y) {
        boolean xReached = this.currentPosition.x == x;
        boolean yReached = this.currentPosition.y == y;

        if (xReached && yReached) {
            return true;
        }

        return false;
    }

    public void deletePriorityTarget() {
        this.priorityPath.clear();
    }

    public void findNextTarget() {
        Pair targetNode = this.quadTree.getTargetOnPathLayer();
        List<Pair> newPath = AStar.getPathConsideringColors(this.quadTree.getPathLayer(), this.currentPosition,
                targetNode);
        this.path.clear();
        this.path.addAll(newPath);
    }

    public int getBotNumber() {
        return this.botType.number;
    }

    public Pair getCurrentPosition() {
        return this.currentPosition;
    }

    public Pair getNextDirection() {
        Pair nextNode = this.path.peek();

        if (checkPointReached(this.currentPosition.x, this.currentPosition.y)) {
            this.path.poll();
            if (this.path.isEmpty()) {
                findNextTarget();
            }
            nextNode = this.path.poll();
        }

        return new Pair(nextNode.x - this.currentPosition.x, nextNode.y - this.currentPosition.y);
    }

    public void setPriorityTarget(final Update update) {
        // TODO: Implement
    }

    public void updatePosition(int x, int y) {
        this.currentPosition = this.quadTree.getPathLayer().getNodeForPixelPosition(x, y);
    }

}
