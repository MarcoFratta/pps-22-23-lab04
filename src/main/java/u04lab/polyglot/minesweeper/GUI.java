package u04lab.polyglot.minesweeper;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import u04lab.polyglot.minesweeper.*;

public class GUI extends JFrame {
    
    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton,Pair<Integer,Integer>> buttons = new HashMap<>();
    private final Logics logics;
    
    public GUI(final int size, final int mines) {
        this.logics = new LogicsImpl(size, mines);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        
        final JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        final ActionListener onClick = (e)->{
            final JButton bt = (JButton)e.getSource();
            final Pair<Integer,Integer> pos = this.buttons.get(bt);

            final boolean aMineWasFound = this.logics.hit(pos.getX(), pos.getY()); // call the logic here to tell it that cell at 'pos' has been seleced
            if (aMineWasFound) {
                this.quitGame();
                JOptionPane.showMessageDialog(this, "You lost!!");
            } else {
                this.drawBoard();
            }
            final boolean isThereVictory = this.logics.isWin(); // call the logic here to ask if there is victory
            if (isThereVictory){
                this.quitGame();
                JOptionPane.showMessageDialog(this, "You won!!");
                System.exit(0);
            }
        };

        final MouseInputListener onRightClick = new MouseInputAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final JButton bt = (JButton)e.getSource();
                if (bt.isEnabled()){
                    final Pair<Integer,Integer> pos = GUI.this.buttons.get(bt);
                    GUI.this.logics.flag(pos.getX(), pos.getY());
                }
                GUI.this.drawBoard();
            }
        };
                
        for (int i=0; i < size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                jb.addActionListener(onClick);
                jb.addMouseListener(onRightClick);
                this.buttons.put(jb,new Pair<>(i,j));
                panel.add(jb);
            }
        }
        this.drawBoard();
        this.setVisible(true);
    }
    
    private void quitGame() {
        this.drawBoard();
    	for (final var entry: this.buttons.entrySet()) {
            final var p = entry.getValue();
            if(this.logics.hasMine(p.getX(), p.getY())) {
                entry.getKey().setText("*");
            } else {
                entry.getKey().setText("");
            }
            entry.getKey().setEnabled(false);
    	}
    }

    private void drawBoard() {
        for (final var entry: this.buttons.entrySet()) {
            final var p = entry.getValue();
            final var button = entry.getKey();
            final var adjacentMines = this.logics.getAdjacentMines(p.getX(), p.getY());
            if(adjacentMines.isPresent()) {
                button.setText(String.valueOf(adjacentMines.get()));
                button.setEnabled(false);
            } else if (this.logics.hasFlag(p.getX(), p.getY())) {
                button.setText("F");
            } /*else if(this.logics.hasMine(p.getX(), p.getY())) {
                entry.getKey().setText("*");
            }*/ else {
                button.setText("");
            }
        }
    	}

}
