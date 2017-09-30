/**
 * Solves a given sudoku, using basic rules and XOR conditions.
 * Programmed for expansion -> Sudoku N (N^2xN^2), where N is the block width (Sudoku 3 is the vanilla 9x9 sudoku)
 * Designed to operate in steps, so progression can be observed
 * 
 * @author drbob132
 * @version 0.2
 * @date 9/19/2017
 */

package SudokuSolver;

import java.util.ArrayList;

public class SudokuSolver {
	
	private Sudoku sudokuAttempt;
	
	//used for detecting when progress has halted.
	private int mostRecentValueFound; //Identifies the most recent number found
	private int mostRecentBlock; //Identifies the block that the most recent value was found in
	private boolean valueFoundInCurrentValue; //If a value was found in current check - Used to reiterate block search when finding values and avoid early termination
	private boolean progressHalted;
	
	//tracking current state (in object context, as this will have step functionality
	private int currentValue;
	private int currentBlock;
	
	public SudokuSolver(){
		mostRecentValueFound = 0;
		mostRecentBlock = 0;
		valueFoundInCurrentValue = false;
		progressHalted = false;
		currentValue = 0;
		currentBlock = 0;
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
	
	public String toString(){
		return sudokuAttempt.toString();
	}
	
	/**
	 * Returns the details of any extra stored conditions that assert
	 * @return
	 */
	public String getXORConditions(){
		return "";
	}
	
	/**
	 * Attempts to solve the stored Sudoku fully without guessing.
	 * Assumes puzzle is valid until it reaches an invalid condition.
	 * If puzzle remains incomplete, it is either invalid or unsolvable (according to this algorithm)
	 * Progress and discovered conditions may be retrieved.
	 */
	public void solveFull(){
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
		
		//if restarted, for whatever reason, if it's halted, chances are you want to give it a go.
		progressHalted = false; 
		do{
			progressHalted = true;
		}while(!progressHalted);
	}
	
	/**
	 * 
	 * @return position of most recent square found
	 */
	public int solveStep(){
		return -1;
	}
	
	private boolean findValueInBlock(int value, int block) throws SudokuException{
		
		boolean found = false;
		
		if(!(sudokuAttempt.blockContains(block, value))){
	        //find squares in block that can contain Number (rows and columns internally track this)
	        //    (Check rows & columns)
			ArrayList<Integer> possiblePositions = new ArrayList<Integer>();
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
		    //else
			}else{
		        //    if 2 positions
				if(possiblePositions.size() == 2){
					// *       Create XOR condition for those squares, and block (This is effectively found)
					// *       Update mostRecentNumberFound
				//    else if 0 positions
				}else if(possiblePositions.size() == 0){
					//        Puzzle is invalid!
					throw new SudokuException("Value (" + value + ") is impossible to find in Block " + block + ".");
				}
			}
		}else{
			found = true;
		}
		
		return found;
	}
}
