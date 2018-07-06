package de.htw_berlin.ai_for_games;

import java.util.Arrays;
import java.util.List;

import de.htw_berlin.ai_for_games.Bot.Bot;
import de.htw_berlin.ai_for_games.Bot.BotType;
import de.htw_berlin.ai_for_games.Bot.LargeBrushBot;
import de.htw_berlin.ai_for_games.Bot.SmallBrushBot;
import de.htw_berlin.ai_for_games.Bot.SprayCanBot;
import de.htw_berlin.ai_for_games.pathfinding.Color;
import de.htw_berlin.ai_for_games.pathfinding.QuadTree;
import lenz.htw.zpifub.PowerupType;
import lenz.htw.zpifub.Update;
import lenz.htw.zpifub.net.NetworkClient;

public class ZpifubController {

    public static void main(String[] args) {
        String host = args[0], name = args[1], message = args[2];
        new ZpifubController().startGame(host, name, message);
    }

    private Bot findBotNextToItem(List<Bot> botList, int x, int y) {
        int lowestDistanceToItem = Integer.MAX_VALUE;
        Bot botToUse = botList.get(0);
        for (final Bot bot : botList) {
            final int distanceToItem = bot.getDistanceToItem(x, y);
            //TODO: Use BotType.speed for calculation min(length/speed of all bots)
            if (distanceToItem < lowestDistanceToItem || distanceToItem == lowestDistanceToItem && //
                    (botToUse.getBotType().equals(BotType.LARGE_BRUSH)
                            || botToUse.getBotType().equals(BotType.SMALL_BRUSH)
                                    && bot.getBotType().equals(BotType.SPRAY_CAN))) {
                lowestDistanceToItem = distanceToItem;
                botToUse = bot;
            }
        }
        return botToUse;
    }

    public void startGame(String host, String name, String message) {
        System.out.println(
                "Client started with '" + host + "' as host, '" + name + "' as name and '" + message + "' as message.");

        // init
        final NetworkClient client = new NetworkClient(host, name, message);
        final int myPlayerNumber = client.getMyPlayerNumber();
        Color myColor = Arrays.stream(Color.values()).filter(c -> c.playerNumber == myPlayerNumber).findFirst().get();
        System.out.println(name + " has the playerNumber " + myPlayerNumber + " i.e. " + myColor);

        final QuadTree quadTree = new QuadTree(myColor, client);
        final Bot sprayCan = new SprayCanBot(quadTree);
        final Bot smallBrush = new SmallBrushBot(quadTree);
        final Bot largeBrush = new LargeBrushBot(quadTree);
        final List<Bot> botList = Arrays.asList(sprayCan, smallBrush, largeBrush);

        quadTree.initNodes();

        System.out.println("Finished initialization!");

        // game loop
        while (client.isAlive()) {
            Update update;
            if ((update = client.pullNextUpdate()) != null) {
                System.out.println("Received Update: {" + "position: (" + update.x + "," + update.y + "), " + "player: "
                        + update.player + ", " + "bot: " + update.bot + ", " + "powerup type: " + update.type + "}");

                if (update.type == null) {
                    quadTree.updateQuad(update.x, update.y);
                } else if (update.player == -1) {
                    // item handling
                    if (update.type == PowerupType.SLOW) {
                        quadTree.addObstacleToPathLayer(update.x, update.y);
                        for (Bot bot : botList) {
                            bot.findNextTargetAndCalculatePath();
                        }
                    } else {
                        Bot bot = findBotNextToItem(botList, update.x, update.y);
                        bot.setItemAsTarget();
                        System.out.println(
                                "Bot: " + bot.getBotNumber() + " of player " + myPlayerNumber + " wants to go to item "
                                        + update.type.toString() + " (" + update.x + "; " + update.y + ").");
                    }
                } else {
                    if (update.type == PowerupType.SLOW) {
                        quadTree.removeObstacleFromPathLayer(update.x, update.y);
                    } else {
                        for (Bot bot : botList) {
                            if (bot.isChasesItem()) {
                                bot.removeItemAsTargetAndFindNextTarget();
                                System.out.println("Bot: " + bot.getBotNumber() + " of player " + myPlayerNumber
                                        + " forgot about item " + update.type.toString() + " (" + update.x + "; "
                                        + update.y + ") consumed by player " + update.player + " .");
                            }
                        }
                    }
                }
                
                //if (update.player == enemy that is followed) {
                //  TODO: spraycan update path
                //}

                // our bots
                if (update.player == myPlayerNumber) {
                    // find bot in update and get bot object
                    Bot bot = botList.stream().filter(b -> b.getBotNumber() == update.bot).findFirst().get();
                    bot.updatePosition(update.x, update.y);

                    Pair direction = bot.getNextDirection();
                    client.setMoveDirection(bot.getBotNumber(), direction.x, direction.y);
                    System.out.println("Direction sent for Bot " + bot.getBotNumber() + ": (" + direction.x + ","
                            + direction.y + ")");
                }
            }
        }
    }

}
