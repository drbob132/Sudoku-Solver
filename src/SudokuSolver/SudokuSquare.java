package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquare {
	private int value = 0;
	private SudokuRow row;
	private SudokuColumn column;
	private SudokuBlock block;
	private ArrayList<SudokuSquareXOR> conditions; //Could technically be up to 9 of these
	
	public SudokuSquare(SudokuRow row, SudokuColumn column, SudokuBlock block){
		this.row = row;
		this.column = column;
		this.block = block;
	}

	public Boolean isEmpty(){
		if(value == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public Boolean canBe(int val){
		if(!this.isEmpty()){
			return false;
		}
		if(row.contains(val) || column.contains(val) || block.contains(val)){
			return false;
		}
		return true;
	}
	
	public void set(int val){
		
	}
}
