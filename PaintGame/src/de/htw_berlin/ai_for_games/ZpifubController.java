package de.htw_berlin.ai_for_games;

import java.util.Arrays;
import java.util.List;

import de.htw_berlin.ai_for_games.Bot.Bot;
import de.htw_berlin.ai_for_games.Bot.LargeBrushBot;
import de.htw_berlin.ai_for_games.Bot.SmallBrushBot;
import de.htw_berlin.ai_for_games.Bot.SprayCanBot;
import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class ZpifubController {

    public static void main(String[] args) {
        String host = args[0], name = args[1], message = args[2];
        new ZpifubController().startGame(host, name, message);
    }

    private Bot findBotNextToItem(List<Bot> botList, Update update) {
        // TODO find bot which is next to the item
        // get item position
        // check if bots are in certain range of item (e.g.) 10 nodes or so
        // what about enemy bots?
        return null;
    }

    public void startGame(String host, String name, String message) {
        System.out.println(
                "Client started with '" + host + "' as host, '" + name + "' as name and '" + message + "' as message.");

        // init
        final NetworkClient client = new NetworkClient(host, name, message);
        final int myPlayerNumber = client.getMyPlayerNumber();

        Color myColor = Arrays.stream(Color.values()).filter(c -> c.playerNumber == myPlayerNumber).findFirst().get();
        final QuadTree quadTree = new QuadTree(myColor, client);
        final Bot spraycan = new SprayCanBot(quadTree);
        final Bot smallBrush = new SmallBrushBot(quadTree);
        final Bot largeBrush = new LargeBrushBot(quadTree);
        final List<Bot> botList = Arrays.asList(spraycan, smallBrush, largeBrush);

        quadTree.initNodes();

        System.out.println("Finished initialization!");

        // game loop
        while (client.isAlive()) {
            Update update;
            if ((update = client.pullNextUpdate()) != null) {
                System.out.print("Received Update:\n" + "position: (" + update.x + "," + update.y + ")\n" + "player: "
                        + update.player + "\n" + "bot: " + update.bot + "\n" + "powerup type: " + update.type + "\n");

                if (update.player != -1) {
                    quadTree.updateQuad(update.x, update.y);
                }

                // our bots
                if (update.player == myPlayerNumber) {

                    // find bot in update and get bot object
                    Bot bot = botList.stream().filter(b -> b.getBotNumber() == update.bot).findFirst().get();

                    bot.updatePosition(update);

                    if (bot.pathQueuesAreEmpty()) {
                        // bot has most likely reached it's destination
                        bot.findNextTarget();
                    }

                    Pair direction = bot.getNextDirection();
                    client.setMoveDirection(bot.getBotNumber(), direction.x, direction.y);
                    System.out.println("Direction sent for Bot" + bot.getBotNumber() + ": (" + direction.x + ","
                            + direction.y + ")");
                }
            }
        }
    }

}
