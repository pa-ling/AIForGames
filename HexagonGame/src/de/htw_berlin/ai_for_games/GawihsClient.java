package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import de.htw_berlin.ai_for_games.player.RandomMoveStrategy;
import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

public class GawihsClient {

    public static void main(String[] args) {
        String host = args[0], name = args[1], logoPath = args[2];
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File(logoPath));
        } catch (final IOException e) {
            System.err.println("There was a problem loading the logo.");
        }

        GawihsBoard board = new GawihsBoard();
        NetworkClient client = new NetworkClient(host, name, logo);
        Queue<GawihsPlayer> players = new LinkedList<>();
        players.offer(new GawihsPlayer(0, new RandomMoveStrategy(), board));
        players.offer(new GawihsPlayer(1, new RandomMoveStrategy(), board));
        players.offer(new GawihsPlayer(2, new RandomMoveStrategy(), board));

        int playerNumber = client.getMyPlayerNumber();
        GawihsPlayer currentPlayer = players.poll();

        // client.getTimeLimitInSeconds();
        // client.getExpectedNetworkLatencyInMilliseconds();

        try {
            while (true) {
                Move move = client.receiveMove();
                if (move == null) {
                    if (currentPlayer.getPlayerNumberAsOrdinal() != playerNumber) {
                        System.out.println(
                                "Apparently " + currentPlayer.getPlayerNumber() + " was kicked. He will be removed.");
                        board.removePlayer(currentPlayer);
                        currentPlayer = players.poll();
                    }
                    move = currentPlayer.move();
                    client.sendMove(move);
                    System.out.println(name + " (" + playerNumber + ") sent Move from (" + move.fromX + "," + move.fromY
                            + ") to (" + move.toX + "," + move.toY + ")\n");
                } else {
                    if (!board.isPlayerOnTopOfField(new Field(move.fromX, move.fromY), currentPlayer)) {
                        System.out.println(
                                "Apparently " + currentPlayer.getPlayerNumber() + " was kicked. He will be removed.");
                        board.removePlayer(currentPlayer);
                        currentPlayer = players.poll();
                    }
                    currentPlayer.applyMove(move);
                    board.applyMove(move);
                    players.offer(currentPlayer);
                    currentPlayer = players.poll();
                    System.out.println(name + " (" + playerNumber + ") received Move from (" + move.fromX + ","
                            + move.fromY + ") to (" + move.toX + "," + move.toY + ")\n");
                }
            }
        } catch (NullPointerException | IllegalStateException | IllegalArgumentException
                | IndexOutOfBoundsException e) {
            System.out.println(name + " (" + playerNumber + ") encountered an internal error:\n " + e + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(name + " (" + playerNumber + ") got kicked.\n Reason: " + e.getMessage() + "\n");
        }
    }

}
