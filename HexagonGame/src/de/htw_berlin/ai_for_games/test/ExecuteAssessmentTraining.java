package de.htw_berlin.ai_for_games.test;

import java.io.IOException;

class ExecuteAssessmentTraining {

    private final static String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final static String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final static String LOGO_PATH = "res/claptrap.png";

    private final static int CANDIDATES_COUNT = 21;

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(AssessmentTraining.playAGame(NAMES, CONFIGS, LOGO_PATH));
    }

}