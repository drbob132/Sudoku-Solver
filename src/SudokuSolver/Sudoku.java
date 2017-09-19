/**
 * Builder and organization aide for a Sudoku puzzle.
 * Also solves them, which is what this is built for!
 * @author drbob132
 * @version 0.2
 * @date 9/19/2017
 */

package SudokuSolver;

import java.util.Arrays;

public class Sudoku {
	public static final int SUDOKU_BLOCK_LENGTH = 3;
	public static final int SUDOKU_SIDE_LENGTH = SUDOKU_BLOCK_LENGTH * SUDOKU_BLOCK_LENGTH;
	public static final int SUDOKU_NUMBER_OF_SQUARES = SUDOKU_SIDE_LENGTH * SUDOKU_SIDE_LENGTH;

	//The squares that populate blocks/rows/collumns
	private SudokuSquare[] squares;

	//columns[] 0 to 8, left to right
	private SudokuColumn[] columns;
	
	//rows[] 0 to 8, top to bottom
	private SudokuRow[] rows;
	
	/*Blocks[] numbered as 
	 * |0|1|2|
	 * |3|4|5|
	 * |6|7|8|*/
	private SudokuBlock[] blocks;
	
	/**
	 * Populates a Sudoku.
	 * @param numbers Takes an int[] of 89 (9^2) digits. All must be from 1 to 9, or 0 for empty squares.
	 * |00 01 02|03 04 05|06 07 08|
	 * |09 10 11|12 13 14|15 16 17|
	 * |18 19 20|21 22 23|24 25 26|
	 * ----------------------
	 * |27 28 29|30 31 32|33 34 35|
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
		
	}
	
	
	private void populateSquares(int[] numbers){
		
		squares = new SudokuSquare[SUDOKU_NUMBER_OF_SQUARES];
		
		for(int i=0; i < squares.length; i++){
			squares[i] = new SudokuSquare(numbers[i]);
		}
	}

	private void populateRows() throws SudokuException{
		rows = new SudokuRow[SUDOKU_SIDE_LENGTH];
		
		SudokuSquare[] tempArray;
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			tempArray = Arrays.copyOfRange(squares, i*SUDOKU_SIDE_LENGTH, (i+1)*SUDOKU_SIDE_LENGTH);
			try{
				rows[i] = new SudokuRow(tempArray);
			}catch(SudokuException e){
				throw new SudokuException("Caught at populateRows\n" + e.getMessage());
			}
		}
	}
	
	private void populateColumns() throws SudokuException{
		columns = new SudokuColumn[SUDOKU_SIDE_LENGTH];
		
		SudokuSquare[] tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		int index;
		
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			for(int j=0; j<SUDOKU_SIDE_LENGTH; j++){
				index = i + j*SUDOKU_SIDE_LENGTH;
				tempArray[j] = squares[index];
			}
			try{
				columns[i] = new SudokuColumn(Arrays.copyOf(tempArray, tempArray.length));
			}catch(SudokuException e){
				throw new SudokuException("Caught at populateRows\n" + e.getMessage());
			}
		}
	}

	/*
	 * |0 1 2|
	 * |3 4 5|
	 * |6 7 8|
	 */
	private void populateBlocks() throws SudokuException{
		blocks = new SudokuBlock[SUDOKU_SIDE_LENGTH];
		
		SudokuSquare[] tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		int index;
		
		for(int block=0; block < SUDOKU_SIDE_LENGTH; block++){
			for(int pos=0; pos < SUDOKU_SIDE_LENGTH; pos++){
				index = blockSquareIndex(block, pos);
				tempArray[pos] = squares[index];
			}
			try{
				blocks[block] = new SudokuBlock(tempArray);
			}catch(SudokuException e){
				throw new SudokuException("Caught at populateBlocks\n" + e.getMessage());
			}
		}
	}
	
	/**
	 * Checks the square, and relevant row and column to see if the square can hold the given value.
	 * For checking block and conditions, check blockContains.
	 * @param block The block that contains the square that is being tested
	 * @param position The square in the block that is being tested
	 * @param value The value to check for legality in the square
	 * @return True if not immediately illegal
	 */
	public boolean squareAtPositionCanBe(int squarePosition, int value){
		
		boolean squareEmpty = isSquareEmpty(squarePosition);
		
		if(squareEmpty){
			boolean rowCheck = rowContains(squarePosition/SUDOKU_SIDE_LENGTH, value);
			boolean columnCheck = columnContains(squarePosition%SUDOKU_SIDE_LENGTH, value);
			
			return !(rowCheck || columnCheck);
		}else{
			return squareEmpty;
		}
	}
	
	/**
	 * Checks the block if the given value has been found absolutely, or if found conditionally. (See SudokuSquareXOR for details on conditions)
	 * @param block The block to be tested.
	 * @param value The value to check.
	 * @return True if found absolutely, or if found conditionally. (See SudokuSquareXOR)
	 */
	public boolean blockContains(int block, int value){
		return blocks[block].hasDiscovered(value);
	}
	
	public boolean isSquareEmpty(int position){
		return squares[position].isEmpty();
	}
	
	public void setSquare(int position, int value) throws SudokuException{
		try{
			squares[position].set(value);
		}catch(SudokuException e){
			throw e;
		}
	}
	
	/**
	 * Checks the column if the given value has been found.
	 * @param column The column to be tested.
	 * @param value The value to check.
	 * @return True if found absolutely, or if found conditionally. (See SudokuSquareXOR)
	 */
	public boolean columnContains(int column, int value){
		return columns[column].contains(value);
	}
	
	/**
	 * Checks the row if the given value has been found.
	 * @param row The row to be tested.
	 * @param value The value to check.
	 * @return True if found absolutely, or if found conditionally. (See SudokuSquareXOR)
	 */
	public boolean rowContains(int row, int value){
		return rows[row].contains(value);
	}
	
	public static int blockSquareIndex(int block, int position){
		int index;
		int blockOffset;
		int rowOffset;
		//get the index of the starting position of a block
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
