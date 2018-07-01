package de.htw_berlin.ai_for_games.pathfinding;

/**
 * Represents a node of the board.
 *
 * @author Maximilian Schulze
 *
 */
public class Node {

    public final int x;
    public final int y;
    public int value;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = Color.WHITE.intValue;
    }

    public Node(int x, int y, int value) {
        this(x, y);
        this.value = value;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.x;
        result = prime * result + this.y;
        return result;
    }

}
