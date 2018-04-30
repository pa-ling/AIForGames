package de.htw_berlin.ai_for_games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.htw_berlin.ai_for_games.GawihsBoard.FieldState;
import lenz.htw.gawihs.Move;

public class GawihsPlayer {

    private static List<Field> getFieldsAround(int x, int y) {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(x - 1, y - 1));
        fields.add(new Field(x, y - 1));
        fields.add(new Field(x - 1, y));
        fields.add(new Field(x + 1, y));
        fields.add(new Field(x, y + 1));
        fields.add(new Field(x + 1, y + 1));
        return fields;
    }

    private static List<Field> getStartingPositions(FieldState playerNumber) {
        Field[] positionsArray;

        switch (playerNumber) {
        case PLAYER_0:
            positionsArray = new Field[] { new Field(0, 0), new Field(1, 0), new Field(2, 0), new Field(3, 0),
                    new Field(4, 0) };
            break;
        case PLAYER_1:
            positionsArray = new Field[] { new Field(0, 4), new Field(1, 5), new Field(2, 6), new Field(3, 7),
                    new Field(4, 8) };
            break;
        case PLAYER_2:
            positionsArray = new Field[] { new Field(8, 4), new Field(8, 5), new Field(8, 6), new Field(8, 7),
                    new Field(8, 8) };
            break;
        default:
            positionsArray = new Field[0];
        }

        return Arrays.asList(positionsArray);
    }

    private final FieldState playerNumber;

    private final List<Field> playerPositions;

    private final GawihsBoard board;

    public GawihsPlayer(int playerNumber, GawihsBoard board) {
        this.playerNumber = FieldState.values()[playerNumber];
        this.board = board;
        this.playerPositions = getStartingPositions(this.playerNumber);
    }

    private Set<Move> getValidMoves() {
        Set<Move> validMoves = new HashSet<>();
        for (Field playerPosition : this.playerPositions) { // TODO: remove blocked stones
            for (Field targetField : getValidTargetFields()) {
                List<Field> fieldsAround = getFieldsAround(targetField.x, targetField.y);
                for (Field field : fieldsAround) {
                    // ist an dieser Stelle ein andere Stein, als der aktuelle Stein?
                    if (!field.equals(playerPosition) && this.playerPositions.contains(field)) {
                        validMoves.add(new Move(playerPosition.x, playerPosition.y, targetField.x, targetField.y));
                    }
                }
            }
        }
        return validMoves;
    }

    private Set<Field> getValidTargetFields() {
        Set<Field> fields = new HashSet<>();

        for (Field currentPlayerPosition : this.playerPositions) {
            fields.addAll(getFieldsAround(currentPlayerPosition.x, currentPlayerPosition.y));
        }

        Iterator<Field> itr = fields.iterator();
        while (itr.hasNext()) {
            Field field = itr.next();
            if (field.x < 0 || field.x > 8 || field.y < 0 || field.y > 8) {
                itr.remove();
                continue;
            }

            FieldState fieldState = this.board.getField(field.x, field.y).peek();
            if (fieldState == FieldState.DESTROYED || fieldState == this.playerNumber) {
                itr.remove();
            }
        }

        // System.out.println("validTargetFields:");
        // for (Field field : fields) {
        // System.out.println(
        // "(" + field.x + "," + field.y + "):" + this.board.getField(field.x,
        // field.y).peek().toString());
        // }

        return fields;
    }

    public Move move() {
        Move moveToSend = getValidMoves().stream().findFirst().get();

        // update playerPositions
        this.playerPositions.remove(new Field(moveToSend.fromX, moveToSend.fromY));
        this.playerPositions.add(new Field(moveToSend.toX, moveToSend.toY));

        return moveToSend;
    }
}
