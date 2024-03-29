package com.chess.gui;


import com.chess.engine.player.Alliance;
import com.chess.engine.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameSetUp extends JDialog {

        private Table.PlayerType whitePlayerType;
        private Table.PlayerType blackPlayerType;
        private JSpinner searchDepthSpinner;

        private static final String HUMAN_TEXT = "Human";
        private static final String COMPUTER_TEXT = "Computer";

        GameSetUp(final JFrame frame,
                  final boolean modal) {
            super(frame, modal);
            final JPanel myPanel = new JPanel(new GridLayout(0, 1));
            final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
            final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
            final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
            final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
            whiteHumanButton.setActionCommand(HUMAN_TEXT);
            final ButtonGroup whiteGroup = new ButtonGroup();
            whiteGroup.add(whiteHumanButton);
            whiteGroup.add(whiteComputerButton);
            whiteHumanButton.setSelected(true);

            final ButtonGroup blackGroup = new ButtonGroup();
            blackGroup.add(blackHumanButton);
            blackGroup.add(blackComputerButton);
            blackHumanButton.setSelected(true);

            getContentPane().add(myPanel);
            myPanel.add(new JLabel("White"));
            myPanel.add(whiteHumanButton);
            myPanel.add(whiteComputerButton);
            myPanel.add(new JLabel("Black"));
            myPanel.add(blackHumanButton);
            myPanel.add(blackComputerButton);

            myPanel.add(new JLabel("Search"));
            this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(4, 0, Integer.MAX_VALUE, 1));

            final JButton cancelButton = new JButton("Cancel");
            final JButton okButton = new JButton("OK");

            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    whitePlayerType = whiteComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
                    blackPlayerType = blackComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
                    GameSetUp.this.setVisible(false);
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Cancel");
                    GameSetUp.this.setVisible(false);
                }
            });

            myPanel.add(cancelButton);
            myPanel.add(okButton);

            setLocationRelativeTo(frame);
            pack();
            setVisible(false);
        }

        void promptUser() {
            setVisible(true);
            repaint();
        }

        boolean isAIPlayer(final Player player) {
            if(player.getAlliance() == Alliance.WHITE) {
                return getWhitePlayerType() == Table.PlayerType.COMPUTER;
            }
            return getBlackPlayerType() == Table.PlayerType.COMPUTER;
        }

        Table.PlayerType getWhitePlayerType() {
            return this.whitePlayerType;
        }

        Table.PlayerType getBlackPlayerType() {
            return this.blackPlayerType;
        }

        private static JSpinner addLabeledSpinner(final Container c,
                                                  final String label,
                                                  final SpinnerModel model) {
            final JLabel l = new JLabel(label);
            c.add(l);
            final JSpinner spinner = new JSpinner(model);
            l.setLabelFor(spinner);
            c.add(spinner);
            return spinner;
        }

       public int getSearchDepth() {
            return (Integer)this.searchDepthSpinner.getValue();
        }
    }


