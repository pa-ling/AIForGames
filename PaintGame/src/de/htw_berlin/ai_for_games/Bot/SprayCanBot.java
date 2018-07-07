package de.htw_berlin.ai_for_games.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.htw_berlin.ai_for_games.Pair;
import de.htw_berlin.ai_for_games.pathfinding.AStar;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;

public class SprayCanBot extends Bot {
    
    private static final int SECTION_SIZE = 5;

    public SprayCanBot(QuadTree quadTree) {
        super(BotType.SPRAY_CAN, quadTree);
    }
    
    @Override
    protected void calculatePath(int x, int y) {
        List<Pair> newPath = AStar.getPathConsideringColors(this.quadTree.getPathLayer(), this.currentPosition,
                new Pair(x, y));
        newPath.remove(0);
        
        for (int i = 0; i < newPath.size() - SECTION_SIZE; i++) {
            List<Pair> sectionToAdd = new ArrayList<>();
            
            for (int j = 0; j < SECTION_SIZE; j++) {
                sectionToAdd.add(newPath.get(i + j));
            }
            
            Collections.reverse(sectionToAdd);
            newPath.addAll(SECTION_SIZE + i , sectionToAdd);
            Collections.reverse(sectionToAdd);
            newPath.addAll(2 * SECTION_SIZE + i , sectionToAdd);
            i += 2 * SECTION_SIZE + 2;
        }
        
        this.path.clear();
        this.path.addAll(newPath);
    }

}
