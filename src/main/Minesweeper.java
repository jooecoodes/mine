package main;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Minesweeper extends JFrame {

    private JLabel statusbar;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        statusbar = new JLabel("Hello");
        add(statusbar, BorderLayout.SOUTH);

        // Initialize the board
        add(new Board(statusbar));

        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            var ex = new Minesweeper();
//            ex.setVisible(true);
//        });
        EventQueue.invokeLater(() -> {
            var ex = new MainMenu();
            ex.setVisible(true);
        });
    	 
    }
}

class MainMenu extends JFrame {
	 private JButton startButton;
	 private JButton helpButton;

	    public MainMenu() {
	        initUI();
	    }

	    private void initUI() {
	    	 // Create and set up the image label
	        ImageIcon icon = new ImageIcon("src/resources/9.png");
	        JLabel imageLabel = new JLabel(icon);

	        startButton = new JButton("Start Game");
	        startButton.addActionListener(e -> startGame());

	        helpButton = new JButton("Instructions");
	        helpButton.addActionListener(e -> showHelpFrame());

	        JPanel panel = new JPanel();
	        panel.setLayout(new GridBagLayout());  
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;  
	        gbc.gridy = 0;  
	        gbc.anchor = GridBagConstraints.CENTER; 
	        gbc.insets = new Insets(10, 10, 10, 10);  

	        panel.add(imageLabel, gbc);

	        gbc.gridy = 1; 
	        panel.add(startButton, gbc);

	        gbc.gridy = 2;  
	        panel.add(helpButton, gbc);

	        getContentPane().setLayout(new BorderLayout());
	        getContentPane().add(panel, BorderLayout.CENTER);
	        
	        // Set the window icon
	        setIconImage(new ImageIcon("src/resources/9.png").getImage()); 

	        setTitle("Minesweeper - Main Menu");
	        setSize(400, 400);
	        setLocationRelativeTo(null);  // Center the window on the screen
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }

	    private void createLayout(JComponent... args) {
	        var pane = getContentPane();
	        pane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	        for (var arg : args) {
	            pane.add(arg);
	        }
	        
	    }

	    private void startGame() {
	        // Close the main menu
	        this.setVisible(false);
	        
	        // Open the Minesweeper game window
	        var game = new Minesweeper();
	        game.setVisible(true);
	        game.setIconImage(new ImageIcon("src/resources/9.png").getImage()); // Path to your icon image file
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
}
