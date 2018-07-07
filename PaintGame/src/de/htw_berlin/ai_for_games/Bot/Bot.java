package de.htw_berlin.ai_for_games.Bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.htw_berlin.ai_for_games.Pair;
import de.htw_berlin.ai_for_games.pathfinding.AStar;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;

public abstract class Bot {
    
    private static final int IS_STUCK = 5;
    
    private int stuckCounter = 0;

    protected Pair currentPosition;
    
    private Pair lastPosition;
    
    private final BotType botType;

    protected final QuadTree quadTree;

    protected final Queue<Pair> path;

    private final Queue<Pair> pathToItem;

    private boolean chasesItem;

    public Bot(final BotType botType, final QuadTree quadTree) {
        this.botType = botType;
        this.quadTree = quadTree;

        this.path = new LinkedList<>();
        this.pathToItem = new LinkedList<>();
    }

    protected void calculatePath(int x, int y) {
        List<Pair> newPath = AStar.getPathConsideringColors(this.quadTree.getPathLayer(), this.currentPosition,
                new Pair(x, y));
        newPath.remove(0);
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
        
        List<Pair> newPath = AStar.getPathWithoutConsideringColors(
                this.quadTree.getPathLayer(), //
                this.currentPosition, //
                this.quadTree.getPathLayer().getNodeForPixelPosition(x, y) //
        );
        Pair lastNode = newPath.get(newPath.size() - 1);
        Pair secondToLastNode = newPath.get(newPath.size() - 2);
        Pair directionBetweenNodes = new Pair(lastNode.x - secondToLastNode.x, lastNode.y - secondToLastNode.y);
        newPath.add(lastNode.add(directionBetweenNodes)); //add another node to make sure we cross the item

        // cache this path in case we need to use it later
        this.pathToItem.clear();
        this.pathToItem.addAll(newPath);

        return this.pathToItem.size();
    }

    public Pair getNextDirection() {
        // emergency - calculate new direction if we're stuck
        if (this.currentPosition.equals(this.lastPosition)) {
            this.stuckCounter++;
            if (this.stuckCounter == IS_STUCK) {
                stuckCounter = 0;
                findNextTargetAndCalculatePath();
            }
        }
        
        Pair nextNode = this.path.peek();
        if (nextNode == null || checkPointReached(nextNode.x, nextNode.y)) {
            this.path.poll();
            if (this.path.isEmpty()) {
                if (this.chasesItem) {
                    //TODO: chase item until its gone, i.e. collect update is received
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
        this.lastPosition = this.currentPosition;
        this.currentPosition = this.quadTree.getPathLayer().getNodeForPixelPosition(x, y);
    }

}
