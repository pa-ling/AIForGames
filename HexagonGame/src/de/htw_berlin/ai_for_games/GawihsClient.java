package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

public class GawihsClient {

	public static void main(String[] args) {

		final String host = args[0], name = args[1], logoPath = args[2];
		BufferedImage logo = null;
		try {
			logo = ImageIO.read(new File(logoPath));
		} catch (final IOException e) {
			System.err.println("There was a problem loading the logo.");
		}

		final GawihsBoard board = new GawihsBoard();
		final NetworkClient client = new NetworkClient(host, name, logo);
		final GawihsPlayer player = new GawihsPlayer(client.getMyPlayerNumber(), board);

		// client.getTimeLimitInSeconds();
		// client.getExpectedNetworkLatencyInMilliseconds();

		try {
			while (true) {
				Move move = client.receiveMove();
				if (move == null) {
					move = player.move();
					client.sendMove(move);
					System.out.println("Player " + client.getMyPlayerNumber() + " sent Move from (" + move.fromX + ","
							+ move.fromY + ") to (" + move.toX + "," + move.toY + ")");
				} else {
					System.out.println("Player " + client.getMyPlayerNumber() + " received Move from (" + move.fromX
							+ "," + move.fromY + ") to (" + move.toX + "," + move.toY + ")");

					board.move(move.fromX, move.fromY, move.toX, move.toY);
				}

			}
		} catch (final Exception e) {
			System.out.println("Player " + client.getMyPlayerNumber() + " got kicked.");
		}
	}

}
