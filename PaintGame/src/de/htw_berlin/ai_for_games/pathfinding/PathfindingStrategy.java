package de.htw_berlin.ai_for_games.pathfinding;

import java.util.List;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;

public interface PathfindingStrategy {

    /**
     * Calculates the optimal path to the target node using the supplied graph.
     *
     * @param graph
     *            {@link Graph} to use for target search
     * @param targetNode
     *            {@link Node target node} for the search
     * @param bot
     *            the {@link BotType type of the bot} to calculate the target for
     * @return a List containing all nodes which must be passed in order to reach
     *         the target or {@code null} if no path was found.
     */
    public List<Node> getPath(BoardInterface graph, Node startNode, Node targetNode, BotType bot);
}
