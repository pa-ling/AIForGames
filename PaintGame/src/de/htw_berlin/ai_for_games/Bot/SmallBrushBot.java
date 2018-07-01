package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.BotType;
import de.htw_berlin.ai_for_games.pathfinding.board.QuadTree;

public class SmallBrushBot extends Bot {

    public SmallBrushBot(QuadTree quadTree) {
        super(BotType.SMALL_BRUSH, quadTree);
    }

}
