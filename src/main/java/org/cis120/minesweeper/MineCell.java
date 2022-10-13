package org.cis120.minesweeper;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MineCell extends Cell {

    public MineCell(int r, int c, GameBoard g) {
        super(r, c, g);
    }

    // When a mine is clicked, the game is over
    public void leftClick() {
        GameBoard g = getBoard();
        g.loseGame();
        repaintCell();
    }

    public boolean isMine() {
        return true;
    }

    // Draws this cell
    public void drawCell(Graphics g) {
        int xPos = getRow() * CELL_SIZE;
        int yPos = getCol() * CELL_SIZE;
        if (isHidden()) {
            // If the cell's state hidden, it is displayed as an empty cell
            BufferedImage hidden = makeCellImage("files/empty.png");
            g.drawImage(hidden, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        } else if (isRevealed()) {
            // If the cell's state is revealed, it is displayed as a mine
            BufferedImage revealed = makeCellImage("files/mine.png");
            g.drawImage(revealed, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        } else if (isFlagged()) {
            // If the cell's state is flagged, it is displayed as a flag
            BufferedImage flagged = makeCellImage("files/flag.png");
            g.drawImage(flagged, xPos, yPos, CELL_SIZE, CELL_SIZE, null);
        }
    }
}
