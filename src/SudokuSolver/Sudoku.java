/**
 * Builder and organization aide for a Sudoku puzzle.
 * @author drbob132
 * 
 */

package SudokuSolver;

public class Sudoku {
	public static final int SUDOKU_NUMBER_OF_SQUARES = 89;
	/*Blocks[] numbered as 
	 * |1|2|3|
	 * |4|5|6|
	 * |7|8|9|*/
	private SudokuBlock[] blocks;
	
	//columns[] 1 to 9, left to right
	private SudokuColumn[] columns;
	
	//rows[] 1 to 9, top to bottom
	private SudokuRow[] rows;
	
	private SudokuSquare[] squares;
	/*
	 * |010203|040506|070809|
	 * |101112|131415|161718|
	 * ...
	 */
	public Sudoku(int[] numbers) throws SudokuException{
		if(numbers.length < SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Not enough numbers given");
		}else if(numbers.length > SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Too many numbers given.");
		}
		
		squares = new SudokuSquare[SUDOKU_NUMBER_OF_SQUARES];
		
		for(int i=0; i < squares.length; i++){
			squares[i] = new SudokuSquare(numbers[i]);
		}
	}
		
	public String toString(){
		return "| | | |/n| | | |/n| | | |";
	}
	
}
