package org.cis120.minesweeper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EmptyCell extends Cell {

    private int numNeighboringMines; // the number of surrounding mines

    public EmptyCell(int r, int c, GameBoard g) {
        super(r, c, g);
        numNeighboringMines = g.numNeighboringMines(r, c);
    }

    public boolean isMine() {
        return false;
    }

    // When an empty cell is clicked, it is revealed
    public void leftClick() {
        GameBoard g = getBoard();
        if (isHidden()) {
            revealCell();
            g.increaseNumCellsRevealed();
            if (g.won()) {
                g.winGame();
            }
            // Recursive loop to reveal all neighbors with no neighboring mines
            if (numNeighboringMines == 0) {
                g.flood(getRow(), getCol());
            }
        }
        repaintCell();
    }

    // Draws this cell
    public void drawCell(Graphics g) {
        int xPos = getRow() * CELL_SIZE;
        int yPos = getCol() * CELL_SIZE;
        if (isHidden()) {
            // If the cell's state hidden, it is displayed as an empty cell
            BufferedImage hidden = makeCellImage("files/empty.png");
            g.drawImage(hidden, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        } else if (isFlagged()) {
            // If the cell's state is flagged, it is displayed as a flag
            BufferedImage flagged = makeCellImage("files/flag.png");
            g.drawImage(flagged, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        } else if (isRevealed()) {
            // If the cell's state is revealed, it is displayed as the number of neighboring mines
            BufferedImage number = makeCellImage("files/" + numNeighboringMines + ".png");
            g.drawImage(number, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        }
    }
}
