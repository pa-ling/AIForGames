package de.htw_berlin.ai_for_games.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import de.htw_berlin.ai_for_games.game.Field;
import de.htw_berlin.ai_for_games.game.GawihsBoard;
import de.htw_berlin.ai_for_games.game.GawihsBoard.FieldState;

/**
 * Utility class for various methods for operations on {@link Field Fields}.
 */
public final class BoardUtil {

    /**
     * Checks if the given player can move away from the given field. he can move
     * away if he's on the top of the field.
     *
     * @param x
     *            x coordinate of the player field
     * @param y
     *            y coordinate of the player field
     * @param board
     *            board to check
     * @return {@code true} if the player can move otherwise {@code false}
     */
    public static boolean canPlayerMove(int x, int y, GawihsBoard board, FieldState player) {
        return board.getField(x, y).peek() == player;
    }

    /**
     * Returns {@link Field field objects} for available adjacent fields of a given
     * center field. Available means that the field is either unoccupied or occupied
     * by just one player.
     *
     * @param x
     *            x coordinate of the center field
     * @param y
     *            y coordinate of the center field
     * @param board
     *            board to check
     * @return {@link List} containing {@link Field fields}. If there are no
     *         available fields an empty list is returned.
     */
    public static List<Field> getAvailableFieldsAround(int x, int y, GawihsBoard board) {
        final List<Field> fields = getFieldsAround(x, y);
        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            Stack<FieldState> fieldStack = board.getField(field.x, field.y);
            if (isFullOrDestroyed(fieldStack)) {
                itr.remove();
            }
        }

        return fields;
    }

    /**
     * Returns {@link Field field objects} for available adjacent fields of a given
     * center field. A field is available for a player if it's either unoccupied or
     * occupied by just one player but not by the player itself. This method is
     * slightly optimized and performs it's check inside one loop.
     *
     * @param x
     *            x coordinate of the center field
     * @param y
     *            y coordinate of the center field
     * @param board
     *            board to check
     * @param player
     *            as
     * @return {@link List} containing {@link Field fields}. If there are no
     *         available fields an empty list is returned
     */
    public static List<Field> getAvailableFieldsForPlayerAround(int x, int y, GawihsBoard board, FieldState player) {
        final List<Field> fields = getFieldsAround(x, y);
        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            Stack<FieldState> fieldStack = board.getField(field.x, field.y);
            if (isFullOrDestroyed(fieldStack) || isSamePlayer(player, fieldStack)) {
                itr.remove();
            }
        }

        return fields;
    }

    /**
     * Returns {@link Field field objects} for the adjacent fields of a given center
     * field. Does not check if the fields are occupied or destroyed.
     *
     * @param x
     *            x coordinate of the center field
     * @param y
     *            y coordinate of the center field
     * @return {@link List} containing the adjacent fields
     */
    public static List<Field> getFieldsAround(int x, int y) {
        final List<Field> fields = new ArrayList<>();
        fields.add(new Field(x - 1, y - 1));
        fields.add(new Field(x, y - 1));
        fields.add(new Field(x - 1, y));
        fields.add(new Field(x + 1, y));
        fields.add(new Field(x, y + 1));
        fields.add(new Field(x + 1, y + 1));

        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            if (field.x < 0 || field.x > 8 || field.y < 0 || field.y > 8) {
                itr.remove();
            }
        }

        return fields;
    }

    private static boolean isFullOrDestroyed(Stack<FieldState> fieldStack) {
        // since the unoccupied state is also saved, full means that there are at least
        // three elements on the stack: empty, player_x, player_y
        return fieldStack.size() > 2 || fieldStack.peek() == FieldState.DESTROYED;
    }

    private static boolean isSamePlayer(FieldState player, Stack<FieldState> fieldStack) {
        return fieldStack.peek() == player;
    }
}
