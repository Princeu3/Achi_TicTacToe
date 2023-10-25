
package ticTacToe;

import java.util.Scanner;

public class TicTacToe {

    private static Scanner getInput;
    private static GameBoard gameBoard;
    private static int whoseTurnIsIt;    // 1 for player 1, -1 for player 2
    private static int movesMade; // To keep track of moves made in the Drop Phase

    /** Driver for the program. */
    public static void main(String[] args) {
        initializeGameVariables();
        System.out.println("Drop phase of Achi");
        System.out.println("Enter moves in the format 'n m': ");
        dropPhase(); // Start the Drop Phase

        System.out.println("Move phase of Achi");
        movePhase(); // Start the Move Phase
    }

    public static int getWhoseTurnIsIt() {
        return whoseTurnIsIt;
    }

    /** Create initial values for the Scanner, the gameboard, and Variables */
    private static void initializeGameVariables() {
        getInput = new Scanner(System.in);
        gameBoard = new GameBoard();
        whoseTurnIsIt = 1;
        movesMade = 0;
    }

    /** Change the player whose turn it is. */
    public static void changePlayer() {
        whoseTurnIsIt *= -1;
    }

    /** Manage the drop phase of the game */
    private static void dropPhase() {
        while (movesMade < 8) {
            System.out.print("What move do you make? ");
            int row = getInput.nextInt();
            int col = getInput.nextInt();

            // Check if the move is legal in the Drop Phase
            if (gameBoard.isDropPhaseMoveLegal(row, col)) {
                dropPhaseMove(row, col);
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    /** Manage the move phase of the game */
    private static void movePhase() {
        while (true) {
            System.out.print("What move do you make? ");
            int row = getInput.nextInt();
            int col = getInput.nextInt();

            // Check if the move is legal in the Move Phase
            if (gameBoard.isMoveLegalInTheMovePhase(row, col)) {
               movePhaseMove(row, col);
            } else {
                System.out.println("That's not a legal move. Please try again.");
            }
        }
    }

    /** Make one move in the Drop Phase */
    private static void dropPhaseMove(int row, int col) {
        gameBoard.dropPhasePutPieceAtLocation(row, col);
        movesMade++;
        System.out.print("The current board is:\n" + gameBoard);
        changePlayer();
    }

    /** Make one move in the Move Phase */
    private static void movePhaseMove(int row, int col) {
        gameBoard.movePieceFromLocation(row, col);
        System.out.print("The current board is:\n" + gameBoard);
        changePlayer();
    }
}
