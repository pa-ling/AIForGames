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

    private QuadTree quadTree;

    private final Queue<Pair> path;

    private final Queue<Pair> priorityPath;

    public Bot(final BotType botType, final QuadTree quadTree) {
        this.botType = botType;
        this.quadTree = quadTree;

        this.path = new LinkedList<>();
        this.priorityPath = new LinkedList<>();
    }

    public void deletePriorityTarget() {
        this.priorityPath.clear();
    }

    public void findNextTarget() {
        Pair targetNode = this.quadTree.getTargetOnPathLayer();
        List<Pair> newPath = new AStar().getPath(this.quadTree.getPathLayer(), currentPosition, targetNode);
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
        final Queue<Pair> pathToUse = this.priorityPath.isEmpty() ? this.path : this.priorityPath;
        Pair nextNode = pathToUse.poll();
        if (nextNode.equals(this.currentPosition)) {
            nextNode = pathToUse.poll();
        }

        return new Pair(nextNode.x - currentPosition.x, nextNode.y - currentPosition.y);
    }

    public boolean pathQueuesAreEmpty() {
        return this.priorityPath.isEmpty() && this.path.isEmpty();
    }

    public void setPriorityTarget(final Update update) {
        // TODO: Implement
    }

    public void updatePosition(int x, int y) {
        this.currentPosition = this.quadTree.getPathLayer().getNodeForPixelPosition(x, y);
    }

}
