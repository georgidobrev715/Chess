package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(Board board, int depth) {
        // Precompute values to avoid recalculating them for both players
        boolean whiteCheckMate = board.whitePlayer().getOpponent().isInCheckMate();
        boolean blackCheckMate = board.blackPlayer().getOpponent().isInCheckMate();
        boolean whiteInCheck = board.whitePlayer().getOpponent().isInCheck();
        boolean blackInCheck = board.blackPlayer().getOpponent().isInCheck();
        int whiteMobility = board.whitePlayer().getLegalMoves().size();
        int blackMobility = board.blackPlayer().getLegalMoves().size();

        return scorePlayer(board.whitePlayer(), depth, whiteCheckMate, whiteInCheck, whiteMobility) -
                scorePlayer(board.blackPlayer(), depth, blackCheckMate, blackInCheck, blackMobility);
    }

    private int scorePlayer(Player player, int depth, boolean checkMate, boolean inCheck, int mobility) {
        return pieceValue(player) +
                mobility + // Direct use of precomputed mobility
                (inCheck ? CHECK_BONUS : 0) +
                (checkMate ? CHECK_MATE_BONUS * depthBonus(depth) : 0) +
                (player.isCastled() ? CASTLE_BONUS : 0);
    }

    private static int depthBonus(int depth) {
        // Simplify the depth bonus calculation
        return DEPTH_BONUS * depth + 1;
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceType().getPieceValue();
        }
        return pieceValueScore;
    }
}