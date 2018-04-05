/**
 * Solves a given sudoku, using basic rules and XOR conditions.
 * Programmed for expansion -> Sudoku N (N^2xN^2), where N is the block width (Sudoku 3 is the vanilla 9x9 sudoku)
 * Designed to operate in steps, so progression can be observed
 * 
 * @author drbob132
 * @version 0.3
 * @date 04/03/2018
 */

package SudokuSolver;

import java.util.ArrayList;

public class SudokuSolver {

	private static final boolean DEBUG = false;
	
	private Sudoku sudokuAttempt;
	
	
	//used for detecting when progress has halted.
	private int mostRecentvalueFound; //Identifies the most recent number found
	private int mostRecentBlock; //Identifies the block that the most recent value was found in
	private boolean mostRecentFirstPass;
	private boolean progressHalted;
	private int iterations;
	private int maxIterations = 1000;
	
	//tracking current state (in object context, as this will have step functionality
	private ArrayList<ArrayList> valuesToFind;
	private int currentValue;
	private int currentBlock;
	private int targetBlock; //Identifies the last block to search; the block that the most recent value was found in
	private boolean valueFoundInBlock; //If a value was found in current check - Used to reiterate block search when finding values and avoid early termination
	
	public SudokuSolver(){
		mostRecentvalueFound = 1;
		mostRecentBlock = 0;
		valueFoundInBlock = true; //hacky override to get it started without potential endless loop.
		progressHalted = false;
		currentValue = 1;
		currentBlock = 0;
		targetBlock = Sudoku.SUDOKU_SIDE_LENGTH - 1;
		iterations = 0;
	}
	
	/**
	 * Populates a Sudoku object using
	 * @param puzzle
	 * @throws SudokuException
	 */
	public void enterSudoku(String puzzle) throws SudokuException{
		int[] values = new int[Sudoku.SUDOKU_NUMBER_OF_SQUARES];
		int tempValue;
		int index = 0;
		int tempChar;
		
		if(puzzle.length() < Sudoku.SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Not enough characters to enter puzzle");
		}
		//not testing for too many, as I'd expect people to want to format things weirdly
		
		for(int i=0; index<Sudoku.SUDOKU_NUMBER_OF_SQUARES && i<puzzle.length(); i++){
			//get number
			tempChar = puzzle.charAt(i);
			
			//scrubbing
			if(tempChar >= '0' && tempChar <= '0' + Sudoku.SUDOKU_NUMBER_OF_SQUARES){
				tempValue = tempChar - '0';
				
				values[index++] = tempValue;
			}
		}
		
		if(index < Sudoku.SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Not enough numbers/spaces in string to enter puzzle (index: " + index + ", argChars: " + puzzle.length() + ")");
		}
		
		sudokuAttempt = new Sudoku(values);
	}
	
	public String print(){
		return sudokuAttempt.print();
	}
	
	/**
	 * @return The details of any remaining stored conditions that assert a number's possible positions.
	 */
	public String getXORConditions(){
		return ;
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
		try{
			do{ //until progress halted
				currentBlock = 0;
				targetBlock = Sudoku.SUDOKU_SIDE_LENGTH;
				firstPass = true;
				do{ //until this value can't be found currently
					currentBlock = currentBlock % Sudoku.SUDOKU_SIDE_LENGTH;
					valueFoundInBlock = false;
					if(!(sudokuAttempt.blockContains(currentBlock, currentValue))){
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
						iterations++;
					}else if(DEBUG){
						System.out.println("Value " + currentValue + " in block #" + currentBlock + " already discovered.");
					}
					firstPass = false;
					currentBlock++;
				}while( !(currentBlock == targetBlock && !valueFoundInBlock) && iterations < maxIterations || firstPass);
				
				currentValue = currentValue % Sudoku.SUDOKU_SIDE_LENGTH + 1; // max number would become 1
				
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
			
			
		}catch(SudokuException e){
			progressHalted = true;
			throw e;
		}
	}
	
	public Sudoku getSudokuAttempt() {
		return new Sudoku(sudokuAttempt);
	}

	public int getIterations() {
		return iterations;
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
	 * 
	 * @return position of most recent square found
	 */
	public int solveStep(){
		return -1;
	}
	
	private boolean findValueInBlock(int value, int block) throws SudokuException{

		ArrayList<Integer> possiblePositions = new ArrayList<Integer>();
		
		boolean found = false;
		if(!(sudokuAttempt.blockContains(block, value))){
	        //find squares in block that can contain Number (rows and columns internally track this)
			for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
				if(sudokuAttempt.squareAtPositionCanBe(Sudoku.blockSquareIndex(block, i), value)){
					possiblePositions.add(i);
				}
			}
	        //If only 1 position
			if(possiblePositions.size() == 1){
		        //    Assign Number to that square
		        //    (XOR conditions automatically trigger on assignment, and so does completing a row/column/block)
				try{
					sudokuAttempt.setSquare(Sudoku.blockSquareIndex(block, possiblePositions.get(0)), value);
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
}
