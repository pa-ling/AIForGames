package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsAIPlayer;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.strategies.ThreadedAlphaBetaPruningStrategy;
import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

public class GawihsClient {

    public static void main(String[] args) {
        String host = args[0], name = args[1], logoPath = args[2], configPath = "res/config.json";

        if (args.length > 3) {
            configPath = args[3];
        }

        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File(logoPath));
        } catch (final IOException e) {
            System.err.println("There was a problem loading the logo.");
        }

        final GawihsBoard board = new GawihsBoard();
        NetworkClient client = new NetworkClient(host, name, logo);
        Queue<GawihsPlayer> players = new LinkedList<>();
        List<GawihsPlayer> enemies = new ArrayList<>();

        int playerNumber = client.getMyPlayerNumber();
        ThreadedAlphaBetaPruningStrategy moveStrategy = new ThreadedAlphaBetaPruningStrategy(configPath,
                client.getTimeLimitInSeconds());
        GawihsAIPlayer ourPlayer = new GawihsAIPlayer(playerNumber, moveStrategy, board);

        if (playerNumber == 0) {
            players.offer(ourPlayer);
        } else {
            GawihsPlayer enemy = new GawihsPlayer(0, board);
            players.offer(enemy);
            enemies.add(enemy);
        }

        if (playerNumber == 1) {
            players.offer(ourPlayer);
        } else {
            GawihsPlayer enemy = new GawihsPlayer(1, board);
            players.offer(enemy);
            enemies.add(enemy);
        }

        if (playerNumber == 2) {
            players.offer(ourPlayer);
        } else {
            GawihsPlayer enemy = new GawihsPlayer(2, board);
            players.offer(enemy);
            enemies.add(enemy);
        }

        ourPlayer.setEnemies(enemies);
        GawihsPlayer currentPlayer = players.poll();

        try {
            while (true) {
                Move move = client.receiveMove();
                moveStrategy.setExpectedNetworkLatency(client.getExpectedNetworkLatencyInMilliseconds());
                if (move == null) {
                    while (currentPlayer.getPlayerNumberAsOrdinal() != playerNumber) {
                        System.out.println("[INFO] Apparently " + currentPlayer.getPlayerNumber()
                                + " was kicked. He will be removed.");
                        board.removePlayer(currentPlayer);
                        ourPlayer.removeEnemy(currentPlayer);
                        currentPlayer = players.poll();
                    }
                    move = currentPlayer.move();
                    client.sendMove(move);
                    System.out.println("[INFO] " + name + " (" + playerNumber + ") sent Move from (" + move.fromX + ","
                            + move.fromY + ") to (" + move.toX + "," + move.toY + ")");
                } else {
                    while (!board.isPlayerOnTopOfField(new Field(move.fromX, move.fromY), currentPlayer)) {
                        System.out.println("[INFO] Apparently " + currentPlayer.getPlayerNumber()
                                + " was kicked. He will be removed.");
                        board.removePlayer(currentPlayer);
                        ourPlayer.removeEnemy(currentPlayer);
                        currentPlayer = players.poll();
                    }
                    currentPlayer.applyMove(move);
                    board.applyMove(move);
                    players.offer(currentPlayer);
                    currentPlayer = players.poll();
                    System.out.println("[INFO] " + name + " (" + playerNumber + ") received Move from (" + move.fromX
                            + "," + move.fromY + ") to (" + move.toX + "," + move.toY + ")");
                }
            }
        } catch (IllegalStateException e) {
            System.err.println(
                    "[ERROR] " + name + " (" + playerNumber + ") encountered an internal error:\n " + e + "\n");
            client.sendMove(new Move(0, 0, 0, 0)); // send illegal move so we do not have to wait for the timeout
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException e) {
            System.err.println(
                    "[ERROR] " + name + " (" + playerNumber + ") encountered an internal error:\n " + e + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(
                    "[ERROR] " + name + " (" + playerNumber + ") got kicked.\n Reason: " + e.getMessage() + "\n");
        }
    }

}
