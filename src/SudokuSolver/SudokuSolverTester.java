/**
 * Tester for SudokuSolver - A symmetrical Sudoku solver - 9x9, or (n^2)x(n^2)
 * Operates on taking argument input... file or direct arguments.
 * @author drbob132
 * @version 1.4
 * @date 05/09/2018
 * 
 * Example form: *todo*
 * 
 * Arguments:
 * --scale,--size: 			Defaults to 9x9, also known as scale 3, as in (3^2)x(3^2) = 9x9. Use when specifying the dimensions of the Sudoku when it is not 9x9. 
 * 								Example: 2 for a 4x4 sudoku, 4 for a 16x16 Sudoku, n for a (n^2)x(n^2)
 * 								Note: Larger Sudokus are supported, but may be quite difficult to solve. Their complexity scales up exponentially, and is currently untested above scale 4
 * --csv-test:				Attempts each puzzle in a csv and compares the result to the solutions provided. Statistics provided after completion. If a Number is provided, that many puzzles will be attempted.
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
 * *todo*--max-iterations, --iterations: *Advanced users only* Overrides the default max number of iterations the solver will run. (This would typically need to be specified if the solver terminates because of an iteration limit, or ) -1 for unlimited.
 * 
 * *todo* [last argument] is the puzzle in numbers or the path to a file which contains the puzzle, with the same format.
 */

package SudokuSolver;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
	private static final boolean DEBUG = false;
	private static String puzzleContent = "";
	private static String invalidArguments = "";
	private static Long startPuzzleTime = 0L;
	private static Long endPuzzleTime = 0L;
	private static Long miliseconds = 0L;
	private static Long decimalPlace = 0L;
	//
	private static final long nanoToMili = 1000000;
	//defaults to typical size of a sudoku
	private static int sudokuSideLength = 9;
	private static int csvSudokuLimit = 5;
	private static SudokuIODecoder inputDecoder = null;
	private static SudokuIODecoder outputDecoder = null;
	
	private static boolean errorInProcess = false;
	private static boolean printDetails = true;
	private static boolean csvPuzzleSet = false;
	private static boolean verbose = false;
	
	private static SudokuSolver solver;
	private static Sudoku copyOfOriginal;
	
	
	public static void main(String[] args){
		readArgs(args);
		if(!errorInProcess) {
			if(csvPuzzleSet) {
				if(DEBUG) {
					System.out.println("DEBUG: loading csv tester");
				}
				csvSolver(args[args.length - 1]);
			}else {
				if(DEBUG) {
					System.out.println("DEBUG: loading single puzzle tester");
				}
				singlePuzzle(args[args.length - 1]);
			}
		}
	}
	
	private static void readArgs(String[] args) {
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
						if(DEBUG) {
							System.out.println("DEBUG: arg: --scale");
						}
						try {
							sudokuSideLength = Integer.parseInt(args[i+1]);
							//it must be squared after accepting it.
							sudokuSideLength = sudokuSideLength*sudokuSideLength;
							i++;
						}catch(IndexOutOfBoundsException e) {
							invalidArguments += args[i] + " expects an int afterwards.\n";
							errorInProcess = true;
						}catch(NumberFormatException e) {
							invalidArguments += args[i] + " could not parse \"" + args[i+1] + " as an integer.\n";
							errorInProcess = true;
						}
						break;
					case "--csv-test":
						csvPuzzleSet = true;
						if(DEBUG) {
							System.out.println("DEBUG: arg: --csv-test");
						}
						try {
							csvSudokuLimit = Integer.parseInt(args[i+1]);
							i++; //only incremented if parse works
						}catch(IndexOutOfBoundsException e) {
							invalidArguments += args[i] + " expects a csv file source.\n";
							errorInProcess = true;
						}catch(NumberFormatException e) {
							//in this case, it'll run until end of file, or the int limit, if you've got one crazy csv.
							csvSudokuLimit = 0;
						}
						break;
					default:
						invalidArguments += args[i] + " is not a known argument.\n";
						errorInProcess = true;
				}
					
			}
		}
	}
	
	private static void singlePuzzle(String puzzle) {
		try {
			puzzleContent = getPuzzleContent(puzzle);
		}catch(IndexOutOfBoundsException e) {
			System.out.println("Please submit a puzzle in the arguments! (At the end)");
			errorInProcess = true;
		}
		if(!errorInProcess) {
			System.out.println("Initializing SudokuSolver...");
			initializeSolver();
		}
		if(!errorInProcess) {
			copyOfOriginal = solver.getSudokuAttempt();
			//print puzzle
			System.out.println("Printing puzzle before attempt...");
			System.out.print(solver.print());
			
			System.out.println("\nAttempting puzzle Validation...");
			System.out.println("Validation result: " + solver.getValidateMessage(solver.validate()) + "\n");
			
			startPuzzleTime = System.nanoTime();
			System.out.println("Attempting to solve puzzle...");
			runSolver();
			if(printDetails) {
				printDetails();
			}
		}
	}
	

	private static void csvSolver(String puzzleCSV) {
		File Puzzlefile = new File(puzzleCSV);
		String[] puzzleAndSolution;
		SudokuSolver dummyForComparison = new SudokuSolver();
		int[] results = new int[5];
		long startBatchTime = 0L;
		long endBatchTime = 0L;
		long latestUpdate = 0L;
		long updatePeriod = nanoToMili*1000;
		int verifyCode;
		
		if(inputDecoder == null) {
			inputDecoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_ZEROBLANKS);
		}
		if(outputDecoder == null) {
			outputDecoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_SPACEBLANKS);
		}
		
		//if it's a file, go get it and read puzzle
		if(Puzzlefile.exists()){
			startBatchTime = System.nanoTime();
			try{
				Scanner s = new Scanner(Puzzlefile);
				for(int i=0; (csvSudokuLimit==0 || i < csvSudokuLimit) && s.hasNextLine(); i++) {
					puzzleAndSolution = s.nextLine().split(",");
					puzzleContent = puzzleAndSolution[0];
					initializeSolver();
					copyOfOriginal = solver.getSudokuAttempt();
					
					try{
						//attempt puzzle
						solver.solveFull();
					}catch(SudokuException e){
						System.out.println(e.getMessage());
						errorInProcess = true;
					}
					
					if(verbose) {
						System.out.print("Before:\n" + copyOfOriginal.print(outputDecoder));
						System.out.print("After:\n" + solver.print(outputDecoder));
					}
					if((System.nanoTime() - latestUpdate) >= updatePeriod) {
						latestUpdate = System.nanoTime();
						System.out.println("- - -");
						System.out.println("Puzzles processed: " + i + " of " + csvSudokuLimit);
					}
					verifyCode = solver.validate();
					results[verifyCode + 1]++;
					if(DEBUG && verifyCode != 0) {
						System.out.println(solver.print(outputDecoder));
						ArrayList<String> xorConditions = solver.getPrintableXORConditions();
						for(String description : xorConditions) {
							System.out.println("[" + description + "]");
						}
					}
				}
				s.close();
			}catch(IOException e){
				System.out.println(e.getMessage());
				errorInProcess = true;
			}
			endBatchTime = System.nanoTime();
			
			System.out.println("\n");
			Long seconds = TimeUnit.NANOSECONDS.toSeconds(endBatchTime - startBatchTime);
			Long milliseconds = TimeUnit.NANOSECONDS.toMillis(endBatchTime - startBatchTime);
			System.out.println("(Time Elapsed: " + seconds.toString() + "." + milliseconds.toString() + " s)");
			System.out.println("Puzzle results:");
			for(int i=0; i<results.length;i++) {
				System.out.println("\"" + solver.getValidateMessage(i - 1) + "\": " + results[i]);
			}
		}
		
		
	}
	
	private static String getPuzzleContent(String arg) {
		File Puzzlefile = new File(arg);
		String content = "";
		//if it's a file, go get it and read puzzle
		if(Puzzlefile.exists()){
			try{
				if(DEBUG) {
					System.out.println("Reading file...");
				}
				Scanner s = new Scanner(Puzzlefile);
				while(s.hasNext()){
					content += s.nextLine();
				}
				s.close();
				
			}catch(IOException e){
				System.out.println(e.getMessage());
				errorInProcess = true;
			}
		}else{ //else, it's assumed to be a puzzle outright
			if(DEBUG) {
				System.out.println("Reading argument...");
			}
			content = arg;
		}
		return content;
	}
	
	private static void initializeSolver() {
		solver = new SudokuSolver();
		
		//defaulting decoder
		if(inputDecoder == null) {
			switch(sudokuSideLength) {
				case 9:
					inputDecoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_SPACEBLANKS);
					break;
				case 16:
					inputDecoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL16X16_SPACEBLANKS_STARTATONE);
					break;
				default:
					inputDecoder = new SudokuIODecoder(SudokuIODecoder.TYPICAL9X9_SPACEBLANKS);
			}
		}
		
		try{
			//enter puzzle
			solver.enterSudoku(puzzleContent, sudokuSideLength, inputDecoder);
		}catch(SudokuException e){
			System.out.println(e.getMessage());
			errorInProcess = true;
		}
	}
	
	private static void runSolver() {
		
		startPuzzleTime = System.nanoTime();
		try{
			//attempt puzzle
			solver.solveFull();
		}catch(SudokuException e){
			System.out.println(e.getMessage());
			errorInProcess = true;
		}
		endPuzzleTime = System.nanoTime();
	}
	
	private static void printDetails() {
		System.out.println("result: " + solver.getMessage());
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

		miliseconds = (endPuzzleTime - startPuzzleTime)/nanoToMili;
		decimalPlace = (endPuzzleTime - startPuzzleTime)%nanoToMili;
		System.out.println("(Time Elapsed: " + miliseconds + "." + decimalPlace + " ms)");
		System.out.println(invalidArguments);
	}
}
