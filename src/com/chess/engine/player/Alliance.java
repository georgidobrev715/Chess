package com.chess.engine.player;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

public enum Alliance {
    WHITE{
        @Override
    public  int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection(){
            return 1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isPawnOnPromotionSquare(int position) {
            return BoardUtils.EIGHT_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK{
        @Override
      public   int getDirection() {
            return 1;
        }
        @Override
        public int getOppositeDirection(){
            return -1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isPawnOnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };


  public   abstract int getDirection();

  public abstract int getOppositeDirection();
  public  abstract  boolean isBlack();
  public  abstract  boolean isWhite();
  public abstract boolean isPawnOnPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
