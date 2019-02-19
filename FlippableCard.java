/*
    CS335 Graphics and Multimedia
    Author: Chelina Ortiz Montanez
    Title: Program 1
    Description: Implementation of the Microsoft Minesweeper game. The player
        clicks on the cell, if it is a bomb the game is over, else it will contain
        a number describing the number of bombs that neighbor the cell.
    -- This class implements the flippable card: an object which contains a front and back image,
        a bomb counter (for surrounding cells), a location on a grid (x,y coordinates) and an id.
 */
import javax.swing.*;

public class FlippableCard extends JButton {
    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    private Icon front; // Card front icon
    private Icon back = new ImageIcon(loader.getResource("res/Back.jpg")); // Card back image

    // ID, neighboring bomb counter and coordinates in grid
    private int id, bombCounter = 0, x = 0, y = 0;
    private boolean isShowingFront; // flag to show if the card has been flipped from back to front

    // Default constructor
    public FlippableCard() { super(); }

    // Constructor with card front initialization
    public FlippableCard(ImageIcon frontImage) {
        super();
        front = frontImage;
        super.setIcon(back);
        bombCounter = 0;
    }

    // Set the image used as the front of the card
    public void setFrontImage(ImageIcon frontImage) {
        front = frontImage;
    }

    // Card flipping functions
    public void showFront() {
        super.setIcon(front);
        isShowingFront = true;
    }

    public void hideFront() {
        super.setIcon(back);
        isShowingFront = false;
    }

    // Neighboring bomb counter set and get functions.
    public void increaseBombCounter() { bombCounter++; } // increase count by one if there is a neighboring bomb
    public int getBombCounter() { return bombCounter; }

    // Card location in grid set and get functions
    public void setCardLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getXLocation() { return x; }
    public int getYLocation() { return y; }

    // returns true if the card is showing the front image/has been flipped
    public boolean showingFront() {
        return isShowingFront;
    }

    // Metadata: ID number
    public int id() { return id; }
    public void setID(int i) { id = i; }
}
