package de.htw_berlin.ai_for_games.Bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.Pair;
import de.htw_berlin.ai_for_games.pathfinding.AStar;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;

public abstract class Bot {

    private Pair currentPosition;

    private final BotType botType;

    private final QuadTree quadTree;

    private final Queue<Pair> path;

    private final Queue<Pair> pathToItem;

    private boolean chasesItem;

    public Bot(final BotType botType, final QuadTree quadTree) {
        this.botType = botType;
        this.quadTree = quadTree;

        this.path = new LinkedList<>();
        this.pathToItem = new LinkedList<>();
    }

    private void calculatePath(int x, int y) {
        List<Pair> newPath = AStar.getPathConsideringColors(this.quadTree.getPathLayer(), this.currentPosition,
                new Pair(x, y));
        this.path.clear();
        this.path.addAll(newPath);
    }

    private boolean checkPointReached(int x, int y) {
        
        if (this.currentPosition.x == x && this.currentPosition.y == y) {
            return true;
        }
        
        for (Pair neighbour : quadTree.getPathLayer().getNeighbors(x, y)) {
            if (this.currentPosition.x == neighbour.x && this.currentPosition.y == neighbour.y) {
                return true;
            }
        }

        return false;
    }

    public void findNextTargetAndCalculatePath() {
        Pair targetNode = this.quadTree.getTargetOnPathLayer();
        calculatePath(targetNode.x, targetNode.y);
    }

    public int getBotNumber() {
        return this.botType.number;
    }

    public BotType getBotType() {
        return this.botType;
    }

    public int getDistanceToItem(int x, int y) {
        List<Pair> newPath = AStar.getPathWithoutConsideringColors(this.quadTree.getPathLayer(), this.currentPosition,
                new Pair(x, y));

        // cache this path in case we need to use it later
        this.pathToItem.clear();
        this.pathToItem.addAll(newPath);

        return this.pathToItem.size();
    }

    public Pair getNextDirection() {
        Pair nextNode = this.path.peek();

        if (nextNode == null || checkPointReached(nextNode.x, nextNode.y)) {
            this.path.poll();
            if (this.path.isEmpty()) {
                if (this.chasesItem) {
                    this.chasesItem = false;
                    System.out.println("Bot reached item.");
                }
                findNextTargetAndCalculatePath();
            }
            nextNode = this.path.poll();
        }

        return new Pair(nextNode.x - this.currentPosition.x, nextNode.y - this.currentPosition.y);
    }

    public boolean isChasesItem() {
        return this.chasesItem;
    }

    public void removeItemAsTargetAndFindNextTarget() {
        this.chasesItem = false;
        findNextTargetAndCalculatePath();
    }

    public void setItemAsTarget() {
        this.chasesItem = true;
        this.path.clear();
        this.path.addAll(this.pathToItem);
    }

    public void updatePosition(int x, int y) {
        this.currentPosition = this.quadTree.getPathLayer().getNodeForPixelPosition(x, y);
    }

}
