package de.htw_berlin.ai_for_games.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.htw_berlin.ai_for_games.player.strategies.AssessedMoveStrategy;
import de.htw_berlin.ai_for_games.player.strategies.AssessmentConfig;

class ExecuteAssessmentTraining {

    private final static String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final static String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final static String BEST_CONFIG = "res/config.json";
    private final static String LOGO_PATH = "res/claptrap.png";

    private final static int CANDIDATES_COUNT = 3;
    private final static int CANDIDATES_TO_SELECT = 3;

    public static void main(String[] args) throws InterruptedException, IOException {
        AssessmentTraining assessmentTraining = new AssessmentTraining(NAMES, CONFIGS, LOGO_PATH);
        // create random candidates
        List<AssessmentConfig> candidates = AssessmentTraining.createCandidates(CANDIDATES_COUNT);
        // get currently best configuration
        candidates.add(AssessedMoveStrategy.readConfig(BEST_CONFIG));
        // find out what are the 3 best configs
        Map<AssessmentConfig, Long> evaluatedCandidates = assessmentTraining.evaluateCandidates(candidates);
        System.out.println(evaluatedCandidates);
        candidates = assessmentTraining.selectCandidates(evaluatedCandidates, CANDIDATES_TO_SELECT);
        // save the best config
        AssessmentTraining.writeConfig(candidates.get(0), BEST_CONFIG);
        // recombinate new candidates out of the best 3
        // mutate some aspects of the new candidates
        System.out.println(candidates);
    }

}