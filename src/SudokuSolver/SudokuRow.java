/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * @author drbob132
 * @version 1.1
 * @date 04/12/2018
 */

package SudokuSolver;

import java.util.*;

public class SudokuRow {

	private final boolean DEBUG = false;
	
	//used to allow values higher than 9, extending into A-Z and thereon after.
	//Characters between 9 and A are nonsense
	public final int AFTER_9_PRINT_OFFSET = 7;
	
	private SudokuSquare[] squares;
	private boolean completed;

	public SudokuRow(SudokuSquare[] squares){
		SudokuSquare[] squareCopy = new SudokuSquare[squares.length];
		System.arraycopy(squares, 0, squareCopy, 0, squares.length);
		this.squares = squareCopy;
		completed = false;
		
		if(getClass().getName().contains("SudokuRow")) {
			for(SudokuSquare square : squares) {
				square.setRow(this);
			}
		}
	}
	
	public boolean contains(int value){
		for(int i=0; i<squares.length; i++){
			if(DEBUG) {
				System.out.println("[" + getClass() + ".contains(); " + squares[i].getValue() + "?=" + value + "]");
			}
			if(squares[i].getValue() == value){
				if(DEBUG) {
					System.out.println("[found "+value+"]");
				}
				return true;
			}
		}
		return false;
	}
	
	public String print(){
		String rowString = "|";
		char valueChar;
		for(int i=0; i<squares.length; i++){
			if(squares[i].getValue() <= 0)
				//if empty, print blank
				valueChar = ' ';
			else{
				valueChar = '0';
				valueChar += squares[i].getValue();
				if(valueChar > '9') {
					valueChar += AFTER_9_PRINT_OFFSET;
				}
			}
			rowString += valueChar;
			rowString += '|';
		}
		return rowString;
	}
	
	public String print(SudokuIODecoder decoderForIO){
		String rowString = "";
		if(decoderForIO.useDelimiter()) {
			rowString += decoderForIO.getDelimiter();
		}
		char valueChar;
		for(int i=0; i<squares.length; i++){
			/*if(squares[i].getValue() <= 0)
				//if empty, print blank
				valueChar = ' ';
			else{
				valueChar = '0';
				valueChar += squares[i].getValue();
				if(valueChar > '9') {
					valueChar += AFTER_9_PRINT_OFFSET;
				}
			}*/
			try {
				valueChar = decoderForIO.intToChar(squares[i].getValue());
			}catch(SudokuException e) {
				if(DEBUG) {
					System.out.print(e.getMessage());
				}
				valueChar = '*';
			}
			rowString += valueChar;
			if(decoderForIO.useDelimiter()) {
				rowString += decoderForIO.getDelimiter();
			}
		}
		return rowString;
	}
	
	public SudokuSquare getSquare(int position){
		return squares[position];
	}
	
	/**
	 * Checks each square of the puzzle.
	 * note: This will report the greatest code found.
	 * 
	 * Result Codes are as follows:
	 * 0: Puzzle is complete and without error.
	 * 1: Puzzle is incomplete, but without any known error.
	 * 2: Puzzle holds conflicting values.
	 * 3: Puzzle holds values beyond the scope the of puzzle.
	 * @return A code specifying describing the result, as specified in the description of this function.
	 */
	public int validate() {
		int resultCode = 0;
		int tempCode = 0;
		boolean[] numberFound = new boolean[squares.length];
		
		for(SudokuSquare square : squares) {
			tempCode = 0;
			
			if(square.getValue() > squares.length || square.getValue() < 0) {
				tempCode = 3;
			}else if(square.getValue() != 0 && numberFound[square.getValue()-1]) {
				tempCode = 2;
			}
			
			if(tempCode > resultCode) {
				resultCode = tempCode;
			}
		}
		
		return resultCode;
	}
	
	/**
	 * Checks if this row/block/column is completed, or one away.
	 * If one away, it will complete it.
	 * @return True if the row/block/column is completed.
	 */
	public boolean checkCompletion() throws SudokuException{
		
		if(completed){
			return completed;
		}
		
		int blankPosition = -1;
		ArrayList<Integer> foundList = new ArrayList<Integer>();
		boolean confirmedIncomplete = false;
		
		for(int i=0; i<squares.length && !confirmedIncomplete; i++){
			if(squares[i].isEmpty()){
				if(blankPosition >= 0){
					confirmedIncomplete = true;
				}else{
					blankPosition = i;
				}
			}else{
				foundList.add(squares[i].getValue());
			}
		}
		
		if(blankPosition == -1){
			completed = true;
		}else if(foundList.size() == squares.length - 1){
			Collections.sort(foundList);
			for(int i=0; i<foundList.size() && !completed; i++){
				if(foundList.get(i) != i+1){
					try{
						squares[blankPosition].set(i+1);
						completed = true;
					}catch(SudokuException e){
						throw new SudokuException("Check Completion error: " + e.getMessage());
					}
				}
			}
		}
		return completed;
	}
}
