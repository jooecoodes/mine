package main;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

// COVERED _MINE_CELL =  MINE_CELL + COVER_FOR_CELL
// MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL
// BOARD_WIDTH = N_COLS * CELL_SIZE + 1
// BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1

enum Settings{
	NUM_IMAGES(13),
	CELL_SIZE(35),
	COVER_FOR_CELL(10),
	MARK_FOR_CELL(10),
	EMPTY_CELL(0),
	MINE_CELL(9),
	COVERED_MINE_CELL(19),
	MARKED_MINE_CELL(29),
	DRAW_MINE(9),
	DRAW_COVER(10),
	DRAW_MARK(11),
	DRAW_WRONG_MARK(12),
	N_MINES(40),
	N_ROWS(16),
	N_COLS(16),
	BOARD_WIDTH(576),
	BOARD_HEIGHT(576);
	
    private int value;
 
    Settings(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

public class Board extends JPanel {
 
	//	globals
    private int[] field;
    private boolean inGame;
    private boolean showLost;
    private int minesLeft;
    private Image[] img;
    private Timer timer;
    private int timeElapsed;

    private int allCells;
    private final JLabel statusbar;

    public Board(JLabel statusbar) {
        this.statusbar = statusbar;
        initBoard();
        
    }

    private void initBoard() {

        setPreferredSize(new Dimension(Settings.BOARD_WIDTH.getValue(), Settings.BOARD_HEIGHT.getValue() + 8));
        
        
        img = new Image[Settings.NUM_IMAGES.getValue()];

        for (int i = 0; i < Settings.NUM_IMAGES.getValue(); i++) {
            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }
        

        JButton helpButton = new JButton("?");
        helpButton.setBounds(10, 565, 50, 20);
        helpButton.addActionListener(e -> showHelpFrame());
        this.setLayout(null);  // Enable absolute positioning
        this.add(helpButton);
        
        
        
        addMouseListener(new MinesAdapter());
        newGame();
    }
    
    
    private void newGame() {
        int cell;
        var random = new Random();
        inGame = true;
        minesLeft = Settings.N_MINES.getValue();
        allCells = Settings.N_ROWS.getValue() * Settings.N_COLS.getValue();
        field = new int[allCells];
        
        for (int i = 0; i < allCells; i++) {
            field[i] = Settings.COVER_FOR_CELL.getValue();
        }
        statusbar.setText("Flags left: " + minesLeft);
        timeElapsed = 0;  // Reset time for new game
        startTimer();  // Start the timer
       
        int i = 0;
        while (i < Settings.N_MINES.getValue()) {
        	
            int position = (int) (allCells * random.nextDouble());
            if ((position < allCells) && (field[position] != Settings.COVERED_MINE_CELL.getValue())) {
                int current_col = position % Settings.N_COLS.getValue();
                field[position] = Settings.COVERED_MINE_CELL.getValue();
                i++;
                
                updateSurroundingCells(position, current_col);
            }
        }
    }

    private void find_empty_cells(int j) {

        int current_col = j % Settings.N_COLS.getValue();
        int cell;

        if (current_col > 0) {
            cell = j - Settings.N_COLS.getValue() - 1;
            if (cell >= 0) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + Settings.N_COLS.getValue()- 1;
            if (cell < allCells) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - Settings.N_COLS.getValue();
        if (cell >= 0) {
            if (field[cell] > Settings.MINE_CELL.getValue()) {
                field[cell] -= Settings.COVER_FOR_CELL.getValue();
                if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + Settings.N_COLS.getValue();
        if (cell < allCells) {
            if (field[cell] > Settings.MINE_CELL.getValue()) {
                field[cell] -= Settings.COVER_FOR_CELL.getValue();
                if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (Settings.N_COLS.getValue() - 1)) {
            cell = j - Settings.N_COLS.getValue() + 1;
            if (cell >= 0) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + Settings.N_COLS.getValue() + 1;
            if (cell < allCells) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] == Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > Settings.MINE_CELL.getValue()) {
                    field[cell] -= Settings.COVER_FOR_CELL.getValue();
                    if (field[cell] ==  Settings.EMPTY_CELL.getValue()) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }
    private void showHelpFrame() {
    	  JFrame helpFrame = new JFrame("Game Instructions");
          helpFrame.setSize(300, 220);
          helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          helpFrame.setLocationRelativeTo(null);
          
          JTextArea instructions = new JTextArea(
              "Minesweeper Game Instructions:\n\n" +
              "1. Left-click to reveal a cell.\n" +
              "2. Right-click to place a flag on a suspected mine.\n" +
              "3. Avoid uncovering cells with mines.\n" +
              "4. The number in a cell indicates how many mines are adjacent to it.\n" +
              "5. Use logic to deduce safe cells.\n" +
              "6. Clear all safe cells to win. \n" +
              "7. Enjoy the game!\n" +
              "Good luck!"
          );
          instructions.setEditable(false);
          instructions.setLineWrap(true);
          instructions.setWrapStyleWord(true);

          JPanel panel = new JPanel();
          panel.setLayout(new java.awt.BorderLayout());
          panel.add(instructions, java.awt.BorderLayout.CENTER);

          helpFrame.add(panel);
          helpFrame.setVisible(true);
    }
    
    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeElapsed++;
            statusbar.setText("Flags left: " + minesLeft + " | Time: " + timeElapsed + "s");
        });
        timer.start();
    }
    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
    private void updateSurroundingCells(int position, int current_col) {
        int cell;
        // section 1
        if (current_col > 0) {
            cell = position - 1 - Settings.N_COLS.getValue(); // top left
            if (cell >= 0) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }
            cell = position - 1; // left
            if (cell >= 0) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }

            cell = position + Settings.N_COLS.getValue() - 1; // bottom left
            if (cell < allCells) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }
        }
        // section 2
        cell = position - Settings.N_COLS.getValue(); // top
        if (cell >= 0) {
            if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                field[cell] += 1;
            }
        }

        cell = position + Settings.N_COLS.getValue(); // bottom
        if (cell < allCells) {
            if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                field[cell] += 1;
            }
        }

        // section 3
        if (current_col < (Settings.N_COLS.getValue() - 1)) {
            cell = position - Settings.N_COLS.getValue() + 1;
            if (cell >= 0) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }
            cell = position + Settings.N_COLS.getValue() + 1;
            if (cell < allCells) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }
            cell = position + 1;
            if (cell < allCells) {
                if (field[cell] != Settings.COVERED_MINE_CELL.getValue()) {
                    field[cell] += 1;
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < Settings.N_ROWS.getValue(); i++) {
            for (int j = 0; j < Settings.N_COLS.getValue(); j++) {
                int cell = field[(i * Settings.N_COLS.getValue()) + j];
                if (inGame && cell == Settings.MINE_CELL.getValue()) {
                	showLost = true;
                    inGame = false;
                }
                if (!inGame) {
                    if (cell == Settings.COVERED_MINE_CELL.getValue()) {
                        cell = Settings.DRAW_MINE.getValue();
                    } else if (cell == Settings.MARKED_MINE_CELL.getValue()) {
                        cell = Settings.DRAW_MARK.getValue();
                    } else if (cell > Settings.COVERED_MINE_CELL.getValue()) {
                        cell = Settings.DRAW_WRONG_MARK.getValue();
                    } else if (cell > Settings.MINE_CELL.getValue()) {
                        cell = Settings.DRAW_COVER.getValue();
                    }
                } else {
                    if (cell > Settings.COVERED_MINE_CELL.getValue()) {
                        cell = Settings.DRAW_MARK.getValue();
                    } else if (cell > Settings.MINE_CELL.getValue()) {
                        cell = Settings.DRAW_COVER.getValue();
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * Settings.CELL_SIZE.getValue()),
                        (i * Settings.CELL_SIZE.getValue()), this);
            }
        }
        
        checkWin(uncover);
    }

    private void checkWin(int uncover) {
    	 // victory condition
        if (uncover == 0 && inGame) {
            inGame = false;
            stopTimer(); 
            SwingUtilities.invokeLater(() -> {
                int response = JOptionPane.showOptionDialog(
                        null, 
                        "Game Won! Time Elapsed: " + timeElapsed + " sec. Would you like to try again?", 
                        "Game Status", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE,
                        null, 
                        new Object[]{"Try Again", "Exit"}, 
                        "Try Again" 
                );

                if (response == JOptionPane.YES_OPTION) {
                    newGame();
                    repaint();
                } else {
                    System.exit(0);
                }
            });

        } else if (!inGame) {
        	stopTimer(); 
        	SwingUtilities.invokeLater(() -> {
        	    int response = JOptionPane.showOptionDialog(
        	            null, 
        	            "Game Over! Time elapsed: " + timeElapsed + " sec. Would you like to try again?", 
        	            "Game Status", 
        	            JOptionPane.YES_NO_OPTION, 
        	            JOptionPane.INFORMATION_MESSAGE, 
        	            null, 
        	            new Object[]{"Try Again", "Exit"}, 
        	            "Try Again" 
        	    );

        	    if (response == JOptionPane.YES_OPTION) {
        	        newGame();
        	        repaint();
        	    } else {
        	        System.exit(0);
        	    }
        	});
       
        } 
    }
 
    
    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int cCol = x / Settings.CELL_SIZE.getValue();
            int cRow = y / Settings.CELL_SIZE.getValue();

            boolean doRepaint = false;

            if ((x < Settings.N_COLS.getValue() * Settings.CELL_SIZE.getValue()) && (y < Settings.N_ROWS.getValue() * Settings.CELL_SIZE.getValue())) {
                if (e.getButton() == MouseEvent.BUTTON3) { // right mouse button clicked
                    if (field[(cRow * Settings.N_COLS.getValue()) + cCol] > Settings.MINE_CELL.getValue()) {
                        doRepaint = true;

                        if (field[(cRow * Settings.N_COLS.getValue()) + cCol] <= Settings.COVERED_MINE_CELL.getValue()) {
                            if (minesLeft > 0) {
                                field[(cRow * Settings.N_COLS.getValue()) + cCol] += Settings.MARK_FOR_CELL.getValue();
                                minesLeft--;
                                statusbar.setText("Flags left: " + minesLeft);
                            } else {
                                statusbar.setText("No flags left");
                            }
                        } else {
                            field[(cRow * Settings.N_COLS.getValue()) + cCol] -= Settings.MARK_FOR_CELL.getValue();
                            minesLeft++;
                            statusbar.setText("Flags left: " + minesLeft);
                        }
                    }
                } else { // left mouse button clicked
                    if (field[(cRow * Settings.N_COLS.getValue()) + cCol] > Settings.COVERED_MINE_CELL.getValue()) {
                        return;
                    }
                    if ((field[(cRow * Settings.N_COLS.getValue()) + cCol] > Settings.MINE_CELL.getValue()) && (field[(cRow * Settings.N_COLS.getValue()) + cCol] < Settings.MARKED_MINE_CELL.getValue())) {
                        field[(cRow * Settings.N_COLS.getValue()) + cCol] -= Settings.COVER_FOR_CELL.getValue();
                        doRepaint = true;

                        if (field[(cRow * Settings.N_COLS.getValue()) + cCol] == Settings.MINE_CELL.getValue()) {
                            inGame = false;
                        }
                        if (field[(cRow * Settings.N_COLS.getValue()) + cCol] == Settings.EMPTY_CELL.getValue()) {
                            find_empty_cells((cRow * Settings.N_COLS.getValue()) + cCol);
                        }
                    }
                }
                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}