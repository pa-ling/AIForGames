package de.htw_berlin.ai_for_games;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.htw_berlin.ai_for_games.Bot.Bot;
import de.htw_berlin.ai_for_games.Bot.LargeBrushBot;
import de.htw_berlin.ai_for_games.Bot.SmallBrushBot;
import de.htw_berlin.ai_for_games.Bot.SpraycanBot;
import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.Graph;
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
        final BoardInterface colorGraph = new Graph();
        final BoardInterface obstacleGraph = new Graph();
        // TODO add strategies
        final Bot spraycan = new SpraycanBot(null, null, obstacleGraph, colorGraph);
        final Bot smallBrush = new SmallBrushBot(null, null, obstacleGraph, colorGraph);
        final Bot largeBrush = new LargeBrushBot(null, null, obstacleGraph, colorGraph);
        final List<Bot> botList = Arrays.asList(spraycan, smallBrush, largeBrush);
        final NetworkClient client = new NetworkClient(host, name, message);
        final int myPlayerNumber = client.getMyPlayerNumber();

        colorGraph.setOurColor(
                Arrays.stream(Color.values()).filter(c -> c.intValue == myPlayerNumber).findFirst().get().intValue);

        // game loop
        boolean graphsAreInitalized = false;
        while (client.isAlive()) {
            if (!graphsAreInitalized) {
                graphsAreInitalized = true;

                // send initial direction
                client.setMoveDirection(spraycan.getBotNumber(), 1, 0);
                client.setMoveDirection(smallBrush.getBotNumber(), 1, 1);
                client.setMoveDirection(largeBrush.getBotNumber(), 0, 1);

                // init graphs
                // TODO initialize obstacles
            }

            // begin game logic
            Update update;
            if ((update = client.pullNextUpdate()) != null) {
                System.out.print("Received Update:\n" + "position: (" + update.x + "," + update.y + ")\n" + "player: "
                        + update.player + "\n" + "bot: " + update.bot + "\n" + "powerup type: " + update.type + "\n");

                // update graph color values
                // colorGraph.update(update, client);

                // set item as prio target if necessary
                // TODO later: enable search for item
                // if (update.type != null) {
                // if (update.type.equals(ItemType.BOMB.powerUpType)
                // || update.type.equals(ItemType.RAIN.powerUpType)) {
                // Bot nextBot = findBotNextToItem(botList, update);
                // if (nextBot != null) {
                // nextBot.setPriorityTarget(update);
                // }
                // } else {
                // obstacleGraph.setItemAsObstacle(update);
                // colorGraph.setItemAsObstacle(update);
                // }
                // }

                // // TODO later: delete prio item
                // if (update said that an item was consumed && the item was good && the item
                // was not consumed by us){
                // the field has changed, we should calculate new targets
                // botList.stream().forEach(b -> {
                // b.deletePriorityTarget();
                // b.findNextTarget();
                // });
                // }

                if (update.player == myPlayerNumber) {
                    Optional<Bot> botToUpdate = botList.stream().filter(b -> b.getBotNumber() == update.bot)
                            .findFirst();
                    if (botToUpdate.isPresent()) {
                        final Bot bot = botToUpdate.get();
                        bot.updatePosition(update);

                        if (bot.pathQueuesAreEmpty()) {
                            // bot has most likely reached it's destination
                            bot.findNextTarget();
                        }

                        Direction dir = bot.getNextDirection();
                        client.setMoveDirection(bot.getBotNumber(), dir.x, dir.y);
                    } else {
                        System.out.println(
                                "Received Update for our bot but updating the bot failed because it could not be found.");
                    }
                }
            }
        }
    }

}
