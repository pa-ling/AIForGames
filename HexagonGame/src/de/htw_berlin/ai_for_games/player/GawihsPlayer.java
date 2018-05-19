package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import de.htw_berlin.ai_for_games.board.GawihsBoard.FieldState;
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
                break;
            default:
                return new ArrayList<>();
        }

        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    private final MoveStrategy moveStrategy;

    private final FieldState playerNumber;

    private final List<Field> playerStonePositions;

    private final GawihsBoard board;

    public GawihsPlayer(int playerNumber, MoveStrategy moveStrategy, GawihsBoard board) {
        // set up player
        this.playerNumber = FieldState.values()[playerNumber];
        this.playerStonePositions = getStartingPositions(this.playerNumber);
        this.board = board;

        // set up move strategy
        this.moveStrategy = moveStrategy;
        this.moveStrategy.setBoard(board);
        this.moveStrategy.setPlayer(this);
    }

    public void applyMove(Move move) {
        Field fromField = new Field(move.fromX, move.fromY);
        if (!this.board.isPlayerOnTopOfField(fromField, this)) {
            throw new IllegalStateException("We're not moving our stone! The element we try to move is "
                    + this.board.getFieldTop(fromField) + " and we're " + this.playerNumber);
        }
        this.playerStonePositions.remove(fromField);
        this.playerStonePositions.add(new Field(move.toX, move.toY));
    }

    public FieldState getPlayerNumber() {
        return this.playerNumber;
    }

    public int getPlayerNumberAsOrdinal() {
        return this.playerNumber.ordinal();
    }

    public List<Field> getPlayerStonePositions() {
        return this.playerStonePositions;
    }

    public Move move() {
        final Move move = this.moveStrategy.getBestMove();
        if (move == null) {
            throw new IllegalStateException(this.playerNumber + ": No moves are possible anymore!");
        }

        return move;
    }
}
