
import com.chess.engine.board.Board;
import com.chess.gui.Table;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Board board= Board.createBoard();
        System.out.println(board);
//        System.out.println(board.getTile(1).getPiece().getPieceAlliance().toString().charAt(0)+board.getTile(1).getPiece().toString());
          Table.get().show();
    }
}