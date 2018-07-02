package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.pathfinding.QuadTree;

public class SmallBrushBot extends Bot {

    public SmallBrushBot(QuadTree quadTree) {
        super(BotType.SMALL_BRUSH, quadTree);
    }

}
