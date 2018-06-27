package de.htw_berlin.ai_for_games.pathfinding;

/**
 * Represents a node of the board.
 *
 * @author Maximilian Schulze
 *
 */
public class Node {
    public static final int OBSTACLE_VALUE = Integer.MAX_VALUE;

    final int x;

    final int y;

    int value;

    public Node(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object obj) {
        // two nodes are equal if their coordinates are equal
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    public int getCost(final Node targetNode, final int modifier) {
        // sanity check
        if (targetNode.x != this.x && targetNode.x != this.x + 1 && targetNode.x != this.x - 1
                || targetNode.y != this.y && targetNode.y != this.y + 1 && targetNode.y != this.y - 1) {
            throw new IllegalStateException(String.format(
                    "Cannot calculate cost. The node %d, %d is not a direct neighbor of this node (%d, %d).",
                    targetNode.x, targetNode.y, this.x, this.y));
        }

        // cost to oneself is zero
        if (targetNode.equals(this)) {
            return 0;
        }

        // TODO this needs work
        return modifier * targetNode.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.x;
        result = prime * result + this.y;
        return result;
    }

}
