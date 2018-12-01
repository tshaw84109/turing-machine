/**
 * Turing Machine
 * Implemented by Tyler Shaw
 * 11/30/18
 * for CS4110 Formal Languages
 * 
 * (Receives arguments of the form <path> "word")
 * Example: <C:\forProgram\anbnan.txt> "aba" 
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException; 

public class TuringMachine {
	public static void main(String[] args) {
		//Receives two console arguments of the form <filepath of Turing program> "word to be tested"
		//args[0] is the path, args[1] is the word
		
		String currentState = "Start";
		ArrayList<Character> tape = new ArrayList<Character>();
		ArrayList<Edge> edges;
		int currentTapeIndex = 0;
		boolean halted = false, crashed = false, matchFound = false;
		int transitionCount = 0;
		
		//Load Tape
		for(char c : args[1].toCharArray()) {
		    tape.add(c);
		}
		for(int i = tape.size(); i < 10000; i++) { //Add blanks at the end of the char array
			tape.add('_');
		}
		
		//Load Turing 'Program'
		try { // Try/Catch required when reading file
			edges = ReadFromFileUsingScanner(args[0]);
					
			//Main loop for Turing Machine. The machine runs until Halt, Crash, or 1000+ loop
			while(transitionCount <= 1000) {
					
				//Iterate through edges to search for an edge that matches currentEdge
				for(int i = 0; i < edges.size(); i++){
					Edge currentEdge = edges.get(i); //store for ease
							
					//If currentEdge is the matching edge, do stuff. Else keep looping until out of edges to check.
					if(currentEdge.getFrom().equals(currentState) && currentEdge.getLetter() == tape.get(currentTapeIndex) ) {
						matchFound = true;
								
						//Update TapeHead location and assign new letter to tape
						if(currentEdge.getDirection() == 'R') {
							tape.set(currentTapeIndex, currentEdge.getNewLetter());
							currentTapeIndex++;
						}
						else if(currentEdge.getDirection() == 'L' && currentTapeIndex > 0) {
							tape.set(currentTapeIndex, currentEdge.getNewLetter());
							currentTapeIndex--;
						}
						else if(currentEdge.getDirection() == 'L' && currentTapeIndex <= 0) { //Scenario: TapeHead tries to go negative
							crashed = true;
							break; //exit for and while loop when crashed because of left of zero
						}
								
						//Update State
						if(currentEdge.getToState().startsWith("Halt")) {
							halted = true; //Halt is reached
							break; //exit for and while loop when halt is reached
						}
						else {
							currentState = edges.get(i).getToState();
							break; //exit for loop when state is accepted and changed
						}
					}
				}
						
				//Exit while loop if no edge matches, this is a crash.
				if(matchFound == false) {
					crashed = true;
					break;
				}
				transitionCount++;//Counter used to check for loops
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		if(transitionCount >= 1000) {
			crashed = true;
		}
				
		if(halted == true || crashed == false) { //Halted is good! Crashed is bad.
			System.out.println("Accepted: " + args[1]);
		} else {
			System.out.println("Rejected: " + args[1]);
		}
		
	}
	
	public static ArrayList<Edge> ReadFromFileUsingScanner(String directory) throws FileNotFoundException
	{ 
		ArrayList<Edge> edges = new ArrayList<Edge>(); //arraylist of edges that will be returned
		File file; 
		Scanner scanner; 
		
		//Reformatting
	    directory = directory.replace('\\', '/'); //for windows
	    directory = directory.replaceAll("<", ""); //Remove < and >
	    directory = directory.replaceAll(">", "");
	    file = new File(directory);
	    scanner = new Scanner(file);
	    
	    //Get next line, split it up, use the pieces to create an Edge object, add the Edge object to the end of our edges ArrayList
	    while (scanner.hasNextLine()) {
	    	String next = scanner.nextLine();
	    	String[] nextArr;
	    	
	    	if(!next.trim().startsWith("#")) { //if next line doesn't start with a comment marker
	    		
	    		nextArr = next.split(", ");
	    		
	    		if(!nextArr[0].startsWith("Halt")) { //If not HALT. Halts are states, not edges. We don't need them!
	    			
	    			//Input control for when Start is provided to 'From' or 'toState'
	    			if(nextArr[0].toLowerCase().contains("start")){
	    				nextArr[0] = "Start";
	    			}
	    			if(nextArr.length == 5) {
	    				if(nextArr[4].toLowerCase().contains("start")){
	    					nextArr[4] = "Start";
	    				}
	    			}
	    			
	    			Edge edge = new Edge(nextArr[0], nextArr[1].charAt(0), nextArr[2].charAt(0), nextArr[3].charAt(0), nextArr[4]);
	    			edges.add(edge);
	    		}
	    	}
	    }
	    scanner.close();
	    
	    return edges;
	 } 
}