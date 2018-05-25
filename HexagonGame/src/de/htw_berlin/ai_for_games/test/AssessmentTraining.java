package de.htw_berlin.ai_for_games.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.player.strategies.AssessmentConfig;

class AssessmentTraining {

    private static List<AssessmentConfig> createCandidates(int count) {
        List<AssessmentConfig> candidates = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            // TODO: maybe check for linear independance among the candidates;
            AssessmentConfig config = new AssessmentConfig(random.nextInt(1, Integer.MAX_VALUE),
                    random.nextInt(1, Integer.MAX_VALUE), random.nextInt(1, Integer.MAX_VALUE),
                    random.nextInt(1, Integer.MAX_VALUE), random.nextInt(1, Integer.MAX_VALUE),
                    random.nextInt(1, Integer.MAX_VALUE));
            candidates.add(config);
        }
        return candidates;
    }

    private static void writeConfig(AssessmentConfig config, String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(config, AssessmentConfig.class);

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(jsonContent);
        }

    }

    private HashMap<AssessmentConfig, Integer> evaluateCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    private List<AssessmentConfig> mutateCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    private void printCandidates(List<AssessmentConfig> candidates) {
        for (AssessmentConfig config : candidates) {
            System.out.println(config);
        }
    }

    private List<AssessmentConfig> recombineCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    private List<AssessmentConfig> selectCandidates(HashMap<AssessmentConfig, Integer> candidates) {
        return null;
    }

}
