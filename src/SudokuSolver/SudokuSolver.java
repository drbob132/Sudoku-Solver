/**
 * Solves a given sudoku, using basic rules and binary XOR conditions.
 * 
 * @author drbob132
 */

package SudokuSolver;

public class SudokuSolver {
	private Sudoku sudokuAttempt;
	
	public SudokuSolver(){
		
	}
	
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
}
