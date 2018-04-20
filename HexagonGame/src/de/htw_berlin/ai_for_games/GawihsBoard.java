package de.htw_berlin.ai_for_games;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GawihsBoard {
	
	private enum FieldState {
		PLAYER_1, PLAYER_2, PLAYER_3, UNOCCUPIED, DESTROYED
	}
	
	private final static int SIZE = 9;
	
	private List<Stack<FieldState>> board;
	
	Stack <FieldState> getField(int i, int j){
		return board.get(j * SIZE + i); //rowIndex * numberOfColumns + columnIndex
	}
	
	public GawihsBoard() {		
		board = new ArrayList<>(SIZE * SIZE);
		for (int i = 0; i < SIZE * SIZE; i++) {
			board.add(new Stack<>());
		}
		
		//Set everything to unoccupied
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				getField(i, j).push(FieldState.UNOCCUPIED);
			}
		}
		
		//Set players
		for (int i = 0; i < 5; i++) {
			getField(i, 0).push(FieldState.PLAYER_1);
			getField(i, i+4).push(FieldState.PLAYER_2);
			getField(8, 8-i).push(FieldState.PLAYER_3);
		}
		
		//Set destroyed areas
		for (int i = 0; i < 4; i++) {
			for (int j = i; j < 4; j++) {
				getField(j+5, i).push(FieldState.DESTROYED);
				getField(i, j+5).push(FieldState.DESTROYED);
			}
		}
		
		getField(4, 4).push(FieldState.DESTROYED);		
	}
	
	public void move(int x1, int y1, int x2, int y2) {
		Stack<FieldState> sourceField = getField(x1, y1);
		if (sourceField.peek() == FieldState.UNOCCUPIED || sourceField.peek() == FieldState.DESTROYED) {
			throw new IllegalStateException("Hey das geht doch nicht! >:-(");
		}

		Stack<FieldState> targetField = getField(x2, y2);
		if (targetField.peek() == FieldState.DESTROYED) {
			throw new IllegalStateException("Hey das geht doch nicht! >:-(");
		}
		
		targetField.push(sourceField.pop());
		if (sourceField.peek() == FieldState.UNOCCUPIED) {
			sourceField.pop();
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
