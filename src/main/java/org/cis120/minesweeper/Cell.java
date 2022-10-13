package org.cis120.minesweeper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Cell {

    public static final int CELL_SIZE = 40;

    private enum CurrentCellState {
        HIDDEN,
        REVEALED,
        FLAGGED,
    }
    private CurrentCellState state;
    private final int row;
    private final int col;
    private GameBoard gameBoard;

    public Cell(int r, int c, GameBoard b) {
        state = CurrentCellState.HIDDEN;
        row = r;
        col = c;
        gameBoard = b;
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public abstract void leftClick();

    // a right click flags the cell
    public void rightClick() {
        switch (state) {
            case HIDDEN:
                state = CurrentCellState.FLAGGED;
                gameBoard.increaseNumMinesFlagged();
                gameBoard.updateMinesLeftLabel();
                break;
            case FLAGGED:
                state = CurrentCellState.HIDDEN;
                gameBoard.decreaseNumMinesFlagged();
                gameBoard.updateMinesLeftLabel();
                break;
            default:
                break;
        }
        repaintCell();
    }

    public boolean isHidden() {
        return state == CurrentCellState.HIDDEN;
    }

    public boolean isRevealed() {
        return state == CurrentCellState.REVEALED;
    }

    public boolean isFlagged() {
        return state == CurrentCellState.FLAGGED;
    }

    // Sets the cell's state to revealed (when it is clicked)
    public void revealCell() {
        state = CurrentCellState.REVEALED;
    }

    public abstract boolean isMine();

    public abstract void drawCell(Graphics g);

    // Repaints the cell on the game board
    public void repaintCell() {
        Graphics g = gameBoard.getGraphics();
        drawCell(g);
    }

    // Helper function that uses image IO to draw the cells
    public static BufferedImage makeCellImage(String imageFileName) {
        BufferedImage cellImage = null;
        try {
            cellImage = ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
        }
        return cellImage;
    }
}
