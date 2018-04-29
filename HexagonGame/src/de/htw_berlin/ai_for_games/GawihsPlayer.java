package de.htw_berlin.ai_for_games;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.htw_berlin.ai_for_games.GawihsBoard.FieldState;
import lenz.htw.gawihs.Move;

public class GawihsPlayer {

	private class Field {
		public int x;
		public int y;

		public Field(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private final FieldState playerNumber;
	private final Field[] playerPositions;
	private final GawihsBoard board;

	public GawihsPlayer(int playerNumber, GawihsBoard board) {
		this.playerNumber = FieldState.values()[playerNumber];
		this.board = board;
		this.playerPositions = getStartingPositions(this.playerNumber);
	}

	private Field[] getStartingPositions(FieldState playerNumber) {
		switch (playerNumber) {
		case PLAYER_0:
			return new Field[] { new Field(0, 0), new Field(1, 0), new Field(2, 0), new Field(3, 0), new Field(4, 0) };
		case PLAYER_1:
			return new Field[] { new Field(0, 4), new Field(1, 5), new Field(2, 6), new Field(3, 7), new Field(4, 8) };
		case PLAYER_2:
			return new Field[] { new Field(8, 4), new Field(8, 5), new Field(8, 6), new Field(8, 7), new Field(8, 8) };
		default:
			return new Field[0];
		}
	}

	private List<Field> getValidTargetFields() {
		List<Field> fields = new ArrayList<>();

		for (Field currentPlayerPositon : this.playerPositions) {
			fields.add(new Field(currentPlayerPositon.x - 1, currentPlayerPositon.y - 1));
			fields.add(new Field(currentPlayerPositon.x, currentPlayerPositon.y - 1));
			fields.add(new Field(currentPlayerPositon.x - 1, currentPlayerPositon.y));
			fields.add(new Field(currentPlayerPositon.x + 1, currentPlayerPositon.y));
			fields.add(new Field(currentPlayerPositon.x, currentPlayerPositon.y + 1));
			fields.add(new Field(currentPlayerPositon.x + 1, currentPlayerPositon.y + 1));
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

		return fields;
	}

	public Move move() {
		int playerIndex = ThreadLocalRandom.current().nextInt(0, 5); // TODO: remove blocked stones
		List<Field> validTargetFields = getValidTargetFields();
		int targetIndex = ThreadLocalRandom.current().nextInt(0, validTargetFields.size());

		// create randomMoves
		int x1 = this.playerPositions[playerIndex].x;
		int y1 = this.playerPositions[playerIndex].y;
		int x2 = validTargetFields.get(targetIndex).x;
		int y2 = validTargetFields.get(targetIndex).y;

		// update playerPositions
		this.playerPositions[playerIndex].x = x2;
		this.playerPositions[playerIndex].y = x2;

		return new Move(x1, y1, x2, y2);
	}

}
