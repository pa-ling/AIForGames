package de.htw_berlin.ai_for_games.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.htw_berlin.ai_for_games.game.GawihsBoard.FieldState;
import de.htw_berlin.ai_for_games.util.BoardUtil;
import lenz.htw.gawihs.Move;

public class GawihsPlayer {

    private static List<Field> getStartingPositions(FieldState player) {
        Stream<Field> stream;

        switch (player) {
            case PLAYER_0:
                stream = Stream.of(new Field(0, 0), new Field(1, 0), new Field(2, 0), new Field(3, 0), new Field(4, 0));
                break;
            case PLAYER_1:
                stream = Stream.of(new Field(0, 4), new Field(1, 5), new Field(2, 6), new Field(3, 7), new Field(4, 8));
                break;
            case PLAYER_2:
                stream = Stream.of(new Field(8, 4), new Field(8, 5), new Field(8, 6), new Field(8, 7), new Field(8, 8));
            default:
                return new ArrayList<>();
        }

        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    private final FieldState playerNumber;

    private final List<Field> playerStonePositions;

    private final GawihsBoard board;

    public GawihsPlayer(int playerNumber, GawihsBoard board) {
        this.playerNumber = FieldState.values()[playerNumber];
        this.board = board;
        this.playerStonePositions = getStartingPositions(this.playerNumber);
    }

    private List<Move> getPossibleMoves() {
        Set<Field> targetFields = new HashSet<>();

        // get possible target fields
        for (Field currentPlayerPosition : this.playerStonePositions) {
            targetFields.addAll(BoardUtil.getAvailableFieldsForPlayerAround(currentPlayerPosition.x,
                    currentPlayerPosition.y, this.board, this.playerNumber));
        }

        // compute possible moves
        // ein Zug ist möglich, wenn um das Zielfeld mindestens ein Spielerstein liegt
        // und dieser Stein nicht der Stein ist, den wir gerade bewegen wollen
        List<Move> possibleMoves = new ArrayList<>();
        for (Field stoneToMove : this.playerStonePositions) {
            // TODO: Don't move blocked stones
            for (Field targetField : targetFields) {
                for (Field fieldAroundTarget : BoardUtil.getFieldsAround(targetField.x, targetField.y)) {
                    if (this.playerStonePositions.contains(fieldAroundTarget) && !stoneToMove.equals(fieldAroundTarget)) {
                        possibleMoves.add(new Move(stoneToMove.x, stoneToMove.y, targetField.x, targetField.y));
                    }
                }
            }
        }

        return possibleMoves;
    }

    public Move move() {
        Move moveToSend = getPossibleMoves().stream().findFirst().get();

        // update playerPositions
        this.playerStonePositions.remove(new Field(moveToSend.fromX, moveToSend.fromY));
        this.playerStonePositions.add(new Field(moveToSend.toX, moveToSend.toY));

        return moveToSend;
    }
}
