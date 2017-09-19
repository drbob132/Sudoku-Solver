/**
 * Logs a set of SudokuBlocks for the purpose of checking Sudoku rules.
 * Also tracks SudokuSquareXOR conditions contained in the Block.
 * @author drbob132
 * @version 0.2
 * @date 9/19/2017
 */

package SudokuSolver;

import java.util.ArrayList;


public class SudokuBlock extends SudokuRow{
	
	private ArrayList<SudokuSquareXOR> xorConditions;
	
	public SudokuBlock(SudokuSquare[] squares) throws SudokuException{
		super(squares);
		
		xorConditions = new ArrayList<SudokuSquareXOR>();
	}
	
	public boolean hasDiscovered(int value){
		boolean found = contains(value);
		if(!found){
			return hasXOR(value);
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
	
	public void addXOR(int position1, int position2, int value){
		SudokuSquare square1 = getSquare(position1);
		SudokuSquare square2 = getSquare(position2);
		
		SudokuSquareXOR xor = new SudokuSquareXOR(value, square1, square2);
		xorConditions.add(xor);
		square1.addCondition(xor);
		square2.addCondition(xor);
	}
	
}
