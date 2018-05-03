/**
 * Solves a given square sudoku, using basic rules and XOR conditions.
 * Programmed for expansion -> Sudoku N (N^2xN^2), where N is the block width (Sudoku 3 is the vanilla 9x9 sudoku)
 * Designed to operate in steps, so progression can be observed
 * 
 * @author drbob132
 * @version 1.2
 * @date 04/23/2018
 */

package SudokuSolver;

import java.util.ArrayList;

public class SudokuSolver {

	private static final boolean DEBUG = false;
	
	private Sudoku sudokuAttempt;
	private SudokuIODecoder decoderForIO;
	
	
	//used for detecting when progress has halted.
	private int mostRecentvalueFound; //Identifies the most recent number found
	private int mostRecentBlock; //Identifies the block that the most recent value was found in
	private boolean mostRecentFirstPass;
	private boolean progressHalted;
	private int iterations;
	private int maxIterations = 10000;
	private int blockSearchCount;
	
	//tracking current state (in object context, as this will have step functionality
	private ArrayList<ArrayList> valuesToFind;
	private int currentValue;
	private int currentBlock;
	private int targetBlock; //Identifies the last block to search; the block that the most recent value was found in
	private boolean valueFoundInBlock; //If a value was found in current check - Used to reiterate block search when finding values and avoid early termination
	
	public SudokuSolver(){
		
	}
	
	public SudokuSolver(String puzzle, int sudokuSideLength, SudokuIODecoder decoder) throws SudokuException{
		try {
			enterSudoku(puzzle, sudokuSideLength, decoder);
		} catch(SudokuException e){
			throw e;
		}
	}
	
	/**
	 * Populates a Sudoku object using
	 * @param puzzle
	 * @throws SudokuException
	 */
	public void enterSudoku(String puzzle, int sudokuSideLength, SudokuIODecoder decoder) throws SudokuException{
		decoderForIO = decoder;
		int numberOfSquares = sudokuSideLength*sudokuSideLength;
		mostRecentvalueFound = 1;
		mostRecentBlock = 0;
		valueFoundInBlock = true;
		progressHalted = false;
		currentValue = 1;
		currentBlock = 0;
		targetBlock = sudokuSideLength - 1;
		iterations = 0;
		blockSearchCount = 0;
		
		int[] values = new int[numberOfSquares];
		int tempValue;
		int index = 0;
		char tempChar;
		
		if(puzzle.length() < numberOfSquares){
			throw new SudokuException("" + puzzle.length() + " is not enough characters to populate a " + sudokuSideLength + "x" + sudokuSideLength 
					+ " puzzle. (" + numberOfSquares + "required.)");
		}
		//not testing for too many, as Sudoku formats are all over when looking online...
		
		for(int i=0; index<numberOfSquares && i<puzzle.length(); i++){
			//get number
			tempChar = puzzle.charAt(i);
			
			//scrubbing
			if(decoder.charIsValid(tempChar)){
				tempValue = decoder.charToInt(tempChar);
				
				values[index++] = tempValue;
			}
		}
		
		if(index < numberOfSquares){
			throw new SudokuException("Not enough numbers/spaces in string to enter puzzle (index: " + index + ", argChars: " + puzzle.length() + ")");
		}
		
		sudokuAttempt = new Sudoku(values, sudokuSideLength);
	}
	
	/**
	 * replaces the SudokuIODecoder used by the Solver. This will only affect printing.
	 * @precondition This assumes that the decoder is valid (has enough characters defined)
	 * @param decoder a defined SudokeIODecoder that applies for the values found in the Sudoku.
	 */
	public void changeDecoder(SudokuIODecoder decoder){
		decoderForIO = decoder;
	}
	
	public String print(){
		return sudokuAttempt.print(decoderForIO);
	}
	
	/**
	 * @return The details of any remaining stored conditions that assert a number's possible positions.
	 */
	public String getXORConditions(){
		return "";
	}
	
	/**
	 * @return The count of any remaining stored conditions that assert a number's possible positions.
	 */
	public int getXORConditionCount(){
		int count = sudokuAttempt.getXORConditionCount();
		return count;
	}
	
	/**
	 * Attempts to solve the stored Sudoku fully without guessing.
	 * Assumes puzzle is valid until it reaches an invalid condition.
	 * If puzzle remains incomplete, it is either invalid or unsolvable (according to this algorithm)
	 * Progress and discovered conditions may be retrieved.
	 */
	public void solveFull() throws SudokuException{
		//Should be programmed to use solveStep()
		//That'd require a saved-state in this object if doing it by step should perform the same as doing it fully
		
		/*
		 * *start at Number 1
		 * Do until progress has halted
		 * *	   CurrentBlock = 0
		 * *    Do until Number can't be discovered in remaining blocks
		 *         ((Step code start))
		 * *        Increment CurrentBlock (loop back to 1 if out of bounds, skip blocks where number is found or holds an XOR for that number)
		 *         ((Block code start))
		 *         Find squares in block that can contain Number (rows and columns internally track this)
		 *             Check rows & columns
		 *             *W* If square is hosting a pair of overlapping XOR conditions, assert that Number is one asserted by one of the two conditions
		 *             *W* Accounts for alignment ("phantom numbers") from other blocks
		 *         If only 1 position
		 *             Assign Number to that square
		 *             Update mostRecentNumberFound 
		 *             (XOR conditions automatically trigger on assignment, and so does completing a row/column/block)
		 *         else
		 *             if 2 positions
		 *                 Create XOR condition for those squares, and block (This is effectively found)
		 *                 Update mostRecentNumberFound
		 *             else if 0 positions
		 *                 Puzzle is invalid!
		 *             *W* If the positions align to a column/row
		 *                 *W* Assert that Number is in the current block to the respective column/row
		 *                 *W* Update mostRecentNumberFound
		 *         ((Block code stop))
		 *  *       If no block can be picked, the number is fully discovered. (XORs may still exist, and will have to be resolved.)
		 *         ((Step code stop))
		 *  *   Increment number (loop back to 1 if out of bounds, and skip fully discovered numbers)
		 *  *   If no number can be picked, the puzzle is solved. (XORs may still exist, and will have to be resolved.)
		 *  *   If Number loops past the most recent number discovered (number found in a square or XOR *or Alignment*)
		 *  *       Progress has halted.   
		 */
		
//		progressHalted = false;
//		int currentBlock; //The current block of the value search
//		int targetBlock; //The block to stop searching for a value
//		int currentValue = 1; //the current value being serched
//		int iterations = 0; //telemetry
//		boolean valueFoundInBlock = false; //Value found in current block
		boolean firstPass;
		boolean continueSearchingWithThisValue = false;
		try{
			do{ //until progress halted
				currentBlock = 0;
				targetBlock = sudokuAttempt.SUDOKU_SIDE_LENGTH - 1;
				firstPass = true;
				do{ //until this value can't be found currently
					currentBlock = currentBlock % sudokuAttempt.SUDOKU_SIDE_LENGTH;
					valueFoundInBlock = false;
					if(!(sudokuAttempt.blockContainsConditional(currentBlock, currentValue))){
						if(DEBUG) {
							System.out.println("Looking for " + currentValue + " in block #" + currentBlock);
						}
						valueFoundInBlock = findValueInBlock(currentValue, currentBlock);
						if(valueFoundInBlock){
							targetBlock = currentBlock;
							mostRecentvalueFound = currentValue;
							mostRecentBlock = currentBlock;
							mostRecentFirstPass = true;
							if(DEBUG) {
								System.out.println("Found");
							}
						}
						blockSearchCount++;
					}else if(DEBUG){
						System.out.println("Value " + currentValue + " in block #" + currentBlock + " already discovered.");
						if(currentBlock == targetBlock) {
							System.out.println("currentBlock == targetBlock");
							if(firstPass) {
								System.out.println("firstPass");
							}
						}
					}
					
					sudokuAttempt.checkBlockConditions(currentBlock);
					
					iterations++;
					
					continueSearchingWithThisValue = (!(currentBlock == targetBlock && !valueFoundInBlock) && iterations < maxIterations) || firstPass;

					firstPass = false;
					currentBlock++;
				}while(continueSearchingWithThisValue);
				if(DEBUG) {
					System.out.println(">>>escaping do-while");
				}
				
				currentValue = currentValue % sudokuAttempt.SUDOKU_SIDE_LENGTH + 1; // max number would become 1
				
				//termination check
				if(!mostRecentFirstPass && mostRecentvalueFound == currentValue){
					progressHalted = true;
				}else{
					mostRecentFirstPass = false;
				}
			}while(!progressHalted && iterations < maxIterations);
			
			if(progressHalted) {
				System.out.println("Progress was halted because progress appeared to have halted. "
						+ "(looped through Sudoku without progress)");
			}else if(iterations >= maxIterations) {
				System.out.println("Progress was halted because the max number of iterations was reached. "
						+ "(actual iterations: " + iterations + ", max iterations: " + maxIterations + ")");
			}
			if(DEBUG) {
				System.out.println("\nDumping SudokuSolver variables:");
				System.out.println("currentValue = " + currentValue);
				System.out.println("currentBlock = " + currentBlock);
				System.out.println("targetBlock = " + targetBlock);
				System.out.println("progressHalted = " + progressHalted);
				System.out.println("firstPass = " + firstPass);
				System.out.println("mostRecentvalueFound = " + mostRecentvalueFound);
				System.out.println("mostRecentBlock = " + mostRecentBlock);
				System.out.println("mostRecentFirstPass = " + mostRecentFirstPass);
				System.out.println("valueFoundInBlock = " + valueFoundInBlock);
				
				System.out.println("");
			}
			
			
		}catch(SudokuException e){
			progressHalted = true;
			throw e;
		}
	}
	
	public Sudoku getSudokuAttempt(){
		Sudoku puzzle = null;
		try {
			puzzle =  new Sudoku(sudokuAttempt);
		} catch (SudokuException e) {
			if(DEBUG) {
				e.printStackTrace();
			}
		}
		return puzzle;
	}

	public int getIterations() {
		return iterations;
	}
	
	public int getBlockSearchCount() {
		return blockSearchCount;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public int getCurrentBlock() {
		return currentBlock;
	}

	/**
	 * **unimplemented**
	 */
	public void solveStep(){
		return -1;
	}
	
	private boolean findValueInBlock(int value, int block) throws SudokuException{

		ArrayList<Integer> possiblePositions = new ArrayList<Integer>();
		
		boolean found = false;
		if(!(sudokuAttempt.blockContainsConditional(block, value))){
	        //find squares in block that can contain Number (rows and columns internally track this)
			for(int i=0; i<sudokuAttempt.SUDOKU_SIDE_LENGTH; i++){
				if(sudokuAttempt.squareAtPositionCanBe(sudokuAttempt.blockSquareIndex(block, i), value)){
					possiblePositions.add(i);
				}
			}
	        //If only 1 position
			if(possiblePositions.size() == 1){
		        //    Assign Number to that square
		        //    (XOR conditions automatically trigger on assignment, and so does completing a row/column/block)
				try{
					sudokuAttempt.setSquare(sudokuAttempt.blockSquareIndex(block, possiblePositions.get(0)), value);
				}catch(SudokuException e){
					throw e;
				}
		        // *   Update mostRecentNumberFound 
				//***  Update this in controller function
				found = true;
		    //else
			}else{
		        //    if 2 positions
				if(possiblePositions.size() == 2){
					found = true;
					// *       Create XOR condition for those squares, and block (This is effectively found)
					sudokuAttempt.addXOR(block, value, possiblePositions.get(0), possiblePositions.get(1));
					// *       Update mostRecentNumberFound
				//    else if 0 positions
				}else if(possiblePositions.size() == 0){
					//        Puzzle is invalid!
					progressHalted = true;
					throw new SudokuException("Puzzle Invalid. Value (" + value + ") is impossible to find in block #" + block + ".");
				}
			}
		}else{
			found = true;
		}
		
		if(DEBUG) {
			System.out.print("[" + getClass() + ".findValueInBlock(); ");
			if(found) {
				System.out.print("Value must be at: ");
			}else {
				System.out.print("Positions possible: ");
				
			}
			if(possiblePositions.size() > 0) {
				for(Integer number : possiblePositions) {
					System.out.print(number + " ");
				}
			}else {
				System.out.print("none");
			}
			System.out.println("]");
		}
		
		return found;
	}
	
	/**
	 * Checks each square of the puzzle.
	 * 
	 * Result Codes are as follows:
	 * -1: Puzzle could not be initialized or has not been entered. If it has been entered, there was likely a format error.
	 * 0: Puzzle is complete and without error.
	 * 1: Puzzle is incomplete, but without any known error.
	 * 2: Puzzle holds conflicting values.
	 * 3: Puzzle holds values beyond the scope the of puzzle.
	 * @return A code specifying describing the result, as specified in the description of this function.
	 */
	public int validate() {
		int resultCode = -1;
		if(sudokuAttempt != null) {
			resultCode = sudokuAttempt.validate();
		}
		
		return resultCode;
	}
	
	
	
	/**
	 * Compares the cells of other puzzle to the one contained in this solver.
	 * 
	 * Result codes are as follows:
	 * -1: One or both of the puzzles are not initialized.
	 * 0: The other puzzle's cells are identical to this puzzle.
	 * 1: The other puzzle is a more complete version of this one.
	 * 2: This puzzle is a more complete version of the other one.
	 * 3: The two puzzles do not overlap.
	 * 4: The puzzles are of different dimensions.
	 * 
	 * @param otherPuzzle The other puzzle to compare to this one.
	 * @return A code specifying describing the result, as specified in the description of this function.
	 */
	public int compare(Sudoku otherPuzzle) {
		return sudokuAttempt.compare(otherPuzzle);
	}
	
	public ArrayList<String> getPrintableXORConditions(){
		return sudokuAttempt.getPrintableXORConditions();
	}
}
