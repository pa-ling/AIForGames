package de.htw_berlin.ai_for_games.Bot;

import de.htw_berlin.ai_for_games.pathfinding.board.QuadTree;

public class SprayCanBot extends Bot {

    public SprayCanBot(QuadTree quadTree) {
        super(BotType.SPRAY_CAN, quadTree);
    }

}
