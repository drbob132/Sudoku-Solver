package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquare {
	private int value = 0;
	private SudokuRow row;
	private SudokuColumn column;
	private SudokuBlock block;
	private ArrayList<SudokuSquareXOR> conditions; //Could technically be up to 9 of these
}
