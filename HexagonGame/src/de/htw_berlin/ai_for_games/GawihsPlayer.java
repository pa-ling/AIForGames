package de.htw_berlin.ai_for_games;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
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
		
		@Override
		public int hashCode() {
			return this.x * 31 + this.y;
		}
		
		@Override
		public boolean equals(Object o) {
		    if (this == o) return true; // self check
		    if (o == null) return false; // null check
		    if (!(o instanceof Field)) return false; //type check
		    
		    Field field = (Field) o;
		    return this.x == field.x && this.y == field.y; // member comparison
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

	private Set<Field> getValidTargetFields() {
		Set<Field> fields = new HashSet<>();

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
		
		System.out.println("validTargetFields:");
		for (Field field : fields) {
			System.out.println("(" + field.x + "," + field.y + "):" + board.getField(field.x, field.y).peek().toString());
		}
		
		return fields;
	}

	public Move move() {
		int playerIndex = ThreadLocalRandom.current().nextInt(0, 5); // TODO: remove blocked stones
		Set<Field> validTargetFields = getValidTargetFields();
		int targetIndex = ThreadLocalRandom.current().nextInt(0, validTargetFields.size());

		// create randomMoves
		int x1 = this.playerPositions[playerIndex].x;
		int y1 = this.playerPositions[playerIndex].y;
		int i = 0, x2 = 0, y2 = 0;
		for (Field field : validTargetFields) {
			if (i++ == targetIndex) {
				x2 = field.x;
				y2 = field.y;
				break;
			}
		}
		
		// update playerPositions
		this.playerPositions[playerIndex].x = x2;
		this.playerPositions[playerIndex].y = y2;

		return new Move(x1, y1, x2, y2);
	}

}
