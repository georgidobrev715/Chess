package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.List;

public class Queen extends  Piece{

    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={-9,-8,-7,-1,1,7,8,9};

    public Queen(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance,true);
    }
    public Queen(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.QUEEN,piecePosition, pieceAlliance,isFirstMove);
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
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
    private static boolean isFirstColumExclusion(final  int currentPosition,final int candidateOffset){
        return  BoardUtils.FIRST_COLUM[currentPosition] &&(candidateOffset ==-1 ||candidateOffset ==-9||candidateOffset == 7);
    }

    private static boolean isEightColumExclusion(final  int currentPosition,final int candidateOffset){
        return  BoardUtils.EIGHTH_COLUM[currentPosition] &&(candidateOffset == 1||candidateOffset ==-7||candidateOffset == 9);
    }


}
