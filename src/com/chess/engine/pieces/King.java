package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.List;


public class King extends  Piece{

    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={-9,-8,-7,-1,1,7,8,9};

    public King(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KING,piecePosition, pieceAlliance,true);
    }
    public King(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.KING,piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves=new ArrayList<>();


        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){

           final int candidateDestinationCoordinate=this.piecePosition +currentCandidateOffset;

            if(isFirstColumExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

           if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
               final Tile candidateDestinationTile= board.getTile(candidateDestinationCoordinate);
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
        return legalMoves;
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }


    private  static  boolean isFirstColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.FIRST_COLUM[currentPosition]&&(candidateOffset==-9||candidateOffset==-1||
                candidateOffset==7);
    }
    private static boolean isEightColumExclusion(final int currentPosition,final int candidateOffset)
    {
        return BoardUtils.EIGHTH_COLUM[currentPosition]&&(candidateOffset==-7 || candidateOffset==1||
                candidateOffset==9);
    }
}
