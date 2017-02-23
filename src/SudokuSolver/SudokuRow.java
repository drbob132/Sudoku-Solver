/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * @author drbob132
 * @version 0.1
 * @date 2/23/2017
 */

package SudokuSolver;

public class SudokuRow {
	
	private SudokuSquare[] squares = new SudokuSquare[Sudoku.SUDOKU_SIDE_LENGTH];

	public SudokuRow(SudokuSquare[] squares) {
		super();
		this.squares = squares;
	}
	
	public boolean contains(int value){
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
			if(squares[i].getValue() == value){
				return true;
			}
		}
		return false;
	}
	
}
