package ticTacToe;

public class GameCell {

	private int whoOwnsMe; // 1=X, -1=O, 0 means no one.

	public GameCell( ) {
		whoOwnsMe = 0;
	}
	
	// Setters and Getters:
	
	public int getWhoOwnsMe( ) {
		return whoOwnsMe;
	}

	public void setWhoOwnsMe(int whoOwnsMe) {
		this.whoOwnsMe = whoOwnsMe;
	}

	public String toString( ) {
		if ( whoOwnsMe == 1 )
			return "X";
		else if ( whoOwnsMe == -1 )
			return "O";
		else
			return "—";
	}
	
	
}


