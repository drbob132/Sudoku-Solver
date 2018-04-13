/**
 * Organization for printing, storing and also a platform to rules in solving Sudoku puzzles.
 * @author drbob132
 * @version 1.1
 * @date 04/12/2018
 */

package SudokuSolver;

import java.util.Arrays;

public class Sudoku {
	
	private static final boolean DEBUG = false;
	
	/*public static final int SUDOKU_BLOCK_LENGTH = 3;
	public static final int SUDOKU_SIDE_LENGTH = SUDOKU_BLOCK_LENGTH * SUDOKU_BLOCK_LENGTH;
	public static final int SUDOKU_NUMBER_OF_SQUARES = SUDOKU_SIDE_LENGTH * SUDOKU_SIDE_LENGTH;*/
	
	public final int SUDOKU_BLOCK_LENGTH; //This one doesn't really get used, but whatever. It's there.
	public final int SUDOKU_SIDE_LENGTH;
	public final int SUDOKU_NUMBER_OF_SQUARES;

	//The squares that populate blocks/rows/columns
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
	 * Populates a Sudoku of sudokuSideLength by sudokuSideLength.
	 * @param numbers Takes an array of int of length sudokuSideLength^2. (ie: int[89] (9^2) digits. All must be from 1 to 9, or 0 for empty squares.)
	 * |00 01 02|03 04 05|06 07 08|
	 * |09 10 11|12 13 14|15 16 17|
	 * |18 19 20|21 22 23|24 25 26|
	 * ----------------------
	 * |27 28 29|30 31 32|33 34 35|
	 * ...
	 */
	public Sudoku(int[] sudokuValues, int sudokuSideLength) throws SudokuException{
		SUDOKU_NUMBER_OF_SQUARES = sudokuSideLength*sudokuSideLength;
		SUDOKU_SIDE_LENGTH = sudokuSideLength;
		SUDOKU_BLOCK_LENGTH = (int)Math.sqrt(SUDOKU_SIDE_LENGTH); //probably not used, but hey, it's there for validation here now, at least!
		
		if(SUDOKU_NUMBER_OF_SQUARES != SUDOKU_SIDE_LENGTH*SUDOKU_SIDE_LENGTH){
			throw new SudokuException("Number of values given (" + sudokuValues.length + ") not equal to the number of values required. ("
					+ SUDOKU_NUMBER_OF_SQUARES + ")");
		}else if(SUDOKU_SIDE_LENGTH != SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH) {
			throw new SudokuException("Sudoku is not square, and does not apply to this implementation. (side length = " + SUDOKU_SIDE_LENGTH + ")");
		}
		
		populate(sudokuValues);
		
	}
	
	/**
	 * Copies a Sudoku without any condition that might be stored.
	 * @param otherSudoku Sudoku to copy.
	 */
	public Sudoku(Sudoku otherSudoku) throws SudokuException{
		
		SUDOKU_NUMBER_OF_SQUARES = otherSudoku.SUDOKU_NUMBER_OF_SQUARES;
		SUDOKU_SIDE_LENGTH = otherSudoku.SUDOKU_SIDE_LENGTH;
		SUDOKU_BLOCK_LENGTH = otherSudoku.SUDOKU_BLOCK_LENGTH;
		
		if(SUDOKU_NUMBER_OF_SQUARES != SUDOKU_SIDE_LENGTH*SUDOKU_SIDE_LENGTH || SUDOKU_SIDE_LENGTH != SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH) {
			throw new SudokuException("Sudoku being copied is not square, and does not apply to this implementation. (side length = " + SUDOKU_SIDE_LENGTH + ")");
		}
		
		int[] sudokuValues = new int[SUDOKU_NUMBER_OF_SQUARES];
		for(int i=0; i<SUDOKU_NUMBER_OF_SQUARES; i++){
			sudokuValues[i] = otherSudoku.valueAt(i);
		}
		
		populate(sudokuValues);
	}
	
	private void populate(int[] sudokuValues){
		populateSquares(sudokuValues);
		populateRows();
		populateColumns();
		populateBlocks();
	}
	
	private int valueAt(int position){
		return squares[position].getValue();
	}
	
	private void populateSquares(int[] numbers){
		
		squares = new SudokuSquare[SUDOKU_NUMBER_OF_SQUARES];
		
		for(int i=0; i < squares.length; i++){
			squares[i] = new SudokuSquare(numbers[i]);
		}
	}

	private void populateRows(){
		rows = new SudokuRow[SUDOKU_SIDE_LENGTH];
		
		SudokuSquare[] tempArray;
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			tempArray = Arrays.copyOfRange(squares, i*SUDOKU_SIDE_LENGTH, (i+1)*SUDOKU_SIDE_LENGTH);
			rows[i] = new SudokuRow(tempArray);
		}
		if(DEBUG) {
			for(int i=0; i < SUDOKU_SIDE_LENGTH; i++){
				System.out.println("[" + getClass() + ".populateRows(); Printing Row #" + i + "]");
				System.out.println(rows[i].print());
			}
		}
	}
	
	private void populateColumns(){
		columns = new SudokuColumn[SUDOKU_SIDE_LENGTH];
		
		SudokuSquare[] tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
		int index;
		
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			for(int j=0; j<SUDOKU_SIDE_LENGTH; j++){
				index = i + j*SUDOKU_SIDE_LENGTH;
				tempArray[j] = squares[index];
			}
			columns[i] = new SudokuColumn(Arrays.copyOf(tempArray, tempArray.length));
		}
		if(DEBUG) {
			for(int i=0; i < SUDOKU_SIDE_LENGTH; i++){
				System.out.println("[" + getClass() + ".populateColumns(); Printing Column #" + i + "]");
				System.out.println(columns[i].print());
			}
		}
	}

	/*
	 * |0 1 2|
	 * |3 4 5|
	 * |6 7 8|
	 */
	private void populateBlocks(){
		blocks = new SudokuBlock[SUDOKU_SIDE_LENGTH];
		if(DEBUG) {
			System.out.println("[" + getClass() + ".populateBlocks(); blocks.length: " + blocks.length);
		}
		
		int index;
		SudokuSquare[] tempArray;
		
		for(int block=0; block < SUDOKU_SIDE_LENGTH; block++){
			tempArray = new SudokuSquare[SUDOKU_SIDE_LENGTH];
			for(int pos=0; pos < SUDOKU_SIDE_LENGTH; pos++){
				index = blockSquareIndex(block, pos);
				tempArray[pos] = squares[index];
			}
			if(DEBUG) {
				System.out.print(block);
			}
			blocks[block] = new SudokuBlock(tempArray);
		}
		if(DEBUG) {
			for(int i=0; i < SUDOKU_SIDE_LENGTH; i++){
				System.out.println("[" + getClass() + ".populateBlocks(); Printing Block #" + i + "]");
				System.out.println(blocks[i].print());
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
			if(DEBUG) {
				System.out.println("[pos"+squarePosition+" row"+squarePosition/SUDOKU_SIDE_LENGTH+" col"+squarePosition%SUDOKU_SIDE_LENGTH+"]");
			}
			return !(rowCheck || columnCheck);
		}else{
			return squareEmpty;
		}
	}
	
	/**
	 * Checks the block if the given value has been found absolutely. (A square contains that value)
	 * @param block The block to be tested.
	 * @param value The value to check.
	 * @return True if found in a square
	 */
	public boolean blockContainsAbsolute(int block, int value){
		SudokuBlock targetBlock = blocks[block];
		if(DEBUG) {
			System.out.println("[" + getClass() + ".blockContainsAbsolute(); checking block #" + block + " for value " + value + "]");
			System.out.println(targetBlock.print());
		}
		return targetBlock.contains(value);
	}
	
	/**
	 * Checks the block if the given value has been found absolutely OR conditionally. (See SudokuSquareXOR for details on conditions)
	 * @param block The block to be tested.
	 * @param value The value to check.
	 * @return True if found absolutely OR if found conditionally. (See SudokuSquareXOR)
	 */
	public boolean blockContainsConditional(int block, int value){
		SudokuBlock targetBlock = blocks[block];
		if(DEBUG) {
			System.out.println("[" + getClass() + ".blockContainsConditional(); checking block #" + block + " for value " + value + "]");
			System.out.println(targetBlock.print());
		}
		return targetBlock.hasDiscovered(value);
	}
	
	/**
	 * True if square at position is empty.
	 * @param position must be within the boundaries of the Sudoku (ie 0-80 in 9x9 sudoku)
	 * @return
	 */
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
	
	public void addXOR(int block, int value, int position1, int position2) {
		SudokuBlock targetBlock = blocks[block];
		targetBlock.addXOR(value, position1, position2);
	}
	
	/**
	 * Returns the count of conditions found in each block.
	 * @return The count of conditions found in each block.
	 */
	public int getXORConditionCount() {
		int count = 0;
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++) {
			count += blocks[i].getXORConditionCount();
		}
		return count;
	}
	
	/**
	 * Checks any conditions in the block for satisfaction.
	 * @param block the block number to check (from 0 to Sudoku.SUDOKU_SIDE_LENGTH)
	 */
	public void checkBlockConditions(int block) throws SudokuException{
		SudokuBlock targetBlock = blocks[block];
		targetBlock.checkConditions();
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
	
	/**
	 * Adjusts input for file/arg input
	 * @param block
	 * @param position
	 * @return
	 */
	private int blockSquareIndexInverted(int block, int position) {
		return blockSquareIndex(SUDOKU_SIDE_LENGTH - block - 1, position);
	}
	
	public int blockSquareIndex(int block, int position){
		int index;
		int blockOffset;
		int rowOffset;
		//get the index of the starting position of a block
		//Magic -- 9x9 -> {0, 3, 6, 27, 30, 33, 54, 57, 60}
		//{+0,+3,+6} & {+0,+27,+54}
		//Note: This is inverted by how it's read from file
		blockOffset = block%SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH + block/SUDOKU_BLOCK_LENGTH*SUDOKU_BLOCK_LENGTH*SUDOKU_SIDE_LENGTH;
		
		//get the index of the row
		//{+0,+9,+18}
		rowOffset = position/SUDOKU_BLOCK_LENGTH*SUDOKU_SIDE_LENGTH;
		
		//get position in block plus offsets
		index = position%SUDOKU_BLOCK_LENGTH + rowOffset + blockOffset;
		return index;
	}
	
	/**
	 * Prints out the whole Sudoku as a string using the SudokuIODecoder as a reference. 
	 * @param decoderForIO class for managing the formatting and translating the integers to each their own character.
	 */
	public String print(SudokuIODecoder decoderForIO){
		String sudokuString = "";
		
		for(int i=0; i<SUDOKU_SIDE_LENGTH; i++){
			sudokuString += rows[i].print(decoderForIO);
			sudokuString += '\n';
		}
		
		return sudokuString; //"| | | |\n| | | |\n| | | |";
	}
	
}
