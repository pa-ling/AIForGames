package de.htw_berlin.ai_for_games;

public class GawihsBoard {
	
	private enum FieldState {
		PLAYER_1, PLAYER_2, PLAYER_3, EMPTINESS, DESTROYED
	}
	
	private final static int SIZE = 9;
	
	private FieldState[][] board;
	
	public GawihsBoard() {
		board = new FieldState[SIZE][SIZE];
		
		//Set everything to EMPTINESS
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				board[i][j] = FieldState.EMPTINESS;
			}
		}
		
		//Set players
		for (int i = 0; i < 5; i++) {
			board[i][0] = FieldState.PLAYER_1;
			board[i][i+4] = FieldState.PLAYER_2;
			board[8][8-i] = FieldState.PLAYER_3;
		}
		
		//Set destroyed areas
		for (int i = 0; i < 4; i++) {
			for (int j = i; j < 4; j++) {
				board[j+5][i] = FieldState.DESTROYED;
				board[i][j+5] = FieldState.DESTROYED;
			}
		}
		board[4][4] = FieldState.DESTROYED;
	}
	
	public void move(int x1, int y1, int x2, int y2) {
		if (board[x1][y1] == FieldState.EMPTINESS || board[x1][y1] == FieldState.DESTROYED) {
			throw new IllegalStateException("Hey das geht doch nicht! >:-(");
		}

		if (board[x2][y2] == FieldState.DESTROYED) {
			throw new IllegalStateException("Hey das geht doch nicht! >:-(");
		}
		
		board[x2][y2] = board[x1][y1];
		board[x1][y1] = FieldState.DESTROYED;
		
	}
	
	public void printBoard() {	
		for (int j = SIZE - 1; j >= 0; j--) {
			for (int i = 0; i < SIZE; i++) {
				System.out.print(board[i][j] + "\t");
			}
			System.out.println("");
		}
	}

}
