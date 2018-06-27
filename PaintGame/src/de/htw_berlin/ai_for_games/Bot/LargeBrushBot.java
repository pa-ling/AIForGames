package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.pathfinding.Graph;
import de.htw_berlin.ai_for_games.pathfinding.PathfindingStrategy;
import de.htw_berlin.ai_for_games.pathfinding.TargetLocationStrategy;

public class LargeBrushBot extends Bot {

    public LargeBrushBot(TargetLocationStrategy targetLocationStrategy, PathfindingStrategy pathfindingStrategy,
            Graph obstacleGraph, Graph colorGraph) {
        super(BotType.LARGE_BRUSH, targetLocationStrategy, pathfindingStrategy, obstacleGraph, colorGraph);
    }

}
