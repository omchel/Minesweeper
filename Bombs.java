/*
    CS335 Graphics and Multimedia
    Author: Chelina Ortiz Montanez
    Title: Program 1
    Description: Implementation of the Microsoft Minesweeper game. The player
        clicks on the cell, if it is a bomb the game is over, else it will contain
        a number describing the number of bombs that neighbor the cell.
    -- This class implements the Minesweeper game, that contains a Menu to restart/reset, exit
       or learn about the game, a JPanel with a mine count label, a timer and a restart button and
       the game board to play the game.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Bombs extends JFrame implements ActionListener {
    private Board gameBoard; // Core game play objects
    private JLabel bombLabel, timerLabel; // Labels to display game info
    private JPanel boardView, labelView; // layout objects: Views of the board and the label area
    private JButton restart; // Reset/restart button
    private Timer gameTimer; // Timer
    private Container c;

    // Menu bar items
    private JMenuBar menu;
    private JMenu game, setup, help;
    private JMenuItem newMenu, exit, begin, interm, expert, custom, about;

    private int gameTime = 1; // Record keeping for time
    private int size = 8, bombs= 15; // Initial size and number of bombs for the game

    // Description used for the "Help/About the game" menu
    private String description = "According to Wikipedia, The goal of the game is to uncover " +
            "all the squares that do not \ncontain mines without being \"blown up\" by clicking on " +
            "a square with a mine underneath.\n Click on as many slots as possible until you narrow " +
            "down the slots that contain bombs in it.";


    public Bombs() { // Game constructor
        super("Minesweeper");

        // Initialize the items of the menu bar
        menu = new JMenuBar();
        game = new JMenu("Game");
        setup = new JMenu("Setup");
        help = new JMenu("Help");
        newMenu = new JMenuItem("New");
        exit = new JMenuItem("Exit");
        begin = new JMenuItem("Beginner");
        interm = new JMenuItem("Intermediate");
        expert = new JMenuItem("Expert");
        custom = new JMenuItem("Custom");
        about= new JMenuItem("About the game");

        // Hierarchy of the menu bar
        game.add(newMenu);
        game.add(exit);
        setup.add(begin);
        setup.add(interm);
        setup.add(expert);
        setup.add(custom);
        help.add(about);
        menu.add(game);
        menu.add(setup);
        menu.add(help);
        setJMenuBar(menu);

        // Action listener for the menu items
        newMenu.addActionListener(new ActionListener() { // Reset/restart game
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        exit.addActionListener(new ActionListener() { // Exit the game
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        begin.addActionListener(new ActionListener() { // Set the game to a beginner board
            public void actionPerformed(ActionEvent e) {
                size = 4;
                bombs = 4;
                restartGame();
            }
        });
        interm.addActionListener(new ActionListener() { // Set the game to an intermediate board
            public void actionPerformed(ActionEvent e) {
                size = 8;
                bombs = 15;
                restartGame();
            }
        });
        expert.addActionListener(new ActionListener() { // Set the game to an expert game
            public void actionPerformed(ActionEvent e) {
                size = 12;
                bombs = 40;
                restartGame();
            }
        });
        custom.addActionListener(new ActionListener() { // Set the game to a customized size and bomb count
            public void actionPerformed(ActionEvent e) {
                size = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose a grid size from 3 through 12"));
                bombs= Integer.parseInt(JOptionPane.showInputDialog(null, "Choose a number of bombs from 2 through 40"));
                restartGame();
            }
        });
        about.addActionListener(new ActionListener() { // Show information window about the game
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, description, "About this game", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Help was clicked");
            }
        });

        // Allocate the interface elements
        restart = new JButton(":)"); // Restart button
        timerLabel = new JLabel("Timer: 000", SwingConstants.CENTER);
        bombLabel = new JLabel("Mines: 0" + bombs, SwingConstants.CENTER);

        restart.addActionListener(new ActionListener() { // Resets the game using the current size
            public void actionPerformed(ActionEvent e) { // Restart button implementation
                restartGame();
            }
        });
        gameTimer = new Timer(1000, new ActionListener() { // Game timer
            public void actionPerformed(ActionEvent e) {
                gameTime++;
                if (gameTime < 10) {
                    timerLabel.setText("Timer: 00" + gameTime);
                }
                else if (gameTime < 100) {
                    timerLabel.setText("Timer: 0" + gameTime);
                } else {
                    timerLabel.setText("Timer: " + gameTime);
                }
            }
        });

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(size, bombs, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(size, size, 2, 2));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 3, 2, 2));
        labelView.add(bombLabel);
        labelView.add(restart);
        labelView.add(timerLabel);

        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView);

        setSize(400, 400); // Default window size
        setVisible(true);
    }

    /* Handle anything that gets clicked and that uses Bombs as an ActionListener */
    public void actionPerformed(ActionEvent e) {
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard) e.getSource();
        // Show the front of the card and disable its action listener.
        currCard.showFront();
        currCard.removeActionListener(this);

        if (gameTime == 1) { // If the timer has not started, start timer
            gameTimer.start();
        }

        if (currCard.getBombCounter() == 0){ // check if the selected slot is empty (to clear empty neighbors)
            gameBoard.clearSlots(currCard.getXLocation(), currCard.getYLocation());
        }

        if (currCard.id() == 1) { // Check if the slot selected contains a bomb
            gameTimer.stop();
            gameBoard.endGame();  // Remove action listeners of the rest of the grid
            restart.setText(":("); // Set restart button to sad face
        }

        if (gameBoard.gameOver()) { // check if all slots but the bombs are flipped
            gameTimer.stop();
            restart.setText("Game Over");
        }
    }
    // Reset variables, the board and start over
    private void restartGame() {
        // Reset control panel values
        gameTime = 1;
        timerLabel.setText("Timer: 000");
        bombLabel.setText("Mines: 0" + bombs);
        restart.setText(":)");
        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        c.revalidate(); // Revalidate the content pain to generate a new board
        gameBoard.resetBoard(size, bombs);
        setupBoard(size, bombs);
        gameBoard.fillBoardView(boardView);
        boardView.setLayout(new GridLayout(size, size, 2, 2));
        // Change the size of the window according to the size of the grid
        if (size >= 3 && size < 5) {
            setSize(300, 300);
        }else if (size >= 5 && size < 9) {
            setSize(400, 400);
        } else if (size >= 9 && size < 12) {
            setSize(650, 650);
        }
        setVisible(true);
    }

    // Method that is making a new board with custom size and with custom # of bombs
    public void setupBoard(int size, int bombs){
        gameBoard = new Board(size, bombs, this);
    }

    public static void main(String args[]) {
        Bombs M = new Bombs();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}