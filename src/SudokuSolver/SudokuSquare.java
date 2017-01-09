package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquare {
	int value = 0;
	SudokuRow row;
	SudokuColumn column;
	SudokuBlock block;
	ArrayList<SudokuSquareXOR> conditions;
}
