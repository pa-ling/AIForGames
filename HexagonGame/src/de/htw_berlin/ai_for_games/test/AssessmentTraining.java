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

    public static Map<String, Integer> playAGame(String[] names, String[] configs, String logoPath)
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
        Map<String, Integer> scores = new HashMap<>(3);
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith("Final result:")) {
                continue;
            }

            for (int i = 0; i < 3; i++) {
                String[] placementString = reader.readLine().split(": ");
                scores.put(placementString[0], Integer.valueOf(placementString[1]));
            }
            break;
        }

        return scores;
    }

    public static void writeConfig(AssessmentConfig config, String filePath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(config, AssessmentConfig.class);

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(jsonContent);
        }

    }

    private final String[] names;
    private final String[] configPaths;
    private final String logoPath;

    public AssessmentTraining(String[] names, String[] configs, String logoPath) {
        this.names = names;
        this.configPaths = configs;
        this.logoPath = logoPath;
    }

    public Map<AssessmentConfig, Long> evaluateCandidates(List<AssessmentConfig> candidates) {
        if (candidates.size() < 3) {
            throw new IllegalArgumentException("There must be at least 3 candidates for evaluation.");
        }

        HashMap<AssessmentConfig, Long> evaluatedCandidates = new HashMap<>();
        for (AssessmentConfig candidate : candidates) {
            evaluatedCandidates.put(candidate, (long) 0);
        }

        for (AssessmentConfig candidateA : candidates) {
            try {
                writeConfig(candidateA, this.configPaths[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            for (AssessmentConfig candidateB : candidates) {
                if (candidateB == candidateA) {
                    continue;
                }
                try {
                    writeConfig(candidateB, this.configPaths[1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }
                for (AssessmentConfig candidateC : candidates) {
                    if (candidateC == candidateA) {
                        continue;
                    }
                    if (candidateC == candidateB) {
                        continue;
                    }
                    try {
                        writeConfig(candidateC, this.configPaths[2]);
                        Map<String, Integer> scores = playAGame(this.names, this.configPaths, this.logoPath);
                        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                            AssessmentConfig currentCandidate = null;
                            if (entry.getKey().equals(this.names[0])) {
                                currentCandidate = candidateA;
                            } else if (entry.getKey().equals(this.names[1])) {
                                currentCandidate = candidateB;
                            } else if (entry.getKey().equals(this.names[2])) {
                                currentCandidate = candidateC;
                            }

                            if (currentCandidate != null) {
                                evaluatedCandidates.put(currentCandidate,
                                        evaluatedCandidates.get(currentCandidate) + entry.getValue());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return evaluatedCandidates;
    }

    public List<AssessmentConfig> mutateCandidates(List<AssessmentConfig> candidates) {
        List<AssessmentConfig> mutatedCandidates = new ArrayList<>();
        return mutatedCandidates;
    }

    private void printCandidates(List<AssessmentConfig> candidates) {
        for (AssessmentConfig config : candidates) {
            System.out.println(config);
        }
    }

    public List<AssessmentConfig> recombineCandidates(List<AssessmentConfig> candidates) {
        List<AssessmentConfig> recombinedCandidates = new ArrayList<>();
        return recombinedCandidates;
    }

    public List<AssessmentConfig> selectCandidates(Map<AssessmentConfig, Long> candidates, int selectCount) {
        List<AssessmentConfig> selectedCandidates = new ArrayList<>();

        for (int i = 0; i < selectCount; i++) {
            // get candidate with max score in map
            Map.Entry<AssessmentConfig, Long> bestCandidate = null;
            for (Map.Entry<AssessmentConfig, Long> entry : candidates.entrySet()) {
                if (bestCandidate == null || (entry.getValue().compareTo(bestCandidate.getValue()) > 0
                        && !selectedCandidates.contains(entry.getKey()))) {
                    bestCandidate = entry;
                }
            }
            selectedCandidates.add(bestCandidate.getKey());
            candidates.remove(bestCandidate.getKey());
        }

        return selectedCandidates;
    }

}