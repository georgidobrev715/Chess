package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;

    private final MoveLog moveLog;
    private final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;

    private final GameSetUp gameSetUp;
    private  Board chessBoard;
    private BoardDirection boardDirection;
    private Move computerMove;
    private boolean highlightLegalMoves;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final  static Dimension BOARD_PANEL_DIMENSION =  new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final static String imagePath="src\\resources\\icons\\";
    private final Color lightTileColor = Color.decode("#eeeed2");
    private final Color darkTileColor = Color.decode("#769656");

    private static final Table INSTANCE= new Table();

    private Table(){
        this.gameFrame=new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar= createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard=Board.createBoard();
        this.gameHistoryPanel= new GameHistoryPanel();
        this.boardPanel=new BoardPanel();
        this.moveLog=new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetUp= new GameSetUp(this.gameFrame,true);
        this.boardDirection=BoardDirection.NORMAL;
        this.highlightLegalMoves=false;
        this.gameFrame.add(this.gameHistoryPanel,BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }
    public static Table get(){
        return INSTANCE;
    }
    public void show(){
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard,Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }
    private void checkAndNotifyGameState() {
        Board currentBoard = Table.get().getGameBoard();
        if (currentBoard.currentPlayer().isInCheckMate()) {
            JOptionPane.showMessageDialog(gameFrame, "Game over: " + currentBoard.currentPlayer().getAlliance() + " is in checkmate.");
        } else if (currentBoard.currentPlayer().isInStaleMate()) {
            JOptionPane.showMessageDialog(gameFrame, "Game over: " + currentBoard.currentPlayer().getAlliance() + " is in stalemate.");
        } else if (currentBoard.isThreefoldRepetition()) {
            JOptionPane.showMessageDialog(gameFrame, "Draw by repetition: ");
        }

    }

    private GameSetUp getGameSetup(){
        return this.gameSetUp;
    }
    private Board getGameBoard(){
        return this.chessBoard;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu= new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN file");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open pgn file");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem exitMenu= new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenu);
        return fileMenu;
    }
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu=new JMenu("Preferences");
        final JMenuItem flippedBoardMenu=new JMenuItem("FLIP BOARD");
        flippedBoardMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection=boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);

            }
        });
        preferencesMenu.add(flippedBoardMenu);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlightCheckBox=new JCheckBoxMenuItem("Highlight legal moves",false);
        legalMoveHighlightCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves= legalMoveHighlightCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlightCheckBox);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){
        final JMenu optionsMenu=new JMenu("Options");
        final JMenuItem setupGameMenuItem= new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }


    private void setupUpdate(final GameSetUp gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }
    private class TableGameAIWatcher implements  Observer{

        @Override
        public void update(final Observable o,final Object arg) {
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer())&&
            !Table.get().getGameBoard().currentPlayer().isInCheckMate()&&
           !Table.get().getGameBoard().currentPlayer().isInStaleMate()){

                final AIThinkTank thinkTank= new AIThinkTank();
                thinkTank.execute();
            }
            checkAndNotifyGameState();
        }
    }
    public void updateGameBoard(final Board board){
        this.chessBoard=board;
    }
    public void updateComputerMove(final Move move){
        this.computerMove=move;
    }
    private MoveLog getMoveLog(){
        return this.moveLog;
    }
    private GameHistoryPanel getGameHistoryPanel(){
        return this.gameHistoryPanel;
    }
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    private void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }

    private  class AIThinkTank extends SwingWorker<Move,String>{
        private AIThinkTank(){

        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax= new MiniMax(gameSetUp.getSearchDepth());
            final Move bestMove=miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }
        @Override
        public void done(){
            try {
                final Move bestMove=get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                List<TilePanel> reversedList = new ArrayList<>(boardTiles);
                Collections.reverse(reversedList);
                return reversedList;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }


    private class BoardPanel extends JPanel{
        final List<TilePanel>boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles=new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel= new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        public void drawBoard(final Board board){
          removeAll();
            for(final TilePanel tilePanel:boardDirection.traverse(boardTiles)){
               tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog{
        private final List<Move> moves;
        MoveLog(){
            this.moves=new ArrayList<>();
        }
        public List<Move>getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
           this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(Move move){
            return this.moves.remove(move);
        }

    }
    enum PlayerType{
        HUMAN,
        COMPUTER
    }
    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();

            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        // Resetting selection
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            // First click: selecting the source tile
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                System.out.println("No piece selected.");
                                sourceTile = null;
                            } else {
                                System.out.println("Selected piece at " + sourceTile.getTileCoordinate());
                            }
                        } else {
                            // Second click: selecting the destination tile and attempting a move
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);


                            if (transition.getMoveStatus().isDone()) {
                                System.out.println("Move made to " + destinationTile.getTileCoordinate());
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            } else {
                                System.out.println("Move failed to " + destinationTile.getTileCoordinate());
                            }

                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }

                        SwingUtilities.invokeLater(() -> {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            if(gameSetUp.isAIPlayer(chessBoard.currentPlayer())){
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }

                            boardPanel.drawBoard(chessBoard);
                            checkAndNotifyGameState();

                        });
                    }
                }


                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });


            validate();

        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).IsTileOccupied()){
                try {
                    final BufferedImage image=ImageIO.read(new File(imagePath+board.getTile(this.tileId).getPiece().getPieceAlliance().
                            toString().charAt(0)+board.getTile(this.tileId).getPiece().toString()+".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            validate();
            repaint();

        }
        private Color darkerColor(Color color, float factor) {
            int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
            int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
            int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
            return new Color(r, g, b);
        }

        private void highlightLegals(final Board board) {
            if (highlightLegalMoves) {
                Color baseColor = this.getBackground();
                Color highlightColor = darkerColor(baseColor, 0.2f);

                for (final Move move : pieceLegalMove(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        this.setBackground(highlightColor);
                    }

                }
            }
        }
        private Collection<Move> pieceLegalMove(final Board board){
            List<Move> moves;
            if(humanMovedPiece !=null&&humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()){
                if(humanMovedPiece instanceof King){
                    moves=humanMovedPiece.calculateLegalMoves(board);
                    moves.addAll(board.currentPlayer().calculateKingCastles(board.currentPlayer().getLegalMoves(),
                            board.currentPlayer().getOpponent().getLegalMoves()));
                    return moves;

                }
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        private void assignTileColor() {
            if(BoardUtils.EIGHT_RANK[this.tileId]||
               BoardUtils.SIXTH_RANK[this.tileId]||
               BoardUtils.FOURTH_RANK[this.tileId]||
               BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId% 2 == 0 ? lightTileColor:darkTileColor);
            }else if(BoardUtils.SEVENTH_RANK[tileId]||
                    BoardUtils.FIFTH_RANK[tileId]||
                    BoardUtils.THIRD_RANK[tileId]||
                    BoardUtils.FIRST_RANK[tileId]){
                setBackground(this.tileId% 2 != 0 ? lightTileColor:darkTileColor);
            }
        }
    }

}
