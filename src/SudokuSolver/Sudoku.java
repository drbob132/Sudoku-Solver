/**
 * Builder and organization aide for a Sudoku puzzle.
 * Also solves them, which is what this is built for!
 * @author drbob132
 * @version 0.1
 * @date 1/21/2017
 */

package SudokuSolver;


public class Sudoku {
	public static final int SUDOKU_BLOCK_LENGTH = 3;
	public static final int SUDOKU_SIDE_LENGTH = SUDOKU_BLOCK_LENGTH * SUDOKU_BLOCK_LENGTH;
	public static final int SUDOKU_NUMBER_OF_SQUARES = SUDOKU_SIDE_LENGTH * SUDOKU_SIDE_LENGTH;

	//The squares that populate blocks/rows/collumns
	private SudokuSquare[] squares;

	//columns[] 1 to 9, left to right
	private SudokuColumn[] columns;
	
	//rows[] 1 to 9, top to bottom
	private SudokuRow[] rows;
	
	/*Blocks[] numbered as 
	 * |1|2|3|
	 * |4|5|6|
	 * |7|8|9|*/
	private SudokuBlock[] blocks;
	
	/**
	 * Populates a Sudoku.
	 * @param numbers Takes an int[] of 89 digits. All must be from 1 to 9, or 0 for empty squares.
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
		
		
		populateSquares(numbers);
		populateRows();
		populateColumns();
		populateBlocks();
		
//		SudokuSquare[] squareTemp = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		
		//initialize blocks
		
		
		//initialize columns
		
		
		//initialize rows
		
		
	}
		
	private void populateSquares(int[] numbers){
		
		squares = new SudokuSquare[SUDOKU_NUMBER_OF_SQUARES];
		
		for(int i=0; i < squares.length; i++){
			squares[i] = new SudokuSquare(numbers[i]);
		}
	}

	private void populateRows(){
		
	}
	
	private void populateColumns(){
		
	}

	private void populateBlocks(){
//		for(int i=0; i < SUDOKU_SIDE_LENGTH; i++){
//			for(int j=0; j < SUDOKU_SIDE_LENGTH; j++){
//				squareTemp[j] = squares[i*SUDOKU_BLOCK_LENGTH];
//			}
//			blocks[i] = new SudokuBlock(squareTemp);
//		}
	}
	
	
	/**
	 * Prints out the whole Sudoku as a string. 
	 */
	public String toString(){
		return "| | | |/n| | | |/n| | | |";
	}
	
}
