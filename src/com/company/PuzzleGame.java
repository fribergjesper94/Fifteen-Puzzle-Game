package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static javax.swing.JOptionPane.*;

public class PuzzleGame extends JFrame implements ActionListener {

    private final JButton newGame = new JButton("New game");
    private final JButton solveGame = new JButton("Solve puzzle");
    private final JFrame frame = new JFrame();
    private final JPanel panel = new JPanel();
    private JButton[][] buttonNew = new JButton[xyCord][xyCord];
    private int lastEmptyX;
    private int lastEmptyY;
    private boolean swapMade = false;
    private boolean gameWon = false;
    private static final int xyCord = 4;
    private final int[] gameSize = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};


    PuzzleGame() {
        frame.add(panel);
        this.setTitle("Puzzle Game");
        add(panel);
        panel.add(addButtons());
        panel.add(newGame, BorderLayout.NORTH);
        panel.add(solveGame, BorderLayout.NORTH);
        newGame.addActionListener(this);
        solveGame.addActionListener(this);
        pack();
        setVisible(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //Lägger till nya knappar, används vid klick av knappen "New game"
    public void gameWindow() {
        gameWon = false;
        panel.add(addButtons());
        panel.add(newGame, BorderLayout.NORTH);
        panel.add(solveGame, BorderLayout.NORTH);
        setVisible(true);

    }

    //Metod som lägger till alla 15 knappar och en tom knapp
    public JPanel addButtons() {
        JPanel grid = new JPanel();
        int counter = 0;
        getGameSize(gameSize);
        buttonNew = new JButton[xyCord][xyCord];
        for (int y = 0; y < xyCord; y++) {
            for (int x = 0; x < xyCord; x++) {
                counter++;
                buttonNew[x][y] = new JButton(String.valueOf(gameSize[counter - 1]));
                buttonNew[x][y].setPreferredSize(new Dimension(80, 80));
                buttonNew[x][y].addActionListener(this);
                grid.setLayout(new GridLayout(xyCord, xyCord));
                grid.add(buttonNew[x][y], BorderLayout.SOUTH);
                setResizable(false);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);

                if (buttonNew[x][y].getText().equalsIgnoreCase("0")) {
                    lastEmptyX = x;
                    lastEmptyY = y;
                    buttonNew[x][y].setText("");
                }
            }

        }
        return grid;
    }

    //Slumpar ut värden på knapparna
    public int[] getGameSize(int[] gameSize) {
        Random random = new Random();
        for (int i = gameSize.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = gameSize[index];
            gameSize[index] = gameSize[i];
            gameSize[i] = temp;
        }
        return gameSize;
    }

    //Metod som lägger pusslet i ordning förutom de två sista knapparna
    public void solvePuzzle() {
        int counter = 1;
        for (int x = 0; x < xyCord; x++) {
            for (int y = 0; y < xyCord; y++) {
                if (counter < 15) {
                    buttonNew[y][x].setText(String.valueOf(counter));
                    counter += 1;
                }
            }
        }
        buttonNew[2][3].setText("");
        buttonNew[3][3].setText("15");
        lastEmptyX = 2;
        lastEmptyY = 3;
    }

    //Metod som kollar ifall brickorna ligger i ordning, jämförs med counter.
    public boolean checkIfWon() {
        int counter = 1;
        try {
            for (int x = 0; x < xyCord; x++) {
                for (int y = 0; y < xyCord; y++) {
                    if (buttonNew[y][x].getText() != "") {
                        if (Integer.parseInt(buttonNew[y][x].getText()) == counter) {
                            counter += 1;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("The puzzle board did not contain numbers");
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        swapMade = false;
        if (e.getSource() == newGame) {
            panel.removeAll();
            gameWindow();
        } else if (e.getSource() == solveGame) {
            solvePuzzle();


        } else {
            //Går igenom 2d arrayen med en double-for-loop för att flytta knapparna till nya positioner samt sätter den gamla till en tom ruta
            for (int x = 0; x < xyCord; x++) {
                for (int y = 0; y < xyCord; y++) {
                    if (buttonNew[y][x] == e.getSource() && swapMade == false && gameWon == false) {
                        if ((Math.abs(y - lastEmptyX) == 1 && Math.abs(x - lastEmptyY) == 0) || (Math.abs(x - lastEmptyY) == 1 && Math.abs(y - lastEmptyX) == 0)) {
                            buttonNew[lastEmptyX][lastEmptyY].setText(buttonNew[y][x].getText());
                            lastEmptyX = y;
                            lastEmptyY = x;
                            buttonNew[y][x].setText("");
                            swapMade = true;
                        }
                    }

                }

            }
            gameWon = checkIfWon();
            if (gameWon) {
                showMessageDialog(null, "Congratulations you won!");
            }
        }
    }


    public static void main(String[] args) {
        PuzzleGame puzzleGame = new PuzzleGame();
    }

}



