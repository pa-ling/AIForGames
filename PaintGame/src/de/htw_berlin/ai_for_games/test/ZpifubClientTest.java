package de.htw_berlin.ai_for_games.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ZpifubClientTest {

    private final String[] NAMES = { "ALICE", "BOB" };
    private final String[] MESSAGES = { "nice!", "good!", "awesome!" };
    private final String HOST_IP = "127.0.0.1";
    private final int SLEEP_TIME = 4000;

    public void playAGame(String[] names, String[] messages, String host) throws IOException, InterruptedException {
        // start server
        new ProcessBuilder("java", "-jar", "zpifub.jar", "1120", "1024", "180").inheritIO().directory(new File("lib"))
                .start();
        Thread.sleep(this.SLEEP_TIME);

        // start Clients
        ExecutorService executor = Executors.newFixedThreadPool(names.length);
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String message = messages[i];
            GameClientTask gameClientTask = new GameClientTask("127.0.0.1", name, message);
            executor.execute(gameClientTask);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    @Test
    public void testGame() throws InterruptedException, IOException {
        playAGame(this.NAMES, this.MESSAGES, this.HOST_IP);
    }

}