package de.htw_berlin.ai_for_games.test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ExecuteAssessmentTraining {

    private final static String[] NAMES = { "Alice", "Bob", "Carol" };
    private final static String[] CONFIGS = { "res/configA.json", "res/configB.json", "res/configC.json" };
    private final static boolean AUTOMATIC_GAME_SERVER_STARTUP = true;
    private final static String LOGO_PATH = "res/claptrap.png";

    private final static int CANDIDATES_COUNT = 21;

    public static void main(String[] args) throws InterruptedException, IOException {
        // start server
        Optional<Process> process = Optional.empty();
        if (AUTOMATIC_GAME_SERVER_STARTUP) {
            process = Optional
                    .of(new ProcessBuilder("java", "-Djava.library.path=lib/native", "-jar", "gawihs.jar", "800", "600",
                            "5", "showcoords", "noanim", "autoclose").directory(new File("lib")).inheritIO().start());
            Thread.sleep(1000);
        }

        // start Clients
        ExecutorService executor = Executors.newFixedThreadPool(NAMES.length);
        for (int i = 0; i < NAMES.length; i++) {
            String name = NAMES[i];
            String config = CONFIGS[i];
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, LOGO_PATH, config);
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