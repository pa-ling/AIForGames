package de.htw_berlin.ai_for_games.test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

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

    private final String[] NAMES = { "Alice", "Bob", "Carol" };
    private final String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final String BEST_CONFIG = "res/config.json";
    private final boolean AUTOMATIC_GAME_SERVER_STARTUP = true;
    private final String LOGO_PATH = "res/claptrap.png";

    private void createConfig() {
        AssessmentConfig config = new AssessmentConfig();
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
        for (final String name : this.NAMES) {
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, this.LOGO_PATH, this.BEST_CONFIG);
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