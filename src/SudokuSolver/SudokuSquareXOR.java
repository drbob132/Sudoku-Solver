/**
 * Identifies two SudokuSquares.
 * One of the two must contain the number stored.
 * 
 * @author drbob132
 * @version 0.1
 * @date 2/23/2017
 */

package SudokuSolver;
public class SudokuSquareXOR {
	private int value;
	private SudokuSquare square1;
	private SudokuSquare square2;
	
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
	}
	
	/**
	 * checks to see if any of the blocks can not be the value, then the other must be. 
	 * @throws SudokuException
	 * @postcondition This condition is removed from all squares when satisfied
	 */
	public void checkCondition() throws SudokuException{
		
		boolean satisfied = false;

		try{
			//if one of the squares is the value, condition is already fulfilled
			if(square1.getValue() == value || square2.getValue() == value){
				satisfied = true;
			}
			
			//if a square can't be the value, then the other must be
			if(!square1.canBe(value) && !satisfied){
				setSquare(square2);
				satisfied = true;
			}else if(!square2.canBe(value) && !satisfied){
				setSquare(square1);
				satisfied = true;
			}
		}catch(SudokuException problem){
			throw problem;
		}
		
		//cleanup
		if(satisfied){
			removeSelf();
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
			throw new SudokuException("Tried to set square (" + /*square.getx() + ", " + square.gety() +*/ ") with " + value);
		}
	}
	
	/**
	 * Removes condition from squares
	 */
	private void removeSelf(){
		square1.removeCondition(this);
		square2.removeCondition(this);
	}
}
