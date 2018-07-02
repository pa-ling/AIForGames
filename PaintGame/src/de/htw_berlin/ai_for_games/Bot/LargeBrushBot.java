package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.pathfinding.board.QuadTree;

public class LargeBrushBot extends Bot {

    public LargeBrushBot(QuadTree quadTree) {
        super(BotType.LARGE_BRUSH, quadTree);
    }

}
