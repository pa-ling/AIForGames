package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import de.htw_berlin.ai_for_games.game.Field;
import de.htw_berlin.ai_for_games.game.GawihsBoard;
import de.htw_berlin.ai_for_games.game.GawihsPlayer;
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
        players.offer(new GawihsPlayer(0, board));
        players.offer(new GawihsPlayer(1, board));
        players.offer(new GawihsPlayer(2, board));

        int playerNumber = client.getMyPlayerNumber();
        GawihsPlayer currentPlayer = players.poll();

        // client.getTimeLimitInSeconds();
        // client.getExpectedNetworkLatencyInMilliseconds();

        try {
            while (true) {
                Move move = client.receiveMove();
                if (move == null) {
                    move = currentPlayer.move();
                    client.sendMove(move);
                    System.out.println(name + " (" + playerNumber + ") sent Move from (" + move.fromX + "," + move.fromY
                            + ") to (" + move.toX + "," + move.toY + ")\n");
                } else {
                    // FIXME: test reaction to player dying
                    if (!board.isPlayerOnTopOfField(new Field(move.fromX, move.fromY), currentPlayer)) {
                        System.out.println(currentPlayer + " will be removed.");
                        board.removePlayer(currentPlayer);
                        // delete player by getting the next player without putting the currentPlayer on
                        // the back of the queue
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
        } catch (Exception e) {
            System.out.println(name + " (" + playerNumber + ") got kicked.\n Reason: " + e.getMessage() + "\n");
        }
    }

}
