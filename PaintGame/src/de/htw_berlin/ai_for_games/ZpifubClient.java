package de.htw_berlin.ai_for_games;

import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class ZpifubClient {

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient("127.0.0.1", "Teamname", "Gewinnnachricht");
        client.getMyPlayerNumber(); // 0 = rot, 1 = grün, 2 = blau

        client.getScore(0); // Punkte von rot

        client.getInfluenceRadiusForBot(0); // gibt 40

        int x = 50;
        int y = 40;
        client.isWalkable(x, y); // begehbar oder Hinderniss?

        int rgb = client.getBoard(x, y); // x,y zwischen 0-1023
        int b = rgb & 255;
        int g = (rgb >> 8) & 255;
        int r = (rgb >> 16) & 255;

        client.setMoveDirection(0, 1, 0); // bot 0 nach rechts
        client.setMoveDirection(1, 0.23f, -0.52f); // bot 1 nach rechts unten

        Update update;
        while ((update = client.pullNextUpdate()) != null) {
            //verarbeite update
            if (update.type == null) {
                //bot[update.player][update.bot].pos = update.x, update.y
            } else if (update.player == -1) {
                //update spawned, type, position
            } else {
                //update collected
            }
        }

    }

}
