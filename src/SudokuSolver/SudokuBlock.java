/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * Also tracks SudokuSquareXOR conditions contained in the Block.
 * @author drbob132
 * @version 1.1
 * @date 04/18/2018
 */

package SudokuSolver;

import java.util.ArrayList;


public class SudokuBlock extends SudokuRow {

	private final boolean DEBUG = false;
	
	private ArrayList<SudokuSquareXOR> xorConditions;
	
	public SudokuBlock(SudokuSquare[] squares){
		super(squares);
		
		if(getClass().getName().contains("SudokuBlock")) {
			for(SudokuSquare square : squares) {
				square.setBlock(this);
			}
		}
		
		xorConditions = new ArrayList<SudokuSquareXOR>();
	}
	
	public boolean hasDiscovered(int value){
		if(DEBUG) {
			System.out.println("[" + getClass() + ".hasDiscovered(); Printing current block]\n" + toString() + "");
		}
		boolean found = contains(value);
		if(!found){
			if(hasXOR(value)){
				if(DEBUG) {
					System.out.println("[" + getClass() + ".hasDiscovered(); Block has XOR for " + value + "]");
				}
				found = true;
			}
		}else if(DEBUG) {
			System.out.println("[" + getClass() + ".hasDiscovered(); block contained " + value + "]");
		}
			
		return found;
	}
	
	public boolean hasXOR(int value){
		for(SudokuSquareXOR xor : xorConditions){
			if(xor.getValue() == value){
				return true;
			}
		}
		return false;
	}
	
	public void addXOR(int value, int position1, int position2){
		SudokuSquare square1 = getSquare(position1);
		SudokuSquare square2 = getSquare(position2);
		
		SudokuSquareXOR xor = new SudokuSquareXOR(value, square1, square2);
		xorConditions.add(xor);
		square1.addCondition(xor);
		square2.addCondition(xor);
	}
	
	/**
	 * Returns the count of conditions found in this block.
	 * @return The count of conditions found in this block.
	 */
	public int getXORConditionCount() {
		cleanXORs();
		return xorConditions.size();
	}
	
	/**
	 * removes any satisfied XORs from the list.
	 */
	private void cleanXORs() {
		SudokuSquareXOR xor;
		int i=0;
		while(i < xorConditions.size()) {
			xor = xorConditions.get(i);
			if(xor.isSatisfied()) {
				xorConditions.remove(xor);
			}else {
				i++;
			}
		}
	}
	
	/**
	 * Returns the SudokuSquares found in SudokuSquareXORs in this block. Each adjacent pair belongs to an XOR.
	 * These XORs are in order of creation. 
	 * Typically used in conjunction with getXORValues() for the value that is implied in those squares, in the same order.
	 * @return The SudokuSquares found in SudokuSquareXORs
	 */
	public SudokuSquare[] getXORSquares(){
		SudokuSquare[] xorSquareArray = new SudokuSquare[xorConditions.size()*2];
		ArrayList<SudokuSquare> squareList;
		int squareIndex = 0;
		
		for(SudokuSquareXOR xor : xorConditions) {
			squareList = xor.getSquares();
			for(SudokuSquare square : squareList) {
				xorSquareArray[squareIndex++] = square;
			}
		}
		
		return xorSquareArray;
	}
	
	/**
	 * Returns the values found in SudokuSquareXORs in this block.
	 * These XORs are in order of creation. 
	 * Typically used in conjunction with getXORSquares() for the squares that the value is implied in, in the same order.
	 * @return The values that the SudokuSquareXORs are to assign.
	 */
	public int[] getXORValues() {
		int[] xorValueArray = new int[xorConditions.size()];
		int value;
		int squareIndex = 0;
		
		for(SudokuSquareXOR xor : xorConditions) {
			value = xor.getValue();
			xorValueArray[squareIndex++] = value;
		}
		
		return xorValueArray;
	}
	
	/**
	 * Checks the XOR conditions held in this object, and completes any conditions that are satisfied, then clears any which are no longer required.
	 * @throws SudokuException In the event that an XOR can be satisfied, but cannot set the target square because the it has been
	 *  filled with another number. This is typically the result of an incorrectly placed XOR, or an incorrectly filled square.
	 */
	public void checkConditions() throws SudokuException{
		int i = 0;
		int startingSize = xorConditions.size();
		try {
			while(i < xorConditions.size()) {
				xorConditions.get(i++).checkCondition();
			}
		}catch(SudokuException e) {
			throw e;
		}
		if( xorConditions.size() < startingSize) {
			cleanXORs();
		}
	}
}
