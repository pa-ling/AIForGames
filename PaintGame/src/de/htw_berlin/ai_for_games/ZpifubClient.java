package de.htw_berlin.ai_for_games;

import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class ZpifubClient {

    public static void main(String[] args) {
        String host = args[0], name = args[1], message = args[2];
        System.out.println(
                "Client started with '" + host + "' as host, '" + name + "' as name and '" + message + "' as message.");

        NetworkClient client = new NetworkClient(host, name, message);

        while (client.isAlive()) {
            Update update;
            while ((update = client.pullNextUpdate()) != null) {
                // verarbeite update
                System.out.print("Received Update:\n" + "position: (" + update.x + "," + update.y + ")\n" + "player: "
                        + update.player + "\n" + "bot: " + update.bot + "\n" + "powerup type: " + update.type + "\n");
                client.setMoveDirection(0, 1, 0);
                client.setMoveDirection(1, 1, 0);
                client.setMoveDirection(2, 1, 0);
            }
        }

    }

}
