package de.htw_berlin.ai_for_games.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.htw_berlin.ai_for_games.GawihsClient;
import de.htw_berlin.ai_for_games.player.strategies.AssessmentConfig;

public class TrainAssessement {

    private class GameClientTask implements Runnable {
        private final String host, name, logoPath, configPath;

        public GameClientTask(String host, String name, String logoPath, String configPath) {
            this.host = host;
            this.name = name;
            this.logoPath = logoPath;
            this.configPath = configPath;
        }

        @Override
        public void run() {
            String[] args = { this.host, this.name, this.logoPath, this.configPath };
            GawihsClient.main(args);
        }
    }

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

    private final String[] NAMES = { "Alice", "Bob", "Carol" };
    private final String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final boolean AUTOMATIC_GAME_SERVER_STARTUP = true;
    private final String LOGO_PATH = "res/claptrap.png";

    private final int CANDIDATES_COUNT = 21;

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

    @Test
    public void testGame() throws InterruptedException, IOException {
        // start server
        Optional<Process> process = Optional.empty();
        if (this.AUTOMATIC_GAME_SERVER_STARTUP) {
            process = Optional
                    .of(new ProcessBuilder("java", "-Djava.library.path=lib/native", "-jar", "gawihs.jar", "800", "600",
                            "5", "showcoords", "noanim", "autoclose").directory(new File("lib")).inheritIO().start());
            Thread.sleep(1000);
        }

        // start Clients
        ExecutorService executor = Executors.newFixedThreadPool(this.NAMES.length);
        for (int i = 0; i < this.NAMES.length; i++) {
            String name = this.NAMES[i];
            String config = this.CONFIGS[i];
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, this.LOGO_PATH, config);
            executor.execute(gameClientTask);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        process.ifPresent(p -> {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}