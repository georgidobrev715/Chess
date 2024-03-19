package com.chess.engine.pieces;




import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.Alliance;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {

    private  static  final  int[] CANDIDATE_MOVE_COORDINATE ={7,8,9,16};

    public Pawn(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance,true);
    }
    public Pawn(final Alliance pieceAlliance,final int piecePosition,final boolean isFirstMove) {
        super(PieceType.PAWN,piecePosition, pieceAlliance,isFirstMove);
    }




    @Override
    public List<Move> calculateLegalMoves(final Board board) {

    final List<Move> legalMoves = new ArrayList<>();
        for(final  int currentCoordinateOffset:CANDIDATE_MOVE_COORDINATE) {
          final  int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection()* currentCoordinateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //Moving one tile forward
            if(currentCoordinateOffset==8 && !board.getTile(candidateDestinationCoordinate).IsTileOccupied()){
                if(this.pieceAlliance.isPawnOnPromotionSquare(candidateDestinationCoordinate)){
                    //pawn promotion
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,candidateDestinationCoordinate)));
                }else {
                    legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCoordinateOffset==16&&this.isFirstMove()&&((BoardUtils.SEVENTH_RANK[this.piecePosition]
                    && this.getPieceAlliance().isBlack())||
                    (BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite()))){
                final int behindCoordinateDestinationCandidate=this.piecePosition +(this.getPieceAlliance().getDirection()*8);
                if(!board.getTile(behindCoordinateDestinationCandidate).IsTileOccupied()&&
                    !board.getTile(candidateDestinationCoordinate).IsTileOccupied()){
                    legalMoves.add(new Move.PawnJump(board,this,candidateDestinationCoordinate));
                    //Moving two tiles forward

                }

            }else if(currentCoordinateOffset==7&&
                    !((BoardUtils.EIGHTH_COLUM[this.piecePosition]&&this.getPieceAlliance().isWhite() ||
                    (BoardUtils.FIRST_COLUM[this.piecePosition] && this.getPieceAlliance().isBlack())) ) ){
                if(board.getTile(candidateDestinationCoordinate).IsTileOccupied()){
                    final Piece pieceOnCoordinate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!= pieceOnCoordinate.getPieceAlliance()){
                        if(this.pieceAlliance.isPawnOnPromotionSquare(candidateDestinationCoordinate)){
                            //attack and promote
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCoordinate)));
                        }else {
                            legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate,pieceOnCoordinate));
                            //attacking move and exception
                        }
                    }
                } else if (board.getEnPassantPawn()!=null) {
                    if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceAlliance.getOppositeDirection()))){
                     final Piece pieceOnCandidate= board.getEnPassantPawn();
                     if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                         legalMoves.add(new Move.EnPassant(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                     }
                    }
                }
            }else if(currentCoordinateOffset==9&&
                    !((BoardUtils.EIGHTH_COLUM[this.piecePosition]&&this.getPieceAlliance().isBlack() ||
                    (BoardUtils.FIRST_COLUM[this.piecePosition] && this.getPieceAlliance().isWhite())) ) ){
                if(board.getTile(candidateDestinationCoordinate).IsTileOccupied()){
                    final Piece pieceOnCoordinate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!= pieceOnCoordinate.getPieceAlliance()){
                        if(this.pieceAlliance.isPawnOnPromotionSquare(candidateDestinationCoordinate)){
                          legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCoordinate)));
                        }else {
                            legalMoves.add(new Move.PawnAttackMove(board, this,candidateDestinationCoordinate, pieceOnCoordinate));
                            //attacking move and exception
                        }

                    }
                }else if (board.getEnPassantPawn()!=null) {
                    if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition-(this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate= board.getEnPassantPawn();
                        if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new Move.EnPassant(board,this,candidateDestinationCoordinate,pieceOnCandidate));
                        }
                    }
                }
            }

        }

        return legalMoves;
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
    public Piece getPromotionPiece(){
        return new Queen(this.pieceAlliance,this.piecePosition,false);
    }

}
