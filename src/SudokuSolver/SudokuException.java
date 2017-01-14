package SudokuSolver;

public class SudokuException extends Exception{
	/*
	 * I don't really know what serialVersionUID does, and have never needed it.
	 * #CCCYDWTPS: Classic coder comments you don't want the public seeing
	 */
	private static final long serialVersionUID = 1L;
	
	public SudokuException(String message){
		super(message);
	}

}
