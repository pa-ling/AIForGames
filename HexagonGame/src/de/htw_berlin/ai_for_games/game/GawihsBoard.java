package de.htw_berlin.ai_for_games.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GawihsBoard {

    public enum FieldState {
        PLAYER_0, PLAYER_1, PLAYER_2, UNOCCUPIED, DESTROYED
    }

    public static final int SIZE = 9;

    // FIXME: Stack ist ein Problem. Wenn ein Spieler gekickt wird, müssen alle
    // seine Steine entfernt werden, auch die blockierten.
    private final List<Stack<FieldState>> board;

    public GawihsBoard() {
        this.board = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE * SIZE; i++) {
            this.board.add(new Stack<>());
        }

        // Set everything to unoccupied
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                getField(i, j).push(FieldState.UNOCCUPIED);
            }
        }

        // Set players
        for (int i = 0; i < 5; i++) {
            getField(i, 0).push(FieldState.PLAYER_0);
            getField(i, i + 4).push(FieldState.PLAYER_1);
            getField(8, 8 - i).push(FieldState.PLAYER_2);
        }

        // Set destroyed areas
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                getField(j + 5, i).push(FieldState.DESTROYED);
                getField(i, j + 5).push(FieldState.DESTROYED);
            }
        }

        getField(4, 4).push(FieldState.DESTROYED);
    }

    public Stack<FieldState> getField(int x, int y) {
        return this.board.get(y * SIZE + x); // rowIndex * numberOfColumns + columnIndex
    }

    public void move(int x1, int y1, int x2, int y2) {
        Stack<FieldState> sourceField = getField(x1, y1);
        if (sourceField.peek() == FieldState.UNOCCUPIED || sourceField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException("SourceField does not contain a player stone!");
        }

        Stack<FieldState> targetField = getField(x2, y2);
        if (targetField.peek() == FieldState.DESTROYED) {
            throw new IllegalStateException("TargetFiled ist detroyed!");
        }

        targetField.push(sourceField.pop());
        if (sourceField.peek() == FieldState.UNOCCUPIED) {
            sourceField.push(FieldState.DESTROYED);
        }
    }

    public void printBoard() {
        for (int j = SIZE - 1; j >= 0; j--) {
            for (int i = 0; i < SIZE; i++) {
                System.out.print(getField(i, j).peek().toString() + "\t");
            }
            System.out.println("");
        }
    }

}
