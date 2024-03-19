package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.List;

public class Rook extends  Piece{

    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={-8,-1,1,8};

    public Rook(final  Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.ROOK,piecePosition, pieceAlliance,true);
    }
    public Rook(final  Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.ROOK,piecePosition, pieceAlliance,isFirstMove);
    }
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves= new ArrayList<>();

        for(final int candidateCoordinationOffset:CANDIDATE_MOVE_COORDINATE ){

            int candidateDestinationCoordinate=this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){


                if(isFirstColumExclusion(candidateDestinationCoordinate,candidateCoordinationOffset)||
                        isEightColumExclusion(candidateDestinationCoordinate,candidateCoordinationOffset)){
                    break;
                }

                candidateDestinationCoordinate+=candidateCoordinationOffset;

                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

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
                        break;

                    }
                }
            }

        }




        return legalMoves;
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
    private static boolean isFirstColumExclusion(final  int currentPosition,final int candidateOffset){
        return  BoardUtils.FIRST_COLUM[currentPosition] &&(candidateOffset==-1);
    }

    private static boolean isEightColumExclusion(final  int currentPosition,final int candidateOffset){
        return  BoardUtils.EIGHTH_COLUM[currentPosition] &&(candidateOffset==1);
    }

}
