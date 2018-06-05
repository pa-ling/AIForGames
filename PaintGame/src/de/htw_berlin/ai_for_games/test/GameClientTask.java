package de.htw_berlin.ai_for_games.test;

import de.htw_berlin.ai_for_games.ZpifubClient;

class GameClientTask implements Runnable {
    private final String host, name, message;

    public GameClientTask(String host, String name, String message) {
        this.host = host;
        this.name = name;
        this.message = message;
    }

    @Override
    public void run() {
        String[] args = { this.host, this.name, this.message };
        ZpifubClient.main(args);
    }
}