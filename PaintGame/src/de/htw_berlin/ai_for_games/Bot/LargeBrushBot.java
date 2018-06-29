package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.BoardInterface;
import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.pathfinding.PathfindingStrategy;
import de.htw_berlin.ai_for_games.pathfinding.TargetLocationStrategy;

public class LargeBrushBot extends Bot {

    public LargeBrushBot(TargetLocationStrategy targetLocationStrategy, PathfindingStrategy pathfindingStrategy,
            BoardInterface obstacleGraph, BoardInterface colorGraph) {
        super(BotType.LARGE_BRUSH, targetLocationStrategy, pathfindingStrategy, obstacleGraph, colorGraph);
    }

}
