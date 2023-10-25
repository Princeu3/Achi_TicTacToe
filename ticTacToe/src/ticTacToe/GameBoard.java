// CS204
//Author - Prince Upadhyay
//Achi Game based on TicTacToe


package ticTacToe;

public class GameBoard {
	
	private GameCell[][] gameCells; // The nine positions on the board
	private int rowOfEmptyCell; // What row is the (only) empty cell on ?
	private int colOfEmptyCell; // What column is the (only) empty cell on ?

    /** Constructor */
	public GameBoard() {
		gameCells = new GameCell[3][3];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				gameCells[row][col] = new GameCell();
			}
		}
        // Initialize the empty cell position in the center
        rowOfEmptyCell = 1;
        colOfEmptyCell = 1;
	}
	
	/** If we're in the drop phase, is this a legal move? */
	public boolean isDropPhaseMoveLegal(int row, int col) {
	       if (row >= 0 && row < 3 && col >= 0 && col < 3) {
	            return gameCells[row][col].getWhoOwnsMe() == 0;
	        }
	        return false;
	}
	
	/** During the "Drop Phase," drop a piece at the specified location. */
	public void dropPhasePutPieceAtLocation(int row, int col) {
        if (isDropPhaseMoveLegal(row, col)) {
            int player = TicTacToe.getWhoseTurnIsIt();
            gameCells[row][col].setWhoOwnsMe(player);
            findEmptyCell();
        }
    }
	
	/** During the Move Phase, exactly one cell should be empty. Find that. */
	public int[] findEmptyCell() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameCells[row][col].getWhoOwnsMe() == 0) {
                    rowOfEmptyCell = row;
                    colOfEmptyCell = col;
                    int[] emptyCell = { rowOfEmptyCell, colOfEmptyCell };
                    return emptyCell;
                }
            }
        }
        return null; // Return null if no empty cell is found
    }

	/** If we're in the move phase, is this a legal move? */
	public boolean isMoveLegalInTheMovePhase(int row, int col) {
		
		int currentPlayer = TicTacToe.getWhoseTurnIsIt();
			return gameCells[row][col].getWhoOwnsMe() == currentPlayer && 
			isLegalDiagonalMove(row, col, rowOfEmptyCell, colOfEmptyCell) || isLegalRowOrColMove(row, col, rowOfEmptyCell, colOfEmptyCell);
	}
	
	/** This is a private function, so we don't need to document it for coders outside of this class. */
	private boolean isLegalRowOrColMove(int row, int col, int rowOfEmptyCell, int colOfEmptyCell) {
	    // A legal move should have a distance of 1 between the empty cell and the selected cell.
	    return (Math.abs(row - rowOfEmptyCell) + Math.abs(col - colOfEmptyCell) == 1);
	}

	/** A diagonal move is legal if either the piece being moved is in the center */
	private boolean isLegalDiagonalMove(int row, int col, int rowOfEmptyCell, int colOfEmptyCell) {
	    // A diagonal move is legal if either the selected cell or the empty cell is at the center (1, 1).
	    return (row == 1 && col == 1) || (rowOfEmptyCell == 1 && colOfEmptyCell == 1);
	}

	/** If the requested move is legal, make the move. */
	/** Move a piece from one location to another (assuming the move is valid). */
	public void movePieceFromLocation(int row, int col) {
	    int[] emptyCell = findEmptyCell();
	 {
	        // Swap the contents of the selected cell and the empty cell
	        GameCell tempCell = gameCells[row][col];
	        gameCells[row][col] = gameCells[emptyCell[0]][emptyCell[1]];
	        gameCells[emptyCell[0]][emptyCell[1]] = tempCell;

	        // Update the coordinates of the empty cell
	        rowOfEmptyCell = row;
	        colOfEmptyCell = col;
	    }
	    // No need to return a boolean value since the assumption is that the move is valid.
	}


	/** Convert this data structure into a readable string. May be used for debugging */
	public String toString() {
		StringBuilder stringSoFar = new StringBuilder();
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				stringSoFar.append(gameCells[row][col]);
				if (col < 2) {
					stringSoFar.append(",");
				}
			}
			stringSoFar.append("\n");
		}
		return stringSoFar.toString();
	}
}