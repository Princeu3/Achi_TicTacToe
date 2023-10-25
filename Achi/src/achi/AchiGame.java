package achi;

// Author: - Prince Upadhyay // 
import java.util.Scanner;

public class AchiGame {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            GameBoard gameBoard = new GameBoard();
            char[] players = { 'X', 'O' };
            int currentPlayerIndex = 0;
            int stonesPlaced = 0;
            boolean dropPhase = true;

            System.out.println("Drop phase of Achi");
            System.out.println("Enter moves in the format 'n m': ");

            while (true) {
                char currentPlayer = players[currentPlayerIndex];
                if (dropPhase) {
                    System.out.print("What move do you make? ");
                    int row = scanner.nextInt();
                    int col = scanner.nextInt();
                    if (isValidDrop(gameBoard, row, col)) {
                        gameBoard.placeStone(row, col, currentPlayer);
                        stonesPlaced++;

                        if (stonesPlaced == 8) {
                            dropPhase = false;
                            currentPlayerIndex = 0; // Reset currentPlayerIndex for the Move Phase
                        } else {
                            currentPlayerIndex = (currentPlayerIndex + 1) % 2; // Switch to the next player
                        }

                        // Print the game board after a valid move in the drop phase
                        printBoard(gameBoard);
                    } else {
                        System.out.println("That's not a legal move. Please try again.");
                    }

                } else {
                    System.out.println("Move phase of Achi");
                    System.out.print("What move do you make?");
                    int fromRow = scanner.nextInt();
                    int fromCol = scanner.nextInt();
                    if (isValidMove(gameBoard, fromRow, fromCol, currentPlayer)) {
                        // Find the first available empty spot and move the piece there
                        boolean moved = false;
                        for (int toRow = 0; toRow < 3 && !moved; toRow++) {
                            for (int toCol = 0; toCol < 3 && !moved; toCol++) {
                                if (gameBoard.isEmpty(toRow, toCol)) {
                                    gameBoard.moveStone(fromRow, fromCol, toRow, toCol);
                                    moved = true;
                                }
                            }
                        }

                        // Print the game board after a valid move in the move phase
                        printBoard(gameBoard);

                        // Switch to the next player after a valid move
                        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }
            }
        }
    }

    private static void printBoard(GameBoard gameBoard) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char owner = gameBoard.getOwner(i, j);
                if (owner == ' ') {
                    System.out.print("- ");
                } else {
                    System.out.print(owner + " ");
                }
            }
            System.out.println();
        }
    }

    private static boolean isValidDrop(GameBoard gameBoard, int row, int col) {
        return gameBoard.isEmpty(row, col);
    }

    private static boolean isValidMove(GameBoard gameBoard, int fromRow, int fromCol, char currentPlayer) {
        GameCell cell = gameBoard.getGameCells()[fromRow][fromCol];
        return cell.getOwner() == currentPlayer;
    }
}
