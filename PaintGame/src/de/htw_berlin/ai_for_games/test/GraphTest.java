package de.htw_berlin.ai_for_games.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.htw_berlin.ai_for_games.pathfinding.Graph;
import de.htw_berlin.ai_for_games.pathfinding.Node;

class GraphTest {

    @Test
    void testMaingraphConstruction() {
        Graph graph = new Graph();
        // assert that graph was build correctly by getting an element
        assertEquals(graph.getNode(3, 3), new Node(3, 3));
    }

}
