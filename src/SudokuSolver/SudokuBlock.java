/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * Also tracks SudokuSquareXOR conditions contained in the Block.
 * @author drbob132
 * @version 0.3
 * @date 02/16/2018
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
	public void cleanXORs() {
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
	
}
