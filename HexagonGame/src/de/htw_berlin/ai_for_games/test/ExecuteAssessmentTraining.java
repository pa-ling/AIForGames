package de.htw_berlin.ai_for_games.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ExecuteAssessmentTraining {

    private final static String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final static String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final static String LOGO_PATH = "res/claptrap.png";

    private final static int CANDIDATES_COUNT = 21;

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(playAGame(NAMES, CONFIGS, LOGO_PATH));
    }

    private static Map<String, Long> playAGame(String[] names, String[] configs, String logoPath)
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

}