package com.chess.engine.board;


import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.*;
import com.chess.engine.player.Alliance;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;


import java.util.*;

public class Board {

    private  final List<Tile> gameBoard;
    private  final Collection<Piece> whitePieces;
    private  final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private Map<Integer, Integer> boardStateOccurrences = new HashMap<>();
    private final Pawn enPassantPawn;

    @Override
    public String toString() {
        final StringBuilder builder=new StringBuilder();
        for(int i = 0; i< BoardUtils.NUM_TILES; i++){
            final String tileText=this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)% BoardUtils.NUM_TILES_PER_ROW==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private  Board (final Builder builder){
        this.gameBoard=createGameBoard(builder);
        this.whitePieces=calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces=calculateActivePieces(this.gameBoard,Alliance.BLACK);
        this.enPassantPawn=builder.enPassantPawn;

        final Collection<Move> whiteStandardMoves =calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardMoves =calculateLegalMoves(this.blackPieces);

        this.whitePlayer= new WhitePlayer(this,whiteStandardMoves,blackStandardMoves);
        this.blackPlayer= new BlackPlayer(this,blackStandardMoves,whiteStandardMoves);
        this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);

    }
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    public  Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public  Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves= new ArrayList<>();
        for(Piece piece:pieces){
         legalMoves.addAll(piece.calculateLegalMoves(this));
        }
    return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,final Alliance alliance) {
        final List<Piece> activePieces=new ArrayList<>();
        for(final Tile tile:gameBoard){
            if(tile.IsTileOccupied()){
                final Piece piece=tile.getPiece();
                if(piece.getPieceAlliance()==alliance){

            activePieces.add(piece);
                }
            }
        }

        return activePieces;
    }

    public  Tile getTile(final int coordinate)
    {
        return  gameBoard.get(coordinate);
    }
    private  static List<Tile> createGameBoard(final  Builder builder){
        final Tile[] tiles =new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i< BoardUtils.NUM_TILES; i++){
                tiles[i]=Tile.CreateTile(i,builder.boardConfig.get(i));
        }

        return Arrays.asList(tiles);
    }
    public  static  Board createBoard(){

        final Builder builder = new Builder();
        //black layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));

        // White Layout

        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        Collection<Piece> allPieces = currentPlayer.getActivePieces(); // Obtain all pieces on the board

        Collection<Move> allLegalMoves = calculateLegalMoves(allPieces);
        allLegalMoves.addAll(currentPlayer.calculateKingCastles(currentPlayer.getLegalMoves(),currentPlayer.getOpponent().getLegalMoves()));

        return allLegalMoves;
    }
    public void updateBoardStateOccurrences() {
        int boardHash = this.getBoardHash();
        this.boardStateOccurrences.put(boardHash, this.boardStateOccurrences.getOrDefault(boardHash, 0) + 1);
    }

    public int getBoardHash() {
        int hash = 7;
        for (Piece piece:this.currentPlayer().getActivePieces())
        {
            hash=piece.computeHashCode();
        }
        return hash;
    }

    public boolean isThreefoldRepetition() {
        for (Integer count : this.boardStateOccurrences.values()) {

            System.out.println(count);
            System.out.println(this.getBoardHash());
            System.out.println(this.boardStateOccurrences);
        }
        return false;
    }

    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    public static  class Builder{
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        Pawn enPassantPawn;
        public Builder(){
            this.boardConfig=new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;

        }
        public  Builder setMoveMaker(final  Alliance nextMoveMaker){
            this.nextMoveMaker=nextMoveMaker;
            return this;
        }

      public Board build(){
          return new Board(this);
      }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn=enPassantPawn;
        }
    }
}
