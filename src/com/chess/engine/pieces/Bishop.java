package com.chess.engine.pieces;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends  Piece{

    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={-9,-7,7,9};

    public Bishop(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance,true);
    }
    public Bishop(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.BISHOP,piecePosition, pieceAlliance,isFirstMove);
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
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumExclusion(final  int currentPosition, final int candidateOffset){
        return  BoardUtils.FIRST_COLUM[currentPosition] &&(candidateOffset==-9||candidateOffset == 7);
    }

    private static boolean isEightColumExclusion(final  int currentPosition,final int candidateOffset){
        return  BoardUtils.EIGHTH_COLUM[currentPosition] &&(candidateOffset==-7||candidateOffset == 9);
    }


}
