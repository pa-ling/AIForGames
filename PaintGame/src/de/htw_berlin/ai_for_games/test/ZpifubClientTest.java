package de.htw_berlin.ai_for_games.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ZpifubClientTest {

    private final String[] NAMES = { "ALICE", "BOB", "CAROL" };
    private final String[] MESSAGES = {"nice!", "good!", "awesome!"};
    private final String HOST_IP = "127.0.0.1";

    @Test
    public void testGame() throws InterruptedException, IOException {
        playAGame(NAMES, MESSAGES, HOST_IP);
    }
    
    public static void playAGame(String[] names, String[] messages, String host) throws IOException, InterruptedException {
        // start server
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "zpifub.jar").directory(new File("lib"));
        Process process = processBuilder.start();
        Thread.sleep(1000);

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

}