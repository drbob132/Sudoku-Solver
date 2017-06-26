/**
 * Builder and organization aide for a Sudoku puzzle.
 * Also solves them, which is what this is built for!
 * @author drbob132
 * @version 0.1
 * @date 6/26/2017
 */

package SudokuSolver;

import java.util.Arrays;

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
	 * @param numbers Takes an int[] of 89 (9^2) digits. All must be from 1 to 9, or 0 for empty squares.
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
		SudokuSquare[] tempArray;
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			tempArray = Arrays.copyOfRange(squares, i*SUDOKU_SIDE_LENGTH, (i+1)*SUDOKU_SIDE_LENGTH);
			rows[i] = new SudokuRow(tempArray);
		}
	}
	
	private void populateColumns(){
		SudokuSquare[] tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		int index;
		
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			for(int j=0; j<SUDOKU_SIDE_LENGTH; j++){
				index = i + j*SUDOKU_SIDE_LENGTH;
				tempArray[j] = squares[index];
			}
			columns[i] = new SudokuColumn(Arrays.copyOf(tempArray, tempArray.length));
		}
	}

	/*
	 * |0 1 2|
	 * |3 4 5|
	 * |6 7 8|
	 */
	private void populateBlocks(){
		SudokuSquare[] tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		int index;
		
		for(int block=0; block < SUDOKU_SIDE_LENGTH; block++){
			for(int pos=0; pos < SUDOKU_SIDE_LENGTH; pos++){
				index = blockSquareIndex(block, pos);
				tempArray[pos] = squares[index];
			}
			blocks[block] = new SudokuBlock(tempArray);
		}
	}
	
	private int blockSquareIndex(int block, int position){
		int index;
		int blockOffset;
		int rowOffset;
		//get the index of the starting index of a block
		//Magic -- 9x9 -> {0, 3, 6, 27, 30, 33, 54, 57, 60}
		//{+0,+3,+6} & {+0,+27,+54}
		blockOffset = block%SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH + block/SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH*SUDOKU_SIDE_LENGTH;
		
		//get the index of the row
		//{+0,+9,+18}
		rowOffset = position/SUDOKU_BLOCK_LENGTH*SUDOKU_SIDE_LENGTH;
		
		//get position in block plus offsets
		index = position%SUDOKU_BLOCK_LENGTH + rowOffset + blockOffset;
		return index;
	}
	
	/**
	 * Prints out the whole Sudoku as a string. 
	 */
	public String toString(){
		String sudokuString = "";
		
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			sudokuString += rows[i].toString();
			sudokuString += '\n';
		}
		
		return sudokuString; //"| | | |\n| | | |\n| | | |";
	}
	
}
