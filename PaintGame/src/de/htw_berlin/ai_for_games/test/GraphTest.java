package de.htw_berlin.ai_for_games.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import de.htw_berlin.ai_for_games.pathfinding.Graph;
import de.htw_berlin.ai_for_games.pathfinding.Maingraph;
import de.htw_berlin.ai_for_games.pathfinding.Node;

class GraphTest {

    @Test
    void testMaingraphConstruction() {
        Graph graph = new Maingraph();
        // assert that graph was build correctly by getting an element
        assertEquals(graph.getNode(3, 3), new Node(3, 3));
    }

    @Test
    void testSubgraphConstruction() {
        // build subgraph from maingraph
        Graph subgraph = new Maingraph().createSubgraph(1, 1, 8, 8);

        // Get element inside subgraph
        assertEquals(subgraph.getNode(3, 3), new Node(3, 3));

        // Get element outside graph - should be null
        assertNull(subgraph.getNode(0, 0));
    }

    @Test
    void testSubgraphConstructionWithSubgraphCoordinatesOutsideMaingraphRange() {
        // build subgraph from maingraph
        Graph subgraph = new Maingraph().createSubgraph(1, 1, 12, 12);

        // Get element inside subgraph
        assertEquals(subgraph.getNode(3, 3), new Node(3, 3));

        // Get element outside graph - should be null
        assertNull(subgraph.getNode(0, 0));
    }

}
