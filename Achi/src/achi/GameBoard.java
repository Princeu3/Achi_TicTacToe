package achi;

/**
 * Represents the game board for Achi.
 */

public class GameBoard {
	private GameCell[][] gameCells;

	public GameBoard() {
		gameCells = new GameCell[3][3];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				gameCells[row][col] = new GameCell();
			}
		}
	}

	/**
	 * Gets the 2D array of game cells representing the board.
	 *
	 * @return The game cells.
	 */

	public GameCell[][] getGameCells() {
		return gameCells;

	}

	/**
	 * Places a stone on the game board.
	 *
	 *row    The row to place the stone.
	 *col    The column to place the stone.
	 *player The player ('X' or 'O') who owns the stone.
	 */
	
	public void placeStone(int row, int col, char player) {
		gameCells[row][col].setOwner(player);
	}

	/**
	 * Gets the owner of a cell on the game board.
	 * @return The owner of the cell ('X', 'O', or ' ').
	 */
	
	public char getOwner(int row, int col) {
		return gameCells[row][col].getOwner();
	}

	/**
	 * Checks if a cell on the game board is empty.
	 * @return True if the cell is empty, false otherwise.
	 */

	public boolean isEmpty(int row, int col) {
		return gameCells[row][col].getOwner() == ' ';
	}
	
	/**
	 * Checks if a move from one cell to another on the game board is valid.

	 * fromRow The source row of the stone.
	 * fromCol The source column of the stone.
	 * toRow   The destination row.
	 * toCol   The destination column.
	 * True if the move is valid, false otherwise.
	 */

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Check if the destination is within the board bounds and is empty
        if (toRow >= 0 && toRow < 3 && toCol >= 0 && toCol < 3 && gameCells[toRow][toCol].getOwner() == ' ') {
            return true; // Any empty cell is a valid destination for the first move.
        }
        return false;
    }


	/**

	 * Moves a stone from one cell to another on the game board.
	 *
	 * fromRow The source row of the stone.
	 * fromCol The source column of the stone.
	 * toRow   The destination row.
	 * toCol   The destination column.
	 */

    public void moveStone(int fromRow, int fromCol, int toRow, int toCol) {
        // Move the stone from the source cell to the destination cell
        char owner = gameCells[fromRow][fromCol].getOwner();
        gameCells[fromRow][fromCol].setOwner(' '); // Clear the source cell
        gameCells[toRow][toCol].setOwner(owner); // Set the destination cell to the player's stone
    }
	
}