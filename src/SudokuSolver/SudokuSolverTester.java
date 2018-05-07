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
 * *todo*--scale,--size: 	Defaults to 9x9, also known as scale 3, as in (3^2)x(3^2) = 9x9. Use when specifying the dimensions of the Sudoku when it is not 9x9. 
 * 								Example: 2 for a 4x4 sudoku, 4 for a 16x16 Sudoku, n for a (n^2)x(n^2)
 * 								Note: Larger Sudokus are supported, but may be quite difficult to solve. Their complexity scales up exponentially, and is currently untested above scale 4
 * *todo*--brief: 			Reduces the output to only the puzzle solution.
 * *todo*--quiet,--silent: 	Makes the solver cease any output to console. Best used when specifying an output file.
 * *todo*--debug: 			Enables debug messages.
 * *todo*--help: 			Prints out possible arguments.
 * *todo*--verify: 			Checks the given puzzle for conflicts or formatting issues, then attempts to solve the puzzle if possible, then makes a final report.
 * *todo*--verbose: 		Prints out more stats than you might normally need.
 * *todo*--input-encoding: 	Used to specify the character format for the input puzzle. When not specified, the solver will use the default for the size of sudoku. *todo*(specified below)
 * *todo*--print-encoding: 	Used when you want the puzzle printed in a format other than the input encoding.
 * *todo*--step: 			Runs the solver until one value is found and any that are found as an immediate consequence. 
 * *todo*--max-steps: 		Runs the solver for X steps.
 * *todo*--print-each-step: Prints the Sudoku after each step until it is finished.
 * 
 * *todo*--max-iterations, --iterations: *Advanced users only* Specifies the max number of iterations the solver will run. (This would typically need to be specified if the solver terminates because of an iteration limit, or ) -1 for unlimited.
 * 
 * *todo* [last argument] is the puzzle in numbers or the path to a file which contains the puzzle, with the same format.
 */

package SudokuSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
	public static void main(String[] args){
		final boolean DEBUG = false;
		String puzzle = "";
		String invalidArguments = "";
		long startTime = 0;
		long endTime = 0;
		long miliseconds = 0;
		long decimalPlace = 0;
		//
		final long nanoToMili = 1000000;
		//defaults to typical size of a sudoku
		int sudokuSideLength = 9;
		SudokuIODecoder decoder = null;
		
		boolean printDetails = true;
		
		//check args
		if(DEBUG) {
			for(int i=0; i<args.length; i++) {
				System.out.println("args[" + i + "] = " + args[i]);
			}
		}
		
		if(args.length < 1){
			System.out.println("Please give me something in the arguments!");
		}else{
			//Argument processing
			String arg;
			for(int i=0; i < (args.length - 1); i++) {
				arg = args[i].toLowerCase();
				
				switch(arg){
					case "--brief": 
						printDetails = false;
						break;
					case "--scale":
					case "--size":
						try {
							sudokuSideLength = Integer.parseInt(args[i+1]);
							//it must be squared after accepting it.
							sudokuSideLength = sudokuSideLength*sudokuSideLength;
							i++;
						}catch(IndexOutOfBoundsException e) {
							invalidArguments += args[i] + " expects an int afterwards.\n";
						}catch(NumberFormatException e) {
							invalidArguments += args[i] + " could not parse \"" + args[i+1] + " as an integer.\n";
						}
						break;
					default:
						invalidArguments += args[i] + " is not a known argument.\n";
				}
					
			}
			
			//puzzle get
			File file = new File(args[args.length - 1]);
			//if it's a file, go get it and read puzzle
			if(file.exists()){
				try{
					if(DEBUG) {
						System.out.println("Reading file...");
					}
					Scanner s = new Scanner(file);
					while(s.hasNext()){
						puzzle += s.nextLine();
					}
					s.close();
					
				}catch(IOException e){
					System.out.println(e.getMessage());
				}
			}else{ //else, it's assumed to be a puzzle outright
				if(DEBUG) {
					System.out.println("Reading argument...");
				}
				puzzle = args[args.length - 1];
			}
			
			//initialize SudokuSolver
			System.out.println("Initializing SudokuSolver...");
			SudokuSolver solver = new SudokuSolver();
			
			//defaulting decoder
			if(decoder == null) {
				switch(sudokuSideLength) {
					case 9:
						decoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_SPACEBLANKS);
						break;
					case 16:
						decoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL16X16_SPACEBLANKS_STARTATONE);
						break;
					default:
						decoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_SPACEBLANKS);
				}
			}
			
			try{
				//enter puzzle
				solver.enterSudoku(puzzle, sudokuSideLength, decoder);
			}catch(SudokuException e){
				System.out.println(e.getMessage());
			}
			Sudoku copyOfOriginal = solver.getSudokuAttempt();
			//print puzzle
			System.out.println("Printing puzzle before attempt...");
			System.out.print(solver.print());
			
			System.out.println("\nAttempting puzzle Validation...");
			System.out.println("Validation result: " + solver.getValidateMessage(solver.validate()) + "\n");
			
			startTime = System.nanoTime();
			try{
				//attempt puzzle
				System.out.println("Attempting to solve puzzle...");
				solver.solveFull();
			}catch(SudokuException e){
				System.out.println(e.getMessage());
			}
			endTime = System.nanoTime();

			if(printDetails) {
				//print result
				System.out.println("Printing puzzle after attempt...");
				System.out.print(solver.print());
				System.out.println("Number of times blocks were searched: " + solver.getBlockSearchCount());
				System.out.println("Number of iterations: " + solver.getIterations());
				System.out.println("Number of XOR conditions remaining: " + solver.getXORConditionCount());
				System.out.println("Compared to original: " + solver.getCompareMessage(solver.compare(copyOfOriginal)));
				System.out.println("Validation after attempt: " + solver.getValidateMessage(solver.validate()));
			
				ArrayList<String> xorConditions = solver.getPrintableXORConditions();
				for(String description : xorConditions) {
					System.out.println("[" + description + "]");
				}

				miliseconds = (endTime - startTime)/nanoToMili;
				decimalPlace = (endTime - startTime)%nanoToMili;
				System.out.println("(Time Elapsed: " + miliseconds + "." + decimalPlace + " ms)");
				System.out.println(invalidArguments);
			}
			
		}
	}
}
