package de.htw_berlin.ai_for_games;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class GawihsClientTest {

    private String[] NAMES = { "Alice", "Bob", "Carol" };
    private boolean AUTOMATIC_GAME_SERVER_STARTUP = false;
    private String LOGO_PATH = "res/claptrap.png";

    private class GameClientTask implements Runnable {
        private String host, name, logoPath;

        public GameClientTask(String host, String name, String logoPath) {
            this.host = host;
            this.name = name;
            this.logoPath = logoPath;
        }

        public void run() {
            String[] args = { host, name, logoPath };
            GawihsClient.main(args);
        }
    }

    @Test
    public void testGame() throws InterruptedException, IOException {
        // start server
        Optional<Process> process = Optional.empty();
        if (AUTOMATIC_GAME_SERVER_STARTUP) {
            process = Optional
                    .of(new ProcessBuilder("java", "-Djava.library.path=lib/native", "-jar", "gawihs.jar", "640", "480")
                            .directory(new File("lib/")).inheritIO().start());
            Thread.sleep(1000);
        }

        // start Clients
        ExecutorService executor = Executors.newFixedThreadPool(NAMES.length);
        for (final String name : NAMES) {
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, LOGO_PATH);
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