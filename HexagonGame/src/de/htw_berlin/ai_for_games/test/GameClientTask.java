package de.htw_berlin.ai_for_games.test;

import de.htw_berlin.ai_for_games.GawihsClient;

class GameClientTask implements Runnable {
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