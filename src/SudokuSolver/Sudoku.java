/**
 * Builder and organization aide for a Sudoku puzzle.
 * @author drbob132
 * 
 */

package SudokuSolver;

public class Sudoku {
	/*Blocks[] numbered as 
	 * |1|2|3|
	 * |4|5|6|
	 * |7|8|9|*/
	private SudokuBlock[] blocks;
	
	//columns[] 1 to 9, left to right
	private SudokuColumn[] columns;
	
	//rows[] 1 to 9, top to bottom
	private SudokuRow[] rows;
	
		
	public String toString(){
		return "| | | |/n| | | |/n| | | |";
	}
	
}
