package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
        GawihsPlayer player = new GawihsPlayer(client.getMyPlayerNumber(), board);

        // client.getTimeLimitInSeconds();
        // client.getExpectedNetworkLatencyInMilliseconds();

        try {
            while (true) {
                Move move = client.receiveMove();
                if (move == null) {
                    move = player.move();
                    client.sendMove(move);
                    System.out.println(name + " (" + client.getMyPlayerNumber() + ") sent Move from (" + move.fromX
                            + "," + move.fromY + ") to (" + move.toX + "," + move.toY + ")\n");
                } else {
                    System.out.println(name + " (" + client.getMyPlayerNumber() + ") received Move from (" + move.fromX
                            + "," + move.fromY + ") to (" + move.toX + "," + move.toY + ")\n");
                    board.move(move.fromX, move.fromY, move.toX, move.toY);
                }

            }
        } catch (Exception e) {
            System.out.println(
                    name + " (" + client.getMyPlayerNumber() + ") got kicked.\n Reason: " + e.getMessage() + "\n");
        }
    }

}
