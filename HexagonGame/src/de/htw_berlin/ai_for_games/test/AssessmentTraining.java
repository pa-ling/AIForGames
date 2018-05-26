package de.htw_berlin.ai_for_games.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.player.strategies.AssessmentConfig;

class AssessmentTraining {

    public static List<AssessmentConfig> createCandidates(int count) {
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

    public static Map<String, Long> playAGame(String[] names, String[] configs, String logoPath)
            throws IOException, InterruptedException {
        // start server
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-Djava.library.path=lib/native", "-jar",
                "gawihs.jar", "800", "600", "5", "noanim", "autoclose").directory(new File("lib"));
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        Thread.sleep(1000);

        // start Clients
        ExecutorService executor = Executors.newFixedThreadPool(names.length);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String config = configs[i];
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, logoPath, config);
            executor.execute(gameClientTask);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        // get Scores
        String line = null;
        Map<String, Long> scores = new HashMap<>(3);
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("Final result:")) {
                continue;
            }

            for (int i = 0; i < 3; i++) {
                String[] placementString = reader.readLine().split(": ");
                scores.put(placementString[0], Long.valueOf(placementString[1]));
            }
            break;
        }

        return scores;
    }

    private static void writeConfig(AssessmentConfig config, String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(config, AssessmentConfig.class);

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(jsonContent);
        }

    }

    public HashMap<AssessmentConfig, Integer> evaluateCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    public List<AssessmentConfig> mutateCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    private void printCandidates(List<AssessmentConfig> candidates) {
        for (AssessmentConfig config : candidates) {
            System.out.println(config);
        }
    }

    public List<AssessmentConfig> recombineCandidates(List<AssessmentConfig> candidates) {
        return null;
    }

    public List<AssessmentConfig> selectCandidates(HashMap<AssessmentConfig, Integer> candidates) {
        return null;
    }

}
