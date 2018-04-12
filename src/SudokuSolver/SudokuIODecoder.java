/**
 * Enumerates the Sudoku numbers for two-way conversion from characters to integers, for use in the SudokuSolver, then back to characters for printing.
 * @author drbob132
 * @version 0.1
 * @date 04/11/2018
*/

package SudokuSolver;

public class SudokuIODecoder {

	public static final String TYPICAL9X9_ZEROBLANKS = "0123456789";
	public static final String TYPICAL9X9_SPACEBLANKS = " 123456789";
	public static final String TYPICAL16X16_SPACEBLANKS = " 0123456789ABCDEF";
	
	private String index;
	
	/**
	 * Initializes a Decoder/encoder module for converting between integers used in SudokuSolver and characters used to represent those integers.
	 * The first character must represent the blank character (ie ' ' or '0')
	 * The remaining characters (n^2 of them) represent the values seen in the puzzle. (ie "123456789" or "0123456789ABCDEF")
	 * An example of a typical 9x9 Sudoku would be " 123456789" 
	 * @param reference The reference starting with the blank character, then the characters that represent the values in the Sudoku.
	 */
	public SudokuIODecoder(String reference){
		index = reference;
	}
	
	public char intToChar(int x) throws SudokuException{
		if(x >= index.length()) {
			throw new SudokuException("Value out of bounds. Could not covert back to characters. I really hope this error doesn't show up.");
		}
		
		char symbol = index.charAt(x);
		
		return symbol;
	}
	
	public int charToInt(char x) throws SudokuException{
		boolean found = false;
		int i;
		
		for(i=0; i < index.length() && !found; i++) {
			if(x == index.charAt(i)) {
				found = true;
			}
		}
		if(i >= index.length()) {
			throw new SudokuException("Character not found in reference index.");
		}
		return i;
	}
}
