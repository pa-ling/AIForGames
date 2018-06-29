package de.htw_berlin.ai_for_games.pathfinding;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.Bot.Bot;

public interface TargetLocationStrategy {

    /**
     * Calculates the next possible target according to this strategy. Must always
     * return a target.
     *
     * @param graph
     *            graph to use in target calculation
     * @param bot
     *            {@link Bot bot} to calculate the target for
     * @return the {@link Node} where the target is located
     */
    public Node getNextTarget(BoardInterface graph, BotType bot);
}
