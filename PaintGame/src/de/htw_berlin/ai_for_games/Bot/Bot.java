package de.htw_berlin.ai_for_games.Bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.Direction;
import de.htw_berlin.ai_for_games.pathfinding.AStarSearch;
import de.htw_berlin.ai_for_games.pathfinding.Node;
import de.htw_berlin.ai_for_games.pathfinding.PathfindingStrategy;
import de.htw_berlin.ai_for_games.pathfinding.TargetLocationStrategy;
import lenz.htw.zpifub.Update;

public abstract class Bot {

    private Node currentPosition;

    private final BotType botType;

    private final TargetLocationStrategy targetLocationStrategy;

    private final PathfindingStrategy pathfindingStrategy;

    private final BoardInterface obstacleGraph;

    private final BoardInterface colorGraph;

    private final Queue<Node> path;

    private final Queue<Node> priorityPath;

    public Bot(final BotType botType, final TargetLocationStrategy targetLocationStrategy,
            final PathfindingStrategy pathfindingStrategy, final BoardInterface obstacleGraph,
            final BoardInterface colorGraph) {
        this.botType = botType;
        this.targetLocationStrategy = targetLocationStrategy;
        this.pathfindingStrategy = pathfindingStrategy;
        this.obstacleGraph = obstacleGraph;
        this.colorGraph = colorGraph;

        this.path = new LinkedList<>();
        this.priorityPath = new LinkedList<>();
    }

    public void deletePriorityTarget() {
        this.priorityPath.clear();
    }

    public void findNextTarget() {
        final Node targetNode = this.targetLocationStrategy.getNextTarget(this.colorGraph, this.botType);
        final List<Node> newPath = this.pathfindingStrategy.getPath(this.colorGraph, this.currentPosition, targetNode,
                this.botType);
        this.path.clear();
        this.path.addAll(newPath);
    }

    public int getBotNumber() {
        return this.botType.number;
    }

    public Node getCurrentPosition() {
        return this.currentPosition;
    }

    public Direction getNextDirection() {
        final Queue<Node> pathToUse = this.priorityPath.isEmpty() ? this.path : this.priorityPath;
        Node nextNode = pathToUse.poll();
        if (nextNode.equals(this.currentPosition)) {
            nextNode = pathToUse.poll();
        }

        // TODO use position and nextNode to calculate direction
        return new Direction(0, 0);
    }

    public boolean pathQueuesAreEmpty() {
        return this.priorityPath.isEmpty() && this.path.isEmpty();
    }

    public void setCurrentPosition(final Node currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setPriorityTarget(final Update update) {
        final Node targetNode = this.obstacleGraph.getNodeForPixelPosition(update.x, update.y);
        final List<Node> newPath = new AStarSearch().getPath(this.obstacleGraph, this.currentPosition, targetNode,
                this.botType);
        this.priorityPath.clear();
        this.priorityPath.addAll(newPath);
    }

    public void updatePosition(final Update update) {
        this.currentPosition = this.obstacleGraph.getNodeForPixelPosition(update.x, update.y);
    }

}
