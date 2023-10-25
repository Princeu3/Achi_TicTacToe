package achi;

/**
 * Represents a cell in the Achi game board.
 */

public class GameCell {
	private char owner;
	private boolean movable; // Flag to track whether the stone is movable in the Move Phase.

	public GameCell() {
		owner = ' '; // Initialize as an empty cell.
		movable = false; // Initialize as not movable.
	}
	/**
	 * Gets the owner of the cell ('X' or 'O').
	 * @return The owner of the cell.
	 */

	public char getOwner() {
		return owner;
	}
	/**
	 * Sets the owner of the cell ('X' or 'O').
	 */

	public void setOwner(char owner) {
		this.owner = owner;
	}
	/**
	 * Checks if the stone in the cell is movable in the Move Phase.
	 *
	 * @return True if the stone is movable, false otherwise.
	 */
	public boolean isMovable() {
		return movable;
	}
	/**
	 * Marks the stone in the cell as movable in the Move Phase.
	 */
	public void markAsMovable() {
		movable = true;
	}
	/**
	 * Unmarks the stone in the cell as movable in the Move Phase.
	 */

	public void unmarkAsMovable() {
		movable = false;
	}

	@Override

	public String toString() {
		return String.valueOf(owner);
		
	}
	
}