/**
 * Identifies two SudokuSquares.
 * One of the two must contain the number stored.
 * 
 * @author drbob132
 * @version 1.1
 * @date 4/18/2018
 */

package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquareXOR {
	private int value;
	private SudokuSquare square1;
	private SudokuSquare square2;
	private boolean satisfied;
	
	/**
	 * Creates a condition that requires one of the two SudokuSquares to be the given value.
	 * @param value
	 * @param square1
	 * @param square2
	 */
	public SudokuSquareXOR(int value, SudokuSquare square1, SudokuSquare square2){
		this.square1 = square1;
		this.square2 = square2;
		this.value = value;
		this.satisfied = false;
	}
	
	/**
	 * checks to see if any of the blocks can not be the value, then the other must be. 
	 * @throws SudokuException
	 * @postcondition This condition is removed from all squares when satisfied
	 */
	public void checkCondition() throws SudokuException{
		
		boolean satisfied = false;
		SudokuSquare target = null;

		//if one of the squares is the value, condition is already fulfilled
		if(square1.getValue() == value || square2.getValue() == value){
			satisfied = true;
		}
		
		//if a square can't be the value, then the other must be
		if(!square1.canBe(value) && !satisfied){
			target = square2;
		}else if(!square2.canBe(value) && !satisfied){
			target = square1;
		}

		try{
			if(target != null){
				removeSelf();
				if(target != null){
					setSquare(target);
					satisfied = true;
				}
			}
		}catch(SudokuException problem){
			throw problem;
		}
	}
	
	/**
	 * Sets SudokuSquare, and removes the condition from each square, to prevent extra checks
	 * @param square
	 * @precondition Called when other square is excluded.
	 */
	private void setSquare(SudokuSquare square) throws SudokuException{
		if(square.isEmpty()){
			square.set(value);
		}else{
			throw new SudokuException("XOR tried to set square (" + /*square.getx() + ", " + square.gety() +*/ ") with " + value);
		}
	}
	
	/**
	 * Removes this condition from squares
	 */
	private void removeSelf(){
		square1.removeCondition(this);
		square2.removeCondition(this);
	}

	/**
	 * @return True if the condition has been satisfied
	 */
	public boolean isSatisfied() {
		return checkSatisfaction();
	}
	
	private boolean checkSatisfaction() {
		if(square1.getValue() == value || square2.getValue() == value ) {
			satisfied = true;
		}else {
			satisfied = false;
		}
		return satisfied;
	}
	
	/**
	 * @return The value this XOR implies. 
	 */
	public int getValue(){
		return value;
	}
	
	public ArrayList<SudokuSquare> getSquares(){
		ArrayList<SudokuSquare> tempArray = new ArrayList<SudokuSquare>();
		
		tempArray.add(square1);
		tempArray.add(square2);
		
		return tempArray;
	}
}
