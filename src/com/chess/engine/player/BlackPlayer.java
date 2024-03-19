package com.chess.engine.player;



import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> blackStandardMoves, final Collection<Move> whiteStandardMoves) {
        super(board,blackStandardMoves,whiteStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegal,final Collection<Move> opponentLegal) {

        final List<Move> kingCastles=new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            //Black kings side castle
            if (!this.board.getTile(5).IsTileOccupied() && !this.board.getTile(6).IsTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.IsTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttackOnTile(5, opponentLegal).isEmpty() &&
                            Player.calculateAttackOnTile(6, opponentLegal).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add( new Move.KingSideCastleMove(this.board,this.playerKing,6, (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),5));
                    }
                }
            }

            //Black  queen side castle
            if (!this.board.getTile(1).IsTileOccupied() &&
                    !this.board.getTile(2).IsTileOccupied() &&
                    !this.board.getTile(3).IsTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.IsTileOccupied() && rookTile.getPiece().isFirstMove()&&
                        Player.calculateAttackOnTile(2,opponentLegal).isEmpty()&&
                    Player.calculateAttackOnTile(3,opponentLegal).isEmpty()&&
                        rookTile.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing,2, (Rook)rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),3));
                }
            }
        }
        return kingCastles;
    }
}
