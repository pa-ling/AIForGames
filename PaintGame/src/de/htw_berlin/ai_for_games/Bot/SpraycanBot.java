package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.pathfinding.PathfindingStrategy;
import de.htw_berlin.ai_for_games.pathfinding.TargetLocationStrategy;

public class SpraycanBot extends Bot {

    public SpraycanBot(TargetLocationStrategy targetLocationStrategy, PathfindingStrategy pathfindingStrategy,
            BoardInterface obstacleGraph, BoardInterface colorGraph) {
        super(BotType.SPRAYCAN, targetLocationStrategy, pathfindingStrategy, obstacleGraph, colorGraph);
    }

}
