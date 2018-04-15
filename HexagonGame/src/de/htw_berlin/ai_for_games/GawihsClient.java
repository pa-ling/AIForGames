package de.htw_berlin.ai_for_games;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

public class GawihsClient {
	
	public static void main(String[] args) {
		
		String host = args[0], name = args[1], logoPath = args[2];
		BufferedImage logo = null;
		try {
			logo = ImageIO.read(new File(logoPath));
		} catch (IOException e) {
			System.err.println("There was a problem loading the logo.");
		}
		
		NetworkClient client = new NetworkClient(host, name, logo);
        
		client.getMyPlayerNumber();
		client.getTimeLimitInSeconds();
		client.getExpectedNetworkLatencyInMilliseconds();
        
		while(true) {
			Move move = client.receiveMove();
			if (move == null) {
				//ich bin dran
				client.sendMove(new Move(1, 2, 3, 5));
			} else {
				//baue zug in meine spielfeldrepräsentation ein
			}
		}
	}
	
}
