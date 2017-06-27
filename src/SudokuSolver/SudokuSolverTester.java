package SudokuSolver;

import java.io.*;
import java.util.Scanner;

public class SudokuSolverTester {
	public static void main(String[] args){
		String puzzle="";
		
		//check args
		if(args.length > 0){
			File file = new File(args[0]);
			//if it's a file, go get it and read puzzle
			if(file.exists()){
				try{
					System.out.println("Reading file...");
					
					Scanner s = new Scanner(file);
					while(s.hasNext()){
						puzzle += s.next();
					}
					s.close();
					
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}else{ //else, it's assumed to be a puzzle outright
				System.out.println("Reading argument...");
				puzzle = args[0];
			}
			try{				
				//initialize SudokuSolver
				SudokuSolver solver = new SudokuSolver();
				
				//enter puzzle
				solver.enterSudoku(puzzle);
				
				//print puzzle
				System.out.print(solver.toString());
				
				//attempt puzzle
				
				//print result
			
			}catch(SudokuException e){
				System.out.println(e.getMessage());
			}
		}else{
			System.out.println("Please give me something in the arguments!");
		}
	}
}
