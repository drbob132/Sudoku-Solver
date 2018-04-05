/**
 * Contains a value for Sudoku
 * Checks the rules in respect to this square and it's row/column/block.
 * Holds SudokuSquareXOR conditions that will be checked once they are fulfilled.
 * @author drbob132
 * @version 0.3
 * @date 02/16/2018
 */

package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquare {

	private static final boolean DEBUG = false;
	
	private int value;
	private SudokuRow row;
	private SudokuColumn column;
	private SudokuBlock block;
	private ArrayList<SudokuSquareXOR> conditions = new ArrayList<SudokuSquareXOR>(); //Could technically contain up to one of each number

	/**
	 * Initializes a square as empty
	 */
	public SudokuSquare(){
		this.value = 0;
	}
	
	/**
	 * Initializes a square with a value
	 * @param value
	 */
	public SudokuSquare(int value){
		this.value = value;
	}

	/**
	 * @param row
	 */
	public void setRow(SudokuRow row){
		this.row = row;
	}
	
	/**
	 * @
	 * @param column
	 */
	public void setColumn(SudokuColumn column){
		this.column = column;
	}
	
	/**
	 * Assigns Square to block. 
	 * Must be called after constructor.
	 * @param block Block that contains this Square
	 */
	public void setBlock(SudokuBlock block){
		this.block = block;
	}
	
	/**
	 * @return If the square does not contain a value. (Zero is considered empty)
	 */
	public Boolean isEmpty(){
		if(value == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Checks to see if the Square can contain the value, according to the rules of sudoku.
	 * @param value Value to check
	 * @return If the Square can contain the value, according to the rules of sudoku.
	 * @predcondition Row, Block & Column must be set
	 */
	public Boolean canBe(int value){
		if(!this.isEmpty()){
			return false;
		}
		if(row.contains(value) || column.contains(value) || block.contains(value)){
			return false;
		}
		return true;
	}
	
	/**
	 * @return value contained by this square
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 * Sets the square to the given value, then checks any conditions that may be active
	 * @param value value to set Square to
	 * @throws SudokuException when Square already contains a value
	 */
	public void set(int value) throws SudokuException{
		//if already set, throw error
		if(!isEmpty()){
			throw new SudokuException("Tried to set square " + /*square.getx() + ", " + square.gety() +*/ "with " + value + ", but already contains " + getValue());
		}
		
		//set value
		this.value = value;
		
		//then check conditions, and kill them
		while(!conditions.isEmpty()){
			SudokuSquareXOR condition = conditions.get(0);
			condition.checkCondition();
			if(!conditions.isEmpty()){
				conditions.remove(condition);
			}
		}
		
		/*row.checkCompletion();
		column.checkCompletion();
		block.checkCompletion();*/
	}

	/**
	 * Gets an uncontrolled Arraylist of XOR conditions from this square.
	 * @return
	 */
	public ArrayList<SudokuSquareXOR> getXOR(){
		return new ArrayList<SudokuSquareXOR>(conditions);
	}
	
	public void addCondition(SudokuSquareXOR condition){
		conditions.add(condition);
	}
	
	public void removeCondition(SudokuSquareXOR condition){
		conditions.remove(condition);
	}
}
