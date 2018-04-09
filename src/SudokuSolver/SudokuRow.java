/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * @author drbob132
 * @version 0.3
 * @date 02/16/2018
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
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
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
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
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
	
	public SudokuSquare getSquare(int position){
		return squares[position];
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
		
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH && !confirmedIncomplete; i++){
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
		}else if(foundList.size() == Sudoku.SUDOKU_SIDE_LENGTH - 1){
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
