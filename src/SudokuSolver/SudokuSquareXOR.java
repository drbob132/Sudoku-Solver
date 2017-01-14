package SudokuSolver;
/**
 * Identifies two SudokuSquares.
 * One of the two must contain the number stored.
 * 
 * @author drbob132
 *
 */
public class SudokuSquareXOR {
	private int number;
	private SudokuSquare square1;
	private SudokuSquare square2;
	
	public SudokuSquareXOR(int num, SudokuSquare sq1, SudokuSquare sq2){
		square1 = sq1;
		square2 = sq2;
		number = num;
	}
	
	public void checkCondition(){
		//If square is not empty or can not be the number
		//Checking isEmpty first, as it's lower overhead.
		if(!square1.isEmpty()){
			setSquare(square2);
		}else if(!square2.isEmpty()){
			setSquare(square1);
		}else if(!square1.canBe(number)){
			setSquare(square2);
		}else if(!square2.canBe(number)){
			setSquare(square1);
		}
	}
	
	/**
	 * Sets SudokuSquare, and removes the condition from each square, to prevent extra checks
	 * @param square
	 * @precondition Called when other square is excluded.
	 */
	private void setSquare(SudokuSquare square) throws SudokuException{
		if(square.isEmpty()){
			square.set(number);
		}else{
			throw new SudokuException("Tried to set square (" + square.getx() + ", " + square.gety() + ") with " + number);
		}
}
