package de.htw_berlin.ai_for_games.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private final Map<Integer, List<Field>> playerStones;

    public GawihsBoard() {
        this.fields = new ArrayList<>(SIZE * SIZE);
        this.playerStones = new HashMap<>();
        this.playerStones.put(FieldState.PLAYER_0.ordinal(), new ArrayList<>());
        this.playerStones.put(FieldState.PLAYER_1.ordinal(), new ArrayList<>());
        this.playerStones.put(FieldState.PLAYER_2.ordinal(), new ArrayList<>());

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
            // set fields on board
            getFieldState(i, 0).push(FieldState.PLAYER_0);
            getFieldState(i, i + 4).push(FieldState.PLAYER_1);
            getFieldState(8, 8 - i).push(FieldState.PLAYER_2);

            // set field positions
            this.playerStones.get(FieldState.PLAYER_0.ordinal()).add(new Field(i, 0));
            this.playerStones.get(FieldState.PLAYER_1.ordinal()).add(new Field(i, i + 4));
            this.playerStones.get(FieldState.PLAYER_2.ordinal()).add(new Field(8, 8 - i));
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

    public GawihsBoard(GawihsBoard sourceBoard) {
        this.fields = new ArrayList<>(SIZE * SIZE);
        for (Stack<FieldState> currentStack : sourceBoard.fields) {
            this.fields.add((Stack<FieldState>) currentStack.clone());
        }

        this.playerStones = new HashMap<>();
        this.playerStones.put(FieldState.PLAYER_0.ordinal(), new ArrayList<>());
        this.playerStones.put(FieldState.PLAYER_1.ordinal(), new ArrayList<>());
        this.playerStones.put(FieldState.PLAYER_2.ordinal(), new ArrayList<>());

        for (Field srcField : sourceBoard.playerStones.get(FieldState.PLAYER_0.ordinal())) {
            this.playerStones.get(FieldState.PLAYER_0.ordinal()).add(new Field(srcField));
        }

        for (Field srcField : sourceBoard.playerStones.get(FieldState.PLAYER_1.ordinal())) {
            this.playerStones.get(FieldState.PLAYER_1.ordinal()).add(new Field(srcField));
        }

        for (Field srcField : sourceBoard.playerStones.get(FieldState.PLAYER_2.ordinal())) {
            this.playerStones.get(FieldState.PLAYER_2.ordinal()).add(new Field(srcField));
        }
    }

    public void applyMove(GawihsPlayer player, Move move) {
        applyMove(player.getPlayerNumberAsOrdinal(), move);
    }

    public void applyMove(int playerNumber, Move move) {
        // apply move on playerStones first, otherwise the field is already destroyed
        // and the sanity check fails
        applyMoveOnPlayerStones(playerNumber, move);
        applyMoveOnBoard(move);
    }

    private void applyMoveOnBoard(Move move) {
        Stack<FieldState> sourceField = getFieldState(move.fromX, move.fromY);
        if (sourceField.peek() == FieldState.UNOCCUPIED || sourceField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException(
                    "SourceField (" + move.fromX + "," + move.fromY + ") does not contain a player stone!");
        }

        Stack<FieldState> targetField = getFieldState(move.toX, move.toY);
        if (targetField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException("TargetField (" + move.toX + "," + move.toY + ") is destroyed!");
        }

        targetField.push(sourceField.pop());
        if (sourceField.peek() == FieldState.UNOCCUPIED) {
            sourceField.push(FieldState.DESTROYED);
        }
    }

    private void applyMoveOnPlayerStones(int playerNumber, Move move) {
        Field fromField = new Field(move.fromX, move.fromY);
        if (!isPlayerOnTopOfField(fromField, playerNumber)) {
            throw new IllegalStateException("We're not moving our stone! The element we try to move is "
                    + getFieldTop(fromField) + " and we're " + playerNumber);
        }
        this.playerStones.get(playerNumber).remove(fromField);
        this.playerStones.get(playerNumber).add(new Field(move.toX, move.toY));
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
    public List<Field> getAvailableFieldsForPlayerAround(Field centerField, int playerNumber) {
        final List<Field> fields = getFieldsAround(centerField);
        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            if (isFieldFullOrDestroyed(field) || isPlayerOnTopOfField(field, playerNumber)) {
                itr.remove();
            }
        }

        return fields;
    }

    // TODO ditch the "position"
    public List<Field> getAvailablePlayerStonePositions(GawihsPlayer player) {
        return getAvailablePlayerStonePositions(player.getPlayerNumberAsOrdinal());
    }

    // TODO ditch the "position"
    public List<Field> getAvailablePlayerStonePositions(int playerNumber) {
        List<Field> availablePlayerStones = new ArrayList<>();

        // get possible target fields and remove unavailable player stones
        for (final Field playerStone : this.playerStones.get(playerNumber)) {
            if (isPlayerOnTopOfField(playerStone, playerNumber)) {
                availablePlayerStones.add(playerStone);
            }
        }

        return availablePlayerStones;
    }

    private Stack<FieldState> getFieldState(int x, int y) {
        return this.fields.get(y * SIZE + x); // rowIndex * numberOfColumns + columnIndex
    }

    /**
     * Returns the top element of the given {@link Field}.
     *
     * @return a {@link FieldState}
     */
    public FieldState getFieldTop(Field field) {
        return getFieldState(field.x, field.y).peek();
    }

    /**
     * Returns the number of unoccupied fields of this board.
     *
     * @return an int between zero and the maximum number of fields of this board
     */
    public int getUnoccupiedFieldsCount() {
        int count = 0;
        for (Stack<FieldState> fieldStack : this.fields) {
            if (fieldStack.peek() == FieldState.UNOCCUPIED) {
                count++;
            }
        }

        return count;
    }

    private boolean isFieldFullOrDestroyed(Field field) {
        Stack<FieldState> fieldStack = getFieldState(field.x, field.y);
        // since the unoccupied state is also saved, full means that there are at least
        // three elements on the stack: empty, player_x, player_y
        return fieldStack.size() > 2 || fieldStack.peek() == FieldState.DESTROYED;
    }

    private boolean isPlayerOnTopOfField(Field field, int playerNumber) {
        return getFieldState(field.x, field.y).peek().ordinal() == playerNumber;
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
        removePlayer(player.getPlayerNumberAsOrdinal());
    }

    public void removePlayer(int playerNumber) {
        // remove player from field
        for (Field field : this.playerStones.get(playerNumber)) {
            Stack<FieldState> fieldState = getFieldState(field.x, field.y);
            // two elements: unoccupied + playerStone
            if (fieldState.size() == 2) {
                fieldState.pop();
                fieldState.push(FieldState.DESTROYED);
                continue;
            }
            // three elements: unnoccupied + playerStone + enemy
            // or unnoccupied + enemy + playerStone
            // look at the top element - if it's the player to remove we leave it there
            // because of the rules of the game
            FieldState topPlayer = fieldState.peek();
            if (topPlayer.ordinal() == playerNumber) {
                continue;
            }

            // the player to remove was not the top element so he has to be the second one
            // on the stack
            // remove both stones from the stack and put the top player back on
            fieldState.pop();
            fieldState.pop();
            fieldState.push(topPlayer);
        }

        // remove player stone positions
        this.playerStones.remove(playerNumber);
    }

}
