package SudokuSolver;

import java.util.ArrayList;

public class SudokuSquare {
	private int value;
	private SudokuRow row;
	private SudokuColumn column;
	private SudokuBlock block;
	private ArrayList<SudokuSquareXOR> conditions; //Could technically be up to 9 of these
	
	public SudokuSquare(int value){
		this.value = value;
	}

	public void setRow(SudokuRow row){
		this.row = row;
	}
	
	public void setColumn(SudokuColumn column){
		this.column = column;
	}
	
	public void setBlock(SudokuBlock block){
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
		//if already set, throw error
		//set value
		//check conditions
	}
}
