/**
 * Tester for SudokuSolver
 * Operates on taking argument input... file or direct arguments.
 * @author drbob132
 * @version 1.2
 * @date 04/18/2018
 */

package SudokuSolver;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
	public static void main(String[] args){
		String puzzle="";
		long startTime = 0;
		long endTime = 0;
		long miliseconds = 0;
		long decimalPlace = 0;
		final long nanoToMili = 1000000;
		
		//check args
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
	
	//totally ripped from StackExchange, then modified.
	private static String readFile(Path path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, encoding);
	}
}
