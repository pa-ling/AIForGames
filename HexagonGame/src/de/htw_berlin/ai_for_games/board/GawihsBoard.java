package de.htw_berlin.ai_for_games.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import de.htw_berlin.ai_for_games.player.GawihsPlayer;
import lenz.htw.gawihs.Move;

public class GawihsBoard {

    public enum FieldState {
        PLAYER_0, PLAYER_1, PLAYER_2, UNOCCUPIED, DESTROYED
    }

    public static final int SIZE = 9;

    /**
     * Returns {@link Field field objects} for the adjacent fields of a given center
     * field. Does not check if the fields are occupied or destroyed.
     *
     * @param field
     *            center field of the adjacent fields
     * @return {@link List} containing {@link Field fields}. If there are no
     *         available fields an empty list is returned.
     */
    public static List<Field> getFieldsAround(Field centerField) {
        final List<Field> fieldsAround = new ArrayList<>();
        fieldsAround.add(new Field(centerField.x - 1, centerField.y - 1));
        fieldsAround.add(new Field(centerField.x, centerField.y - 1));
        fieldsAround.add(new Field(centerField.x - 1, centerField.y));
        fieldsAround.add(new Field(centerField.x + 1, centerField.y));
        fieldsAround.add(new Field(centerField.x, centerField.y + 1));
        fieldsAround.add(new Field(centerField.x + 1, centerField.y + 1));

        Iterator<Field> itr = fieldsAround.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            if (field.x < 0 || field.x > 8 || field.y < 0 || field.y > 8) {
                itr.remove();
            }
        }

        return fieldsAround;
    }

    private final List<Stack<FieldState>> fields;

    public GawihsBoard() {
        this.fields = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE * SIZE; i++) {
            this.fields.add(new Stack<>());
        }

        // Set everything to unoccupied
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                getFieldState(i, j).push(FieldState.UNOCCUPIED);
            }
        }

        // Set players
        for (int i = 0; i < 5; i++) {
            getFieldState(i, 0).push(FieldState.PLAYER_0);
            getFieldState(i, i + 4).push(FieldState.PLAYER_1);
            getFieldState(8, 8 - i).push(FieldState.PLAYER_2);
        }

        // Set destroyed areas
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                getFieldState(j + 5, i).push(FieldState.DESTROYED);
                getFieldState(i, j + 5).push(FieldState.DESTROYED);
            }
        }

        getFieldState(4, 4).push(FieldState.DESTROYED);
    }

    public void applyMove(Move move) {
        Stack<FieldState> sourceField = getFieldState(move.fromX, move.fromY);
        if (sourceField.peek() == FieldState.UNOCCUPIED || sourceField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException("SourceField does not contain a player stone!");
        }

        Stack<FieldState> targetField = getFieldState(move.toX, move.toY);
        if (targetField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException("TargetField ist destroyed!");
        }

        targetField.push(sourceField.pop());
        if (sourceField.peek() == FieldState.UNOCCUPIED) {
            sourceField.push(FieldState.DESTROYED);
        }
    }

    /**
     * Returns {@link Field field objects} for available adjacent fields of a given
     * center field. Available means that the field is either unoccupied or occupied
     * by just one player.
     *
     * @param field
     *            center field of the adjacent fields
     * @return {@link List} containing {@link Field fields}. If there are no
     *         available fields an empty list is returned.
     */
    public List<Field> getAvailableFieldsAround(Field centerField) {
        final List<Field> fields = getFieldsAround(centerField);
        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            if (isFieldFullOrDestroyed(itr.next())) {
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
     * @param field
     *            center field of the adjacent fields
     * @param player
     *            player for which the check shall be performed
     * @return {@link List} containing {@link Field fields}. If there are no
     *         available fields an empty list is returned
     */
    public List<Field> getAvailableFieldsForPlayerAround(Field centerField, GawihsPlayer player) {
        final List<Field> fields = getFieldsAround(centerField);
        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            if (isFieldFullOrDestroyed(field) || isPlayerOnTopOfField(field, player)) {
                itr.remove();
            }
        }

        return fields;
    }

    private Stack<FieldState> getFieldState(int x, int y) {
        return this.fields.get(y * SIZE + x); // rowIndex * numberOfColumns + columnIndex
    }

    /**
     * Checks if the given field is full or destroyed.
     *
     * @param field
     *            field to check
     * @return {@code true} if the field is occupied by more than two players or
     *         destroyed otherwise {@code false}
     */
    public boolean isFieldFullOrDestroyed(Field field) {
        Stack<FieldState> fieldStack = getFieldState(field.x, field.y);
        // since the unoccupied state is also saved, full means that there are at least
        // three elements on the stack: empty, player_x, player_y
        return fieldStack.size() > 2 || fieldStack.peek() == FieldState.DESTROYED;
    }

    /**
     * Checks if the given player is on top of the given field.
     *
     * @param field
     *            field to check
     * @param player
     *            player for which the check shall be performed
     * @return {@code true} if the player is on top otherwise {@code false}
     */
    public boolean isPlayerOnTopOfField(Field field, GawihsPlayer player) {
        return getFieldState(field.x, field.y).peek() == player.getPlayerNumber();
    }

    public void print() {
        for (int j = SIZE - 1; j >= 0; j--) {
            for (int i = 0; i < SIZE; i++) {
                System.out.print(getFieldState(i, j).peek().toString() + "\t");
            }
            System.out.println("");
        }
    }

    public void removePlayer(GawihsPlayer player) {
        for (Field field : player.getPlayerStonePositions()) {
            Stack<FieldState> fieldState = getFieldState(field.x, field.y);
            // two elements: unoccupied + playerStone
            if (fieldState.size() == 2) {
                // FIXME optional, remove?
                fieldState.pop();
                fieldState.push(FieldState.DESTROYED);
                continue;
            }
            // three elements: unnoccupied + playerStone + enemy
            // or unnoccupied + enemy + playerStone
            // remove the top element - if we're lucky it's the player to remove and we can
            // continue
            FieldState topPlayer = fieldState.pop();
            if (topPlayer == player.getPlayerNumber()) {
                continue;
            }

            // the player to remove was not the top element so he has to be the second one
            // on the stack
            // remove the player from the stack and put the top player back on
            fieldState.pop();
            fieldState.push(topPlayer);
        }
    }

}
