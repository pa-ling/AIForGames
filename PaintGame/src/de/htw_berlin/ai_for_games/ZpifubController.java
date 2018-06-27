package de.htw_berlin.ai_for_games;

import de.htw_berlin.ai_for_games.pathfinding.Graph;
import de.htw_berlin.ai_for_games.pathfinding.Maingraph;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class ZpifubController {

    public static void main(String[] args) {
        String host = args[0], name = args[1], message = args[2];
        new ZpifubController().startGame(host, name, message);
    }

    public void startGame(String host, String name, String message) {
        System.out.println(
                "Client started with '" + host + "' as host, '" + name + "' as name and '" + message + "' as message.");
        // init
        final Graph maingraph = new Maingraph();
        NetworkClient client = new NetworkClient(host, name, message);
        // game loop
        while (client.isAlive()) {
            Update update;
            if ((update = client.pullNextUpdate()) != null) {
                // verarbeite update
                System.out.print("Received Update:\n" + "position: (" + update.x + "," + update.y + ")\n" + "player: "
                        + update.player + "\n" + "bot: " + update.bot + "\n" + "powerup type: " + update.type + "\n");
                client.setMoveDirection(Bot.SPRAYCAN.number, 1, 0);
                client.setMoveDirection(Bot.SMALL_BRUSH.number, 1, 1);
                client.setMoveDirection(Bot.LARGE_BRUSH.number, 0, 1);
            }
        }
    }

}
