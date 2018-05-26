package de.htw_berlin.ai_for_games.test;

import java.io.IOException;

class ExecuteAssessmentTraining {

    private final static String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final static String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final static String BEST_CONFIG = "res/config.json";
    private final static String LOGO_PATH = "res/claptrap.png";

    private final static int CANDIDATES_COUNT = 20;

    public static void main(String[] args) throws InterruptedException, IOException {
        // create random candidates
        // get currently best configuration from res/config.json
        // find out what are the 3 best configs
        // save the best config to res/config.json
        // recombinate new candidates out of the best 3
        // mutate some aspects of the new candidates
        System.out.println(AssessmentTraining.playAGame(NAMES, CONFIGS, LOGO_PATH));
    }

}