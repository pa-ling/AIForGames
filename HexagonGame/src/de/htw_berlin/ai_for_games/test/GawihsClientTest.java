package de.htw_berlin.ai_for_games.test;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class GawihsClientTest {

    private final String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    // private final static String[] CONFIGS = { "res/config.json",
    // "res/config.json", "res/config.json" };
    private final String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final String LOGO_PATH = "res/claptrap.png";

    @Test
    public void testGame() throws InterruptedException, IOException {
        Map<String, Integer> scores = AssessmentTraining.playAGame(this.NAMES, this.CONFIGS, this.LOGO_PATH);
        System.out.println("Score: " + scores);
    }

}