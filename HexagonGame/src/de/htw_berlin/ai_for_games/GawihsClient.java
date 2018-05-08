package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.htw_berlin.ai_for_games.game.GawihsBoard;
import de.htw_berlin.ai_for_games.game.GawihsPlayer;
import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

public class GawihsClient {

    private static int getNextPlayerNumber(int currentPlayer) {
        if (currentPlayer == 2) {
            return 0;
        }
        return currentPlayer + 1;
    }

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
        List<GawihsPlayer> players = new ArrayList<>();
        players.add(new GawihsPlayer(0, board));
        players.add(new GawihsPlayer(1, board));
        players.add(new GawihsPlayer(2, board));

        int playerNumber = client.getMyPlayerNumber();
        GawihsPlayer currentPlayer = players.get(0);

        // client.getTimeLimitInSeconds();
        // client.getExpectedNetworkLatencyInMilliseconds();

        // TODO: react to player dying - remove stones from player and destroy fields
        try {
            while (true) {
                Move move = client.receiveMove();
                if (move == null) {
                    move = currentPlayer.move();
                    client.sendMove(move);
                    System.out.println(name + " (" + playerNumber + ") sent Move from (" + move.fromX + "," + move.fromY
                            + ") to (" + move.toX + "," + move.toY + ")\n");
                } else {
                    currentPlayer.applyMove(move);
                    board.move(move.fromX, move.fromY, move.toX, move.toY);
                    currentPlayer = players.get(getNextPlayerNumber(currentPlayer.getPlayerNumberAsOrdinal()));
                    System.out.println(name + " (" + playerNumber + ") received Move from (" + move.fromX + ","
                            + move.fromY + ") to (" + move.toX + "," + move.toY + ")\n");
                }

            }
        } catch (Exception e) {
            System.out.println(name + " (" + playerNumber + ") got kicked.\n Reason: " + e.getMessage() + "\n");
        }
    }

}
