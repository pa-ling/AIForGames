package de.htw_berlin.ai_for_games.player.strategies;

public class AssessmentConfig {

    private int unoccupiedFieldsMultiplier;
    private int playerStonesMultiplier;
    private int possibleMovesMultiplier;
    private int enemyCountMultiplier;
    private int enemyStonesMultiplier;
    private int enemyPossibleMovesMultiplier;

    public int getEnemyCountMultiplier() {
        return this.enemyCountMultiplier;
    }

    public int getEnemyPossibleMovesMultiplier() {
        return this.enemyPossibleMovesMultiplier;
    }

    public int getEnemyStonesMultiplier() {
        return this.enemyStonesMultiplier;
    }

    public int getPlayerStonesMultiplier() {
        return this.playerStonesMultiplier;
    }

    public int getPossibleMovesMultiplier() {
        return this.possibleMovesMultiplier;
    }

    public int getUnoccupiedFieldsMultiplier() {
        return this.unoccupiedFieldsMultiplier;
    }

    public void setEnemyCountMultiplier(int enemyCountMultiplier) {
        this.enemyCountMultiplier = enemyCountMultiplier;
    }

    public void setEnemyPossibleMovesMultiplier(int enemyPossibleMovesMultiplier) {
        this.enemyPossibleMovesMultiplier = enemyPossibleMovesMultiplier;
    }

    public void setEnemyStonesMultiplier(int enemyStonesMultiplier) {
        this.enemyStonesMultiplier = enemyStonesMultiplier;
    }

    public void setPlayerStonesMultiplier(int playerStonesMultiplier) {
        this.playerStonesMultiplier = playerStonesMultiplier;
    }

    public void setPossibleMovesMultiplier(int possibleMovesMultiplier) {
        this.possibleMovesMultiplier = possibleMovesMultiplier;
    }

    public void setUnoccupiedFieldsMultiplier(int unoccupiedFieldsMultiplier) {
        this.unoccupiedFieldsMultiplier = unoccupiedFieldsMultiplier;
    }
}