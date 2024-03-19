package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Knight extends  Piece{
    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={-17,-15,-10,-6,6,10,15,17};

    public Knight(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,true);
    }
    public Knight(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        int candidateDestinationCoordinate;
        final List<Move> legalMoves=new ArrayList<>();
        for(final  int currentCoordinateOffset:CANDIDATE_MOVE_COORDINATE)
        {
            candidateDestinationCoordinate=this.piecePosition+currentCoordinateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
            {

                if(isFirstColumExclusion(this.piecePosition,currentCoordinateOffset)||isSecondColumExclusion(this.piecePosition,currentCoordinateOffset)
               ||isSeventhColumExclusion(this.piecePosition,currentCoordinateOffset)||isEighthColumExclusion(this.piecePosition,currentCoordinateOffset) )
                {
                    continue;
                }
                final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.IsTileOccupied()) {
                legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                }else {
                    final  Piece pieceAtDestination=candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance= pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance!=pieceAlliance)
                    {
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);

    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
    private  static  boolean isFirstColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.FIRST_COLUM[currentPosition]&&(candidateOffset==-17||candidateOffset==-10||
                candidateOffset==6||candidateOffset==15);
    }
    private static boolean isSecondColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.SECOND_COLUM[currentPosition]&&(candidateOffset==-10 || candidateOffset==6);
    }
    private static boolean isSeventhColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.SEVENTH_COLUM[currentPosition]&&(candidateOffset==-6||candidateOffset==10);
    }
    private  static  boolean isEighthColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.EIGHTH_COLUM[currentPosition]&&(candidateOffset==-15|| candidateOffset==-6||
                candidateOffset==10||candidateOffset==17);
    }

}

