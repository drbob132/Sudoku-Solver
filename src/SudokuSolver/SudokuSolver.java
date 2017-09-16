/**
 * Solves a given sudoku, using basic rules and XOR conditions.
 * Programmed for expansion -> Sudoku N (N^2xN^2), where N is the block width (Sudoku 3 is the vanilla 9x9 sudoku)
 * Designed to operate in steps, so progression can be observed
 * 
 * @author drbob132
 */

package SudokuSolver;

public class SudokuSolver {
	private Sudoku sudokuAttempt;
	
	//used for detecting when progress has halted.
	private int mostRecentValueFound; //Identifies the most recent number found
	private int mostRecentBlock; //Identifies the block that the most recent value was found in
	private boolean valueFoundInCurrentValue; //If a value was found in current check - Used to reiterate block search when finding values and avoid early termination
	
	public SudokuSolver(){
		mostRecentValueFound = 0;
		mostRecentBlock = 0;
	}
	
	/**
	 * Populates a Sudoku object using
	 * @param puzzle
	 * @throws SudokuException
	 */
	public void enterSudoku(String puzzle) throws SudokuException{
		int[] numbers = new int[Sudoku.SUDOKU_NUMBER_OF_SQUARES];
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
				
				numbers[index++] = tempValue;
			}
		}
		
		if(index < Sudoku.SUDOKU_NUMBER_OF_SQUARES){
			throw new SudokuException("Not enough numbers/spaces in string to enter puzzle (index: " + index + ", argChars: " + puzzle.length() + ")");
		}
		
		sudokuAttempt = new Sudoku(numbers);
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
		 * Do until progress halts
		 *   (start at number 1)
		 *   Do until number can't be discovered in remaining blocks
		 *     ((Step code start))
		 *     Query squares in block that can contain number (rows and columns internally track this)
		 *       Check rows & columns
		 *       *W* If square is hosting a pair of overlapping XOR conditions, assert that the number is one of the two conditions
		 *       *W* Accounts for alignment ("phantom numbers") from other blocks
		 *     If only 1 position
		 *       Assign number to that square
		 *       (XOR conditions automatically trigger on assignment, and so does completing a row/column/block)
		 *     else
		 *       if 2 positions
		 *         Create XOR condition for those squares, and block (This is effectively found)
		 *       else if 0 positions
		 *         Puzzle is invalid!
		 *       *W* If the positions align to a column/row
		 *         *W* Assert that the value is in the current block to the respective column/row
		 *     Increment block (loop back to 1 if out of bounds, skip blocks where number is found or holds an XOR for that number)
		 *     If no block can be picked, the number is fully discovered. (XORs may still exist, and will have to be resolved.)
		 *     ((Step code stop))
		 *   Increment number (loop back to 1 if out of bounds, and skip fully discovered numbers)
		 *   If no number can be picked, the puzzle is solved. (XORs may still exist, and will have to be resolved.)
		 */
	}
	
	/**
	 * 
	 * @return position of most recent square found
	 */
	public int solveStep(){
		return -1;
	}
}
