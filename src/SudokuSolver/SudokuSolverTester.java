/**
 * Tester for SudokuSolver - A symmetrical Sudoku solver - 9x9, or (n^2)x(n^2)
 * Operates on taking argument input... file or direct arguments.
 * @author drbob132
 * @version 1.3
 * @date 04/22/2018
 * 
 * Example form: *todo*
 * 
 * Arguments:
 * *todo*--scale,--size: Defaults to 9x9, also known as scale 3, as in (3^2)x(3^2) = 9x9. Use when specifying the dimensions of the Sudoku when it is not 9x9. 
 * 							Example: 2 for a 4x4 sudoku, 4 for a 16x16 Sudoku, n for a (n^2)x(n^2)
 * 							Note: Larger Sudokus are supported, but may be quite difficult to solve. Their complexity scales up exponentially, and is currently untested above scale 4
 * *todo*--brief: Reduces the output to only the puzzle solution.
 * *todo*--quiet,--silent: Makes the solver cease any output to console. Best used when specifying an output file.
 * *todo*--debug: Enables debug messages.
 * *todo*--help: Prints out possible arguments.
 * *todo*--verify: Checks the given puzzle for conflicts, then attempts to solve the puzzle, then makes a final report.
 * *todo*--verbose: Prints out more stats than you might normally need.
 * *todo*--input-encoding: Used to specify the character format for the input puzzle. When not specified, the solver will use the default for the size of sudoku. *todo*(specified below)
 * *todo*--print-encoding: Used when you want the puzzle printed in a format other than the input encoding.
 * *todo* [last argument] is the puzzle in numbers or the path to a file which contains the puzzle, with the same format.
 */

package SudokuSolver;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
	public static void main(String[] args){
		final boolean DEBUG = true;
		String puzzle="";
		long startTime = 0;
		long endTime = 0;
		long miliseconds = 0;
		long decimalPlace = 0;
		final long nanoToMili = 1000000;
		
		//check args
		if(DEBUG) {
			for(int i=0; i<args.length; i++) {
				System.out.println("args[" + i + "] = " + args[i]);
			}
		}
		
		if(args.length < 1){
			System.out.println("Please give me something in the arguments!");
		}else{
			File file = new File(args[0]);
			//if it's a file, go get it and read puzzle
			if(file.exists()){
				try{
					System.out.println("Reading file...");
					Scanner s = new Scanner(file);
					while(s.hasNext()){
						puzzle += s.nextLine();
					}
					s.close();
					
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}else{ //else, it's assumed to be a puzzle outright
				System.out.println("Reading argument...");
				puzzle = args[0];
			}
			
			//initialize SudokuSolver
			System.out.println("Initializing SudokuSolver...");
			SudokuSolver solver = new SudokuSolver();
			SudokuIODecoder decoder = new SudokuIODecoder(" 123456789ABCDEFG"/*/SudokuIODecoder.TYPICAL9X9_SPACEBLANKS*/);
			int sudokuSideLength = 16;
			try{
				//enter puzzle
				solver.enterSudoku(puzzle, sudokuSideLength, decoder);
			}catch(SudokuException e){
				System.out.println(e.getMessage());
			}
			//print puzzle
			System.out.println("Printing puzzle before attempt...");
			System.out.print(solver.print());
			
			startTime = System.nanoTime();
			try{
				//attempt puzzle
				System.out.println("Attempting to solve puzzle...");
				solver.solveFull();
			}catch(SudokuException e){
				System.out.println(e.getMessage());
			}
			endTime = System.nanoTime();
			
			//print result
			System.out.println("Printing puzzle after attempt...");
			System.out.print(solver.print());
			System.out.println("Number of times blocks were searched: " + solver.getBlockSearchCount());
			System.out.println("Number of iterations: " + solver.getIterations());
			System.out.println("Number of XOR conditions remaining: " + solver.getXORConditionCount());
			
			ArrayList<String> xorConditions = solver.getPrintableXORConditions();
			for(String description : xorConditions) {
				System.out.println("[" + description + "]");
			}

			miliseconds = (endTime - startTime)/nanoToMili;
			decimalPlace = (endTime - startTime)%nanoToMili;
			System.out.println("(Time Elapsed: " + miliseconds + "." + decimalPlace + " ms)");
			
		}
	}
}
