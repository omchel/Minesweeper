/*
    CS335 Graphics and Multimedia
    Author: Chelina Ortiz Montanez
    Title: Program 1
    Description: Implementation of the Microsoft Minesweeper game. The player
        clicks on the cell, if it is a bomb the game is over, else it will contain
        a number describing the number of bombs that neighbor the cell.
    -- This class implements the Board of the game that consists of a grid of flippable
        cards that are revealed as the game progresses and when the game is over

    ** Known issue: When clearing the empty cells, I was unable to find an easy way to turn
    **              all the concurrent slots that were also empty, I was only able to implement
    **              the clearing for the neighboring cells to the empty one that was clicked.
 */

import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Board {
    private int size, bombs; //size of the grid (it is squared) and number of bombs in the game
    private Random generator = new Random(); // Generates a random number, used to place the bombs

    private ActionListener AL;
    private FlippableCard[][] slots; // Array to hold board slots

    private ClassLoader loader = getClass().getClassLoader(); // Resource loader

    // Board constructor
    public Board(int size, int bombs, ActionListener AL) {
        this.size = size;
        this.bombs = bombs;
        this.AL = AL;
        slots = new FlippableCard[size][size]; // Allocate and configure the game board: a 2D array of slots

        initMatrix(); // Initializes the matrix of the 2D array of flippable cards

        int bombsMade = 0; // Counter that keeps track of the bombs created
        String imgPath1 = "res/bomb.jpg"; // Location of the bomb image
        String imgPath2 = "res/empty.jpg"; // Location of the empty slot image

        // Fill the slot array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ImageIcon img;
                img = new ImageIcon(loader.getResource(imgPath2)); // Set all cards to empty
                FlippableCard c = new FlippableCard(img); // Setup one card at a time
                c.setID(0); // ID = 0 for empty cards
                slots[i][j] = c; // Add card to the array
                c.addActionListener(AL); // Activate action listener
                c.setCardLocation(i, j); // Store the location of the card
            }
        }

        while (bombsMade < bombs) { // Load the appropriate front image from the resources folder
            int random1 = generator.nextInt(size);
            int random2 = generator.nextInt(size);
            if (slots[random1][random2].id() != 1) {
                ImageIcon img = new ImageIcon(loader.getResource(imgPath1));
                // Setup one card at a time
                //FlippableCard c = new FlippableCard(img);
                slots[random1][random2].setFrontImage(img);
                slots[random1][random2].setID(1);
                // Add them to the array  & Randomize the card positions
                slots[random1][random2].setFrontImage(img);
                // check neighbors and add to the count of neighboring bombs
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        // Check if the iterator is in bounds of the board
                        if ((random1 + i) >= 0 && (random1 + i) <= (size - 1) && (random2 + j) >= 0 && (random2 + j) <= (size - 1)) {
                            if (slots[random1 + i][random2 + j].id() != 1) { // Check if the card is not  a bomb
                                slots[random1 + i][random2 + j].increaseBombCounter(); // Increase the neighboring bomb counter
                                // Set the image of the neighboring bomb to the corresponding number (0 remains empty)
                                img = new ImageIcon(loader.getResource("res/" + (slots[random1 + i][random2 + j].getBombCounter()) + ".jpg"));
                                slots[random1 + i][random2 + j].setFrontImage(img); // Update the image
                                slots[random1 + i][random2 + j].setCardLocation(random1+i, random2+j); // Store the location of the bomb
                            }
                        }
                    }
                }
                bombsMade++; // Increase the bombs made counter
            }
        }
    }

    // Function that allows user to reset/restart the board even when the size or # bombs has changed
    public void resetBoard(int size, int bombs) {
        this.size = size;
        this.bombs = bombs;
        slots = new FlippableCard[size][size];

        initMatrix(); // Initializes the matrix of the 2D array of flippable cards

        int bombsMade = 0; // Counter that keeps track of the bombs created
        String imgPath1 = "res/bomb.jpg"; // Location of the bomb image
        String imgPath2 = "res/empty.jpg"; // Location of the empty slot image

        // Fill the slot array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ImageIcon img;
                img = new ImageIcon(loader.getResource(imgPath2)); // Set all cards to empty
                FlippableCard c = new FlippableCard(img); // Setup one card at a time
                c.setID(0); // ID = 0 for empty cards
                slots[i][j] = c; // Add card to the array
                c.addActionListener(AL); // Activate action listener
                c.setCardLocation(i, j); // Store the location of the card
            }
        }

        while (bombsMade < bombs) { // Load the appropriate front image from the resources folder
            int random1 = generator.nextInt(size);
            int random2 = generator.nextInt(size);
            if (slots[random1][random2].id() != 1) {
                ImageIcon img = new ImageIcon(loader.getResource(imgPath1));
                // Setup one card at a time
                //FlippableCard c = new FlippableCard(img);
                slots[random1][random2].setFrontImage(img);
                slots[random1][random2].setID(1);
                // Add them to the array  & Randomize the card positions
                slots[random1][random2].setFrontImage(img);
                // check neighbors and add to the count of neighboring bombs
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        // Check if the iterator is in bounds of the board
                        if ((random1 + i) >= 0 && (random1 + i) <= (size - 1) && (random2 + j) >= 0 && (random2 + j) <= (size - 1)) {
                            if (slots[random1 + i][random2 + j].id() != 1) { // Check if the card is not  a bomb
                                slots[random1 + i][random2 + j].increaseBombCounter(); // Increase the neighboring bomb counter
                                // Set the image of the neighboring bomb to the corresponding number (0 remains empty)
                                img = new ImageIcon(loader.getResource("res/" + (slots[random1 + i][random2 + j].getBombCounter()) + ".jpg"));
                                slots[random1 + i][random2 + j].setFrontImage(img); // Update the image
                                slots[random1 + i][random2 + j].setCardLocation(random1+i, random2+j); // Store the location of the bomb
                            }
                        }
                    }
                }
                bombsMade++; // Increase the bombs made counter
            }
        }
    }

    // Fill the board of flippable cards and make it visible for other classes
    public void fillBoardView(JPanel view) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                FlippableCard c = slots[i][j];
                c.hideFront(); // Start the game with all cards back image showing
                view.add(c);
            }
        }
    }

    // Initialize 2D array of Flippable cards
    public void initMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                FlippableCard c = new FlippableCard();
                slots[i][j] = c;
            }
        }
    }

    // Remove action listeners and show front image of the entire board when a bomb is clicked
    public void endGame() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                slots[i][j].removeActionListener(AL);
                slots[i][j].showFront();
            }
        }
    }

    // Reveal empty neighbors of slot clicked (**only direct neighbors of the slot clicked are revealed)
    public void clearSlots(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((x+i) >= 0 && (x+i) <= (size - 1) && (y+j) >= 0 && (y+j) <= (size - 1)) {
                    // Check if the neighbor is not a bomb and the bomb counter is greater or equal to zero
                    if (slots[x+i][y+j].getBombCounter() >= 0 && slots[x+i][y+j].id() == 0) {
                        slots[x+i][y+j].showFront();
                        slots[x+i][y+j].removeActionListener(AL);
                    }
                }
            }
        }
    }

    // Check if the all cards that have been flipped are not bombs (only bombs remain not flipped)
    public boolean gameOver() {
        int countFlipped = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (slots[i][j].showingFront() == true) { // Go through the array and count the flipped cards
                    countFlipped++;
                }
            }
        }
        if (countFlipped == ((size * size) - bombs)) { // Check if all the non-bomb cards are flipped
            return true;
        }
        return false;
    }
}