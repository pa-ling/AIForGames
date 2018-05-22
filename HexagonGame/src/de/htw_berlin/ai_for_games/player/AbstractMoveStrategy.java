package de.htw_berlin.ai_for_games.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.htw_berlin.ai_for_games.board.Field;
import de.htw_berlin.ai_for_games.board.GawihsBoard;
import lenz.htw.gawihs.Move;

public abstract class AbstractMoveStrategy implements MoveStrategy {

    /**
     * Returns a list of all possible moves for the given player and board
     * configuration. Imposes no restrictions expect the ones according to the rules
     *
     * @param board
     *            board configuration to check for possible moves
     * @param player
     *            player to calculate possible moves for
     * @return a {@link List} of {@link Move Moves} or an empty list if no move is
     *         possible anymore
     */
    protected static List<Move> getPossibleMoves(GawihsBoard board, GawihsPlayer player) {
        Set<Field> targetFields = new HashSet<>();

        List<Field> availablePlayerStones = player.getAvailablePlayerStonePositions();

        // get possible target fields and remove unavailable player stones
        for (Field playerStone : availablePlayerStones) {
            targetFields.addAll(board.getAvailableFieldsForPlayerAround(playerStone, player));
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

    protected GawihsBoard board;

    protected GawihsPlayer player;

    protected List<GawihsPlayer> enemies;

    @Override
    public List<Move> getPossibleMoves() {
        return getPossibleMoves(this.board, this.player);
    }

    @Override
    public void setBoard(GawihsBoard board) {
        this.board = board;
    }

    @Override
    public void setEnemies(List<GawihsPlayer> enemies) {
        this.enemies = enemies;
    }

    @Override
    public void setPlayer(GawihsPlayer player) {
        this.player = player;
    }

}