package org.cis120.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunMinesweeper implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);

        final JLabel status = new JLabel("Game Status: Game Has Not Been Started");
        status.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        status_panel.add(status);

        // Displays the timer
        final JLabel timer = new JLabel("Time: 0");
        timer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        status_panel.add(timer);

        // Displays the number of mines left to find
        final JLabel minesLeft = new JLabel("Mines Left: 40");
        minesLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        status_panel.add(minesLeft);

        // Game board
        final GameBoard board = new GameBoard(status, timer, minesLeft);
        frame.add(board, BorderLayout.CENTER);

        // Control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // Reads in a file that contains the game instructions
        BufferedReader reader = null;
        String instructionsText = "";
        try {
            reader = new BufferedReader(new FileReader("files/instructions.txt"));
            String nextLine = reader.readLine();
            while (nextLine != null) {
                instructionsText += nextLine + "\n";
                nextLine = reader.readLine();
            }
        } catch (IOException e) {
        }
        // Game instructions button
        final JButton instructions = new JButton("How to Play");
        String finalInstructionsText = instructionsText;
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, finalInstructionsText);
            }
        });
        control_panel.add(instructions);

        // High scores button
        final JButton replay = new JButton("Replay");
        replay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.replayPrevGame();
            }
        });
        control_panel.add(replay);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}