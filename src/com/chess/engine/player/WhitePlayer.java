package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer  extends Player {
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardMoves, final Collection<Move> blackStandardMoves) {
        super(board,whiteStandardMoves,blackStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();

    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegal,final Collection<Move> opponentLegal) {
        final List<Move>kingCastles=new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            //White kings side castle
            if (!this.board.getTile(61).IsTileOccupied() && !this.board.getTile(62).IsTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.IsTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(61, opponentLegal).isEmpty() &&
                            Player.calculateAttackOnTile(62, opponentLegal).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,62,(Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),61));
                    }
                }
            }

            //White  queen side castle
            if (!this.board.getTile(59).IsTileOccupied() &&
                    !this.board.getTile(58).IsTileOccupied() &&
                    !this.board.getTile(57).IsTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.IsTileOccupied() && rookTile.getPiece().isFirstMove()&&
                        Player.calculateAttackOnTile(58,opponentLegal).isEmpty()&&
                        Player.calculateAttackOnTile(59,opponentLegal).isEmpty()&&
                        rookTile.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing,58,(Rook)rookTile.getPiece(),
                                                                 rookTile.getTileCoordinate(),59));
                }
            }
        }
        return kingCastles;
    }
}
