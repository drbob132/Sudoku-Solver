/**
 * Builder and organization aide for a Sudoku puzzle.
 * @author drbob132
 * 
 */

package SudokuSolver;

public class Sudoku {
	public static final int SUDOKU_BLOCK_LENGTH = 3;
	public static final int SUDOKU_SIDE_LENGTH = SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH;
	public static final int SUDOKU_NUMBER_OF_SQUARES = SUDOKU_SIDE_LENGTH*SUDOKU_SIDE_LENGTH;
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
	 * |000102|030405|060708|
	 * |091011|121314|151617|
	 * |181920|212223|242526|
	 * ----------------------
	 * |272829|303132|333435|
	 * ...
	 */
	public Sudoku(int[] numbers) throws SudokuException{
		if(numbers.length < SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Not enough numbers given");
		}else if(numbers.length > SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Too many numbers given.");
		}
		
		squares = new SudokuSquare[SUDOKU_NUMBER_OF_SQUARES];
		
		//initialize squares with input
		for(int i=0; i < squares.length; i++){
			squares[i] = new SudokuSquare(numbers[i]);
		}
		
		SudokuSquare[] squareTemp = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		
		//initialize blocks
		for(int i=0; i < SUDOKU_SIDE_LENGTH; i++){
			for(int j=0; j < SUDOKU_SIDE_LENGTH; j++){
				//i*3 
				//squareTemp[j] = squares[i*SUDOKU_BLOCK_LENGTH];
			}
			blocks[i] = new SudokuBlock({squares[i*9])
		}
		
		//initialize columns
		
		
		//initialize rows
		
		
	}
		
	public String toString(){
		return "| | | |/n| | | |/n| | | |";
	}
	
}
