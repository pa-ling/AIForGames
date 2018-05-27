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
    private final static int PROPERTY_BOUND = 10000;
    private final static int CANDIDATES_TO_SELECT = 3;

    public static void main(String[] args) throws InterruptedException, IOException {
        AssessmentTraining assessmentTraining = new AssessmentTraining(NAMES, CONFIGS, LOGO_PATH);
        // create random candidates
        List<AssessmentConfig> candidates = AssessmentTraining.createCandidates(CANDIDATES_COUNT, PROPERTY_BOUND);
        System.out.println("Random candidates:" + candidates);
        // get currently best configuration
        candidates.add(AssessedMoveStrategy.readConfig(BEST_CONFIG));
        do {
            // find out what are the 3 best configs
            Map<AssessmentConfig, Long> evaluatedCandidates = assessmentTraining.evaluateCandidates(candidates);
            System.out.println("Evaluated candidates:" + evaluatedCandidates);
            candidates = assessmentTraining.selectCandidates(evaluatedCandidates, CANDIDATES_TO_SELECT);
            System.out.println("Selected candidates:" + candidates);
            // save the best config
            AssessmentTraining.writeConfig(candidates.get(0), BEST_CONFIG);
            // recombinate new candidates out of the best 3
            candidates = assessmentTraining.recombineCandidates(candidates, CANDIDATES_COUNT);
            System.out.println("Recombined candidates:" + candidates);
            // mutate some aspects of the new candidates
            candidates = assessmentTraining.mutateCandidates(candidates, PROPERTY_BOUND);
            System.out.println("Mutated candidates: " + candidates);
        } while (true);

    }

}