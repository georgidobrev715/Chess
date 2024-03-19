package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.Alliance;

import java.util.List;

public abstract class Piece {
    protected  final  PieceType pieceType;
    protected  final  int piecePosition;
    protected  final Alliance pieceAlliance;
    protected  final  boolean isFirstMove;
    private  final  int cacheHashCode;

    public Piece(final PieceType pieceType,final int piecePosition, final Alliance pieceAlliance,final boolean isFirstMove) {
        this.pieceType=pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove=isFirstMove;
        this.cacheHashCode=computeHashCode();
    }

    public int computeHashCode(){
        int result=pieceType.hashCode();
        result= 31 *result+pieceAlliance.hashCode();
        result= 31 *result+piecePosition;
        result = 31 * result+(isFirstMove ? 1:0);
        return result;
    }
    @Override
    public boolean equals(final Object object){
        if(this==object){
            return true;
        }
        if(!(object instanceof  Piece)){
            return false;
        }
        final Piece otherPiece=(Piece) object;
        return piecePosition==otherPiece.getPiecePosition() && pieceType==otherPiece.getPieceType() &&
                pieceAlliance==otherPiece.getPieceAlliance() && isFirstMove==otherPiece.isFirstMove();

    }
    @Override
    public int hashCode(){
        return this.cacheHashCode;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Alliance getPieceAlliance()
    {
        return this.pieceAlliance;
    }

    public boolean isFirstMove(){
     return  this.isFirstMove;
    }
    public abstract List<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return this.pieceType;
    }


}
