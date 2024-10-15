/* CSCI 204, Fall 2023
 * @author
 * Date: December 8th, 2023
 */

package achi;

public class GameBoard {

    private GameCell[][] gameCells; // The nine positions on the board
    private int rowOfEmptyCell; // What row is the (only) empty cell on?
    private int colOfEmptyCell; // What column is the (only) empty cell on?

    // Define the allowed connections between cells
    private int[][][] connections = {
        // Connections for cell (0,0)
        { {0,1}, {1,0}, {1,1} },
        // Connections for cell (0,1)
        { {0,0}, {0,2}, {1,1} },
        // Connections for cell (0,2)
        { {0,1}, {1,2}, {1,1} },
        // Connections for cell (1,0)
        { {0,0}, {1,1}, {2,0} },
        // Connections for cell (1,1)
        { {0,0}, {0,1}, {0,2}, {1,0}, {1,2}, {2,0}, {2,1}, {2,2} },
        // Connections for cell (1,2)
        { {0,2}, {1,1}, {2,2} },
        // Connections for cell (2,0)
        { {1,0}, {1,1}, {2,1} },
        // Connections for cell (2,1)
        { {2,0}, {1,1}, {2,2} },
        // Connections for cell (2,2)
        { {2,1}, {1,2}, {1,1} }
    };

    public GameBoard() {
        gameCells = new GameCell[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                gameCells[row][col] = new GameCell();
            }
        }
    }

    // Converts cell coordinates to index in the connections array
    private int cellIndex(int row, int col) {
        return row * 3 + col;
    }

    public boolean isDropPhaseMoveLegal(int row, int col) {
        return gameCells[row][col].getWhoOwnsMe() == 0;
    }

    public void dropPhasePutPieceAtLocation(int row, int col, int player) {
        gameCells[row][col].setWhoOwnsMe(player);
    }

    public void findEmptyCell() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameCells[row][col].getWhoOwnsMe() == 0) {
                    rowOfEmptyCell = row;
                    colOfEmptyCell = col;
                    return;
                }
            }
        }
    }

    public boolean isMovePhaseMoveLegal(int row, int col, int player) {
        if (gameCells[row][col].getWhoOwnsMe() != player) {
            return false;
        }

        // Get the empty cell coordinates
        int emptyRow = rowOfEmptyCell;
        int emptyCol = colOfEmptyCell;

        // Get the connections for the selected cell
        int[][] cellConnections = connections[cellIndex(row, col)];

        // Check if the empty cell is a valid move
        for (int[] connection : cellConnections) {
            if (connection[0] == emptyRow && connection[1] == emptyCol) {
                return true;
            }
        }

        return false;
    }

    public boolean canPlayerMove(int player) {
        // Iterate over all cells
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameCells[row][col].getWhoOwnsMe() == player) {
                    // Get the connections for this cell
                    int[][] cellConnections = connections[cellIndex(row, col)];

                    // Check if any connected cell is empty
                    for (int[] connection : cellConnections) {
                        int connRow = connection[0];
                        int connCol = connection[1];
                        if (gameCells[connRow][connCol].getWhoOwnsMe() == 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void movePieceFromLocation(int fromRow, int fromCol, int toRow, int toCol, int player) {
        gameCells[toRow][toCol].setWhoOwnsMe(player);
        gameCells[fromRow][fromCol].setWhoOwnsMe(0);
        rowOfEmptyCell = fromRow;
        colOfEmptyCell = fromCol;
    }

    public int getRowOfEmptyCell() {
        return rowOfEmptyCell;
    }

    public int getColOfEmptyCell() {
        return colOfEmptyCell;
    }

    public boolean checkRowWins(int player) {
        for (int row = 0; row < 3; row++) {
            if (gameCells[row][0].getWhoOwnsMe() == player &&
                    gameCells[row][1].getWhoOwnsMe() == player &&
                    gameCells[row][2].getWhoOwnsMe() == player) {
                return true;
            }
        }
        return false;
    }

    public boolean checkColWins(int player) {
        for (int col = 0; col < 3; col++) {
            if (gameCells[0][col].getWhoOwnsMe() == player &&
                    gameCells[1][col].getWhoOwnsMe() == player &&
                    gameCells[2][col].getWhoOwnsMe() == player) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDiagonalWins(int player) {
        // Main diagonal
        if (gameCells[0][0].getWhoOwnsMe() == player &&
                gameCells[1][1].getWhoOwnsMe() == player &&
                gameCells[2][2].getWhoOwnsMe() == player) {
            return true;
        }
        // Anti-diagonal
        if (gameCells[0][2].getWhoOwnsMe() == player &&
                gameCells[1][1].getWhoOwnsMe() == player &&
                gameCells[2][0].getWhoOwnsMe() == player) {
            return true;
        }
        return false;
    }

    public int[][] getWinningLine(int player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (gameCells[row][0].getWhoOwnsMe() == player &&
                    gameCells[row][1].getWhoOwnsMe() == player &&
                    gameCells[row][2].getWhoOwnsMe() == player) {
                return new int[][]{{row, 0}, {row, 1}, {row, 2}};
            }
        }
        // Check columns
        for (int col = 0; col < 3; col++) {
            if (gameCells[0][col].getWhoOwnsMe() == player &&
                    gameCells[1][col].getWhoOwnsMe() == player &&
                    gameCells[2][col].getWhoOwnsMe() == player) {
                return new int[][]{{0, col}, {1, col}, {2, col}};
            }
        }
        // Check main diagonal
        if (gameCells[0][0].getWhoOwnsMe() == player &&
                gameCells[1][1].getWhoOwnsMe() == player &&
                gameCells[2][2].getWhoOwnsMe() == player) {
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        // Check anti-diagonal
        if (gameCells[0][2].getWhoOwnsMe() == player &&
                gameCells[1][1].getWhoOwnsMe() == player &&
                gameCells[2][0].getWhoOwnsMe() == player) {
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                builder.append(gameCells[row][col].toString());
                if (col < 2) builder.append(",");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
