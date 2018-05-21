package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

public abstract class AbstractMoveStrategy implements MoveStrategy {

    protected GawihsBoard board;
    protected GawihsPlayer player;

    @Override
    public List<Move> getPossibleMoves() {
        Set<Field> targetFields = new HashSet<>();

        List<Field> availablePlayerStones = new ArrayList<>();

        // get possible target fields and remove unavailable player stones
        for (Field playerStone : this.player.getPlayerStonePositions()) {
            if (!this.board.isPlayerOnTopOfField(playerStone, this.player)) {
                continue;
            }
            availablePlayerStones.add(playerStone);
            targetFields.addAll(this.board.getAvailableFieldsForPlayerAround(playerStone, this.player));
        }

        // compute possible moves
        List<Move> possibleMoves = new ArrayList<>();
        for (Field stoneToMove : availablePlayerStones) {
            for (Field targetField : targetFields) {
                for (Field fieldAroundTarget : GawihsBoard.getFieldsAround(targetField)) {
                    if (availablePlayerStones.contains(fieldAroundTarget) && !stoneToMove.equals(fieldAroundTarget)) {
                        possibleMoves.add(new Move(stoneToMove.x, stoneToMove.y, targetField.x, targetField.y));
                    }
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public void setBoard(GawihsBoard board) {
        this.board = board;
    }

    @Override
    public void setPlayer(GawihsPlayer player) {
        this.player = player;
    }

}