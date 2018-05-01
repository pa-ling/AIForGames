package de.htw_berlin.ai_for_games.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.htw_berlin.ai_for_games.game.GawihsBoard.FieldState;
import de.htw_berlin.ai_for_games.util.BoardUtil;
import lenz.htw.gawihs.Move;

public class GawihsPlayer {

    private static List<Field> getStartingPositions(FieldState playerNumber) {
        switch (playerNumber) {
            case PLAYER_0:
                return Arrays.asList(new Field(0, 0), new Field(1, 0), new Field(2, 0), new Field(3, 0),
                        new Field(4, 0));
            case PLAYER_1:
                return Arrays.asList(new Field(0, 4), new Field(1, 5), new Field(2, 6), new Field(3, 7),
                        new Field(4, 8));
            case PLAYER_2:
                return Arrays.asList(new Field(8, 4), new Field(8, 5), new Field(8, 6), new Field(8, 7),
                        new Field(8, 8));
            default:
                return new ArrayList<>();
        }
    }

    private final FieldState playerNumber;

    private final List<Field> playerPositions;

    private final GawihsBoard board;

    public GawihsPlayer(int playerNumber, GawihsBoard board) {
        this.playerNumber = FieldState.values()[playerNumber];
        this.board = board;
        this.playerPositions = getStartingPositions(this.playerNumber);
    }

    private Set<Move> getPossibleMoves() {
        Set<Move> validMoves = new HashSet<>();
        for (Field playerPosition : this.playerPositions) { // TODO: remove blocked stones
            for (Field targetField : getTargetFields()) {
                for (Field field : BoardUtil.getFieldsAround(targetField.x, targetField.y)) {
                    // ist an dieser Stelle ein andere Stein, als der aktuelle Stein?
                    if (!field.equals(playerPosition) && this.playerPositions.contains(field)) {
                        validMoves.add(new Move(playerPosition.x, playerPosition.y, targetField.x, targetField.y));
                    }
                }
            }
        }
        return validMoves;
    }

    private Set<Field> getTargetFields() {
        Set<Field> fields = new HashSet<>();

        for (Field currentPlayerPosition : this.playerPositions) {
            fields.addAll(BoardUtil.getAvailableFieldsForPlayerAround(currentPlayerPosition.x, currentPlayerPosition.y, this.board, this.playerNumber));
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
        Move moveToSend = getPossibleMoves().stream().findFirst().get();

        // update playerPositions
        this.playerPositions.remove(new Field(moveToSend.fromX, moveToSend.fromY));
        this.playerPositions.add(new Field(moveToSend.toX, moveToSend.toY));

        return moveToSend;
    }
}
