/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * @author drbob132
 * @version 0.2
 * @date 9/19/2017
 */

package SudokuSolver;

import java.util.*;

public class SudokuRow {
	
	private SudokuSquare[] squares = new SudokuSquare[Sudoku.SUDOKU_SIDE_LENGTH];
	private boolean completed;

	public SudokuRow(SudokuSquare[] squares) throws SudokuException{
		super();
		this.squares = squares;
		completed = false;
		try{
			checkCompletion();
		}catch(SudokuException e){
			throw e;
		}
	}
	
	public boolean contains(int value){
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
			if(squares[i].getValue() == value){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		String rowString = "|";
		char valueChar;
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH; i++){
			if(squares[i].getValue() <= 0)
				//if empty, print blank
				valueChar = ' ';
			else{
				valueChar = '0';
				valueChar += squares[i].getValue();
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
		
		int finalPosition = -1;
		ArrayList<Integer> foundList = new ArrayList<Integer>();
		boolean confirmedIncomplete = false;
		
		for(int i=0; i<Sudoku.SUDOKU_SIDE_LENGTH && !confirmedIncomplete; i++){
			if(squares[i].isEmpty()){
				if(finalPosition >= 0){
					confirmedIncomplete = true;
				}else{
					finalPosition = i;
				}
			}else{
				foundList.add(squares[i].getValue());
			}
		}
		
		if(finalPosition == -1){
			completed = true;
		}else if(!confirmedIncomplete){
			Collections.sort(foundList);
			for(int i=0; i<foundList.size() && !completed; i++){
				if(foundList.get(i) != i+1){
					try{
						squares[i].set(i+1);
						completed = true;
					}catch(SudokuException e){
						throw e;
					}
				}
			}
		}
		return completed;
	}
}
