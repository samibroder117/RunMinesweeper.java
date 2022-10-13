package org.cis120.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    // Game constants
    public static final int CELL_SIZE = 40;
    public static final int NUM_ROWS = 16;
    public static final int NUM_COLS = 16;
    public static final int NUM_MINES = 40;
    public static final int BOARD_WIDTH = CELL_SIZE * NUM_ROWS;
    public static final int BOARD_HEIGHT = CELL_SIZE * NUM_COLS;

    private Cell[][] board; // 2D array of cells that makes up the game board
    private int time; // the number of seconds the game has gone on for
    private int numMinesFlagged; // the number of mines flagged
    private int numRevealedCells; // the number of cells that have been revealed
    private boolean firstClickMade; // the game starts when the first click is made

    // updates the time display each second
    private Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            increaseTime();
        }
    });

    // the current state of the game
    private enum CurrentGameStatus {
        NOT_YET_STARTED,
        CURRENTLY_PLAYING,
        WON_GAME,
        LOST_GAME
    }
    private CurrentGameStatus status;

    private JLabel gameStatusLabel; // label to display the current game status
    private JLabel timerLabel; // label to display the timer
    private JLabel minesLeftLabel; // label to display the number of mines remaining


     // Initializes the game board
    public GameBoard(JLabel statusInit, JLabel timeInit, JLabel minesInit) {
        // Creates border around the game board
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the game board. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        board = new Cell[NUM_ROWS][NUM_COLS];
        time = 0;
        numMinesFlagged = 0;
        numRevealedCells = 0;
        firstClickMade = false;
        status = CurrentGameStatus.NOT_YET_STARTED;
        gameStatusLabel = statusInit;
        timerLabel = timeInit;
        minesLeftLabel = minesInit;
        updateStatusLabel();
        updateTimeLabel();
        updateMinesLeftLabel();
        repaint();

        // Listens for mouse clicks
        // Updates the model, then updates the game board based off of the updated model
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int xPos = e.getX();
                int yPos = e.getY();
                int rowOfClick = xPos / CELL_SIZE;
                int colOfClick = yPos / CELL_SIZE;

                // updates the model given the coordinates of the mouseclick
                if (SwingUtilities.isLeftMouseButton(e)) {
                    leftClick(rowOfClick, colOfClick);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    rightClick(rowOfClick, colOfClick);
                }
            }
        });
    }

    // Resets the game to its initial state
    public void reset() {
        board = new Cell[NUM_ROWS][NUM_COLS];
        time = 0;
        numMinesFlagged = 0;
        numRevealedCells = 0;
        firstClickMade = false;
        status = CurrentGameStatus.NOT_YET_STARTED;
        updateStatusLabel();
        updateTimeLabel();
        updateMinesLeftLabel();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    // Updates the board to reflect that the game has begun
    private void startGame() {
        addMineCells();
        addEmptyCells();
        storeBoard();
        status = CurrentGameStatus.CURRENTLY_PLAYING;
        updateStatusLabel();
        timer.start();
    }

    // Randomly places mine cells on the board
    private void addMineCells() {
        int mines = NUM_MINES;
        while (mines > 0) {
            Random random = new Random();
            int randRow = random.nextInt(NUM_ROWS);
            int randCol = random.nextInt(NUM_COLS);
            if (board[randRow][randCol] == null) {
                board[randRow][randCol] = new MineCell(randRow, randCol, this);
            }
            mines--;
        }
    }

    // Fills in the remaining spaces with empty cells
    private void addEmptyCells() {
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                if (board[r][c] == null) {
                    board[r][c] = new EmptyCell(r, c, this);
                }
            }
        }
    }

    // Checks if the given index exists on the board
    private boolean valid(int r, int c) {
        return (r >= 0 && r < NUM_ROWS && c >= 0 && c < NUM_COLS);
    }

    // Handles a left click on a cell
    public void leftClick(int r, int c) {
        if (!valid(r, c)) {
            return;
        }
        if (!firstClickMade) {
            firstClickMade = true;
            startGame();
        }
        Cell cell = board[r][c];
        if (cell == null) {
            return;
        }
        cell.leftClick();
    }

    // Handles a right click on a cell
    public void rightClick(int r, int c) {
        if (!valid(r, c)) {
            return;
        }
        if (!firstClickMade) {
            firstClickMade = true;
            startGame();
        }
        Cell cell = board[r][c];
        if (cell == null) {
            return;
        }
        cell.rightClick();
    }

    // returns a list of all the neighbor cells of a cell at a certain location
    public ArrayList<Cell> getNeighbors(int r, int c) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        // checks for top left diagonal neighbor
        if (r - 1 >= 0 && c - 1 >= 0) {
            neighbors.add(getCell(r - 1, c - 1));
        }
        // checks for top neighbor
        if (r - 1 >= 0) {
            neighbors.add(getCell(r - 1, c));
        }
        // checks for top right diagonal neighbor
        if (r - 1 >= 0 && c + 1 < NUM_COLS) {
            neighbors.add(getCell(r - 1, c + 1));
        }
        // checks for left neighbor
        if (c - 1 >= 0) {
            neighbors.add(getCell(r, c - 1));
        }
        // checks for right neighbor
        if (c + 1 < NUM_COLS) {
            neighbors.add(getCell(r, c + 1));
        }
        // checks for bottom left diagonal neighbor
        if (r + 1 < NUM_ROWS && c - 1 >= 0) {
            neighbors.add(getCell(r + 1, c - 1));
        }
        // checks for bottom neighbor
        if (r + 1 < NUM_ROWS) {
            neighbors.add(getCell(r + 1, c));
        }
        // checks for bottom right diagonal neighbor
        if (r + 1 < NUM_ROWS && c + 1 < NUM_COLS) {
            neighbors.add(getCell(r + 1, c + 1));
        }
        return neighbors;
    }

    // Returns the number of mines surrounding a cell at a certain location
    public int numNeighboringMines(int r, int c) {
        int count = 0;
        for (Cell cell: getNeighbors(r, c)) {
            if (cell != null && cell.isMine()) {
                count++;
            }
        }
        return count;
    }

    // Recursive loop with leftClick to reveal all neighbors of a cell with 0 mines
    public void flood(int r, int c) {
        if (board[r][c].isMine()) {
            return;
        }
        ArrayList<Cell> neighbors = getNeighbors(r, c);
        for (Cell cell: neighbors) {
            if (cell != null && cell.isHidden() && !cell.isMine()) {
                cell.leftClick();
            }
        }
    }

     // Updates the JLabel to reflect the current state of the game.
    public void updateStatusLabel() {
        gameStatusLabel.setText("Game Status: " + statusToString());
    }

    // Returns the current status of the game as a String
    public String statusToString() {
        String str = "";
        switch (status) {
            case NOT_YET_STARTED:
                str = "Game Has Not Been Started";
                break;
            case CURRENTLY_PLAYING:
                str = "Game Is In Progress";
                break;
            case WON_GAME:
                str = "You Won!";
                break;
            case LOST_GAME:
                str = "You Lost!";
                break;
            default:
                break;
        }
        return str;
    }

    // Updates the JLabel to reflect the timer of the game.
    public void updateTimeLabel() {
        timerLabel.setText("Time: " + time);
    }

    // Increases the timer
    private void increaseTime() {
        time++;
        updateTimeLabel();
    }

    // Updates the label displaying the number of mines remaining
    public void updateMinesLeftLabel() {
        int minesLeft = NUM_MINES - numMinesFlagged;
        minesLeftLabel.setText("Mines Left: " + minesLeft);
    }

    // Reveals all cells on the board (called when the player clicks a mine and the game is over)
    public void revealAll() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Cell cell = board[row][col];
                cell.revealCell();
            }
        }
    }

    // Checks if the player won (if they revealed all non-mine cells)
    public boolean won() {
        int totalEmptyCells = NUM_ROWS * NUM_COLS - NUM_MINES;
        return totalEmptyCells == numRevealedCells;
    }

    // Updates the board when the player wins the game
    public void winGame() {
        status = CurrentGameStatus.WON_GAME;
        updateStatusLabel();
        timer.stop();
        repaint();
    }

    // Updates the board when the player loses the game
    public void loseGame() {
        revealAll();
        status = CurrentGameStatus.LOST_GAME;
        updateStatusLabel();
        timer.stop();
        repaint();
    }

    // Returns a cell at a certain position on the board
    public Cell getCell(int r, int c) {
        return board[r][c];
    }

    // Increases the number of cells that have been revealed
    // (when there is a left click on a non-mine cell)
    public void increaseNumCellsRevealed() {
        numRevealedCells++;
    }

    // Increases the number of cells that have been flagged
    // (when there is a right click on a cell)
    public void increaseNumMinesFlagged() {
        numMinesFlagged++;
    }

    // Decreases the number of cells that have been flagged
    // (when there is a right click on a flagged cell)
    public void decreaseNumMinesFlagged() {
        numMinesFlagged--;
    }

    // Stores the current board in a text file
    public void storeBoard() {
        try {
            FileWriter fw = new FileWriter("files/previousBoard.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for (int col = 0; col < NUM_COLS; col++) {
                for (int row = 0; row < NUM_ROWS; row++) {
                    if (board[row][col].isMine()) {
                        bw.write('X');
                        bw.flush();
                    } else {
                        bw.write('O');
                        bw.flush();
                    }
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            return;
        }
    }

    // Reads in the previous board into a 2D array
    public char[][] getPreviousBoard() {
        char[][] prevBoard = new char[NUM_ROWS][NUM_COLS];
        try {
            FileReader fr = new FileReader("files/previousBoard.txt");
            BufferedReader br = new BufferedReader(fr);
            for (int row = 0; row < NUM_ROWS; row++) {
                String currRow = br.readLine();
                for (int col = 0; col < NUM_COLS; col++) {
                    prevBoard[row][col] = currRow.charAt(col);
                }
            }
            br.close();
        } catch (IOException e) {
        }
        return prevBoard;
    }

    public void replayPrevGame() {
        char[][] prevBoard = getPreviousBoard();
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = 0; row < NUM_COLS; row++) {
                char currCell = prevBoard[row][col];
                if (currCell == 'X') {
                    Cell mine = new MineCell(col, row, this);
                    board[col][row] = mine;
                } else {
                    Cell empty = new EmptyCell(col, row, this);
                    board[col][row] = empty;
                }
            }
        }
        time = 0;
        numMinesFlagged = 0;
        numRevealedCells = 0;
        firstClickMade = false;
        status = CurrentGameStatus.NOT_YET_STARTED;
        updateStatusLabel();
        updateTimeLabel();
        updateMinesLeftLabel();
        repaint();
        requestFocusInWindow();
    }

    // Draws the game board
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                Cell cell = board[r][c];
                if (cell == null) {
                    BufferedImage empty = Cell.makeCellImage("files/empty.png");
                    g.drawImage(empty, c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE,null);
                } else {
                    cell.drawCell(g);
                }
            }
        }
    }

     // Returns the size of the game board
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
