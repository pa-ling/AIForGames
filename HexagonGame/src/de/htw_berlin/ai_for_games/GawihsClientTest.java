package de.htw_berlin.ai_for_games;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
 
public class GawihsClientTest {
	
	private int CLIENT_COUNT = 3;
	private String[] NAMES = {"Alice", "Bob", "Carol"};
	
    private class GameClientTask implements Runnable {
    	private String host, name, logoPath;

        public GameClientTask(String host, String name, String logoPath) {
          this.host = host;
          this.name = name;
          this.logoPath = logoPath;
        }

        public void run() {
        	String[] args = {host, name, logoPath};
            GawihsClient.main(args);
        }
    }
 
    @Test
    public void testGame() throws InterruptedException {        
        // start Clients        
        ExecutorService executor = Executors.newFixedThreadPool(CLIENT_COUNT);
        for (int i = 0; i < 3; i++) {
        	GameClientTask gameClientTask = new GameClientTask("127.0.0.1", NAMES[i], "res/claptrap.png");
            executor.execute(gameClientTask);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }
    
}