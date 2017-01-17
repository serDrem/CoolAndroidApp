package com.example.spgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import android.content.Context;
import android.graphics.Path;
import android.widget.Toast;

public class spPath {
	private int board1[][];
	private int board2[][];
	private ArrayList<int[]> visited1;
	private ArrayList<int[]> visited2;
	public final int dimension;
	public final Piece piece;
	public final int start[];
	public final int end[];
	public ArrayList<Path> paths;
	public int cellWidth;
	public ArrayList<Stack<String>> bigSeqs;
	public int sp;
	
	public spPath(Piece piece, int dimension, int start[], int end[], List<List<Integer>> obstacles, int cellWidth){
		this.piece = piece;
		this.dimension = dimension;
		board1 = new int[dimension][dimension];
		board2 = new int[dimension][dimension];
		for(int y = 0; y < dimension; y++ ){
			for(int x = 0; x < dimension; x++){
				board1[y][x] = -2;
				board2[y][x] = -2;
			}
		}
		this.cellWidth = cellWidth;
		this.start = start;
		this.end = end;
		this.paths = new ArrayList<Path>();
		this.bigSeqs = new ArrayList<Stack<String>>();
		
		visited1 = new ArrayList<int[]>();
		visited2 = new ArrayList<int[]>();
		ArrayList<Stack<String>> bigSeqs = new ArrayList<Stack<String>>();
		
		Iterator<List<Integer>> iterator = obstacles.iterator();
		while(iterator.hasNext()){
			List<Integer> pair = iterator.next();
			int y = pair.get(0);
			int x = pair.get(1);
			board1[y][x] = -1;
			board2[y][x] = -1;
		}
	
	}
	public void genBoard(){
		//put zero at start and add it to list of visited locations
		board1[start[0]][start[1]] = 0;
		board2[end[0]][end[1]] = 0;
		int visited1[][] = new int[dimension][dimension];
		int visited2[][] = new int[dimension][dimension];
		visited1[start[0]][start[1]] = 1;
		visited2[end[0]][end[1]] = 1;
		ArrayList<int[]> visited1A = new ArrayList<int[]>();
		ArrayList<int[]> visited2A = new ArrayList<int[]>();
		ArrayList<int[]> visited1B = new ArrayList<int[]>();
		ArrayList<int[]> visited2B = new ArrayList<int[]>();
		visited1A.add(start);
		visited2A.add(end);
		visited1B.add(start);
		visited2B.add(end);		
		//int counter = dimension*dimension;
		int counter = dimension - 1;
		Iterator iterator1;
		Iterator iterator2;
		Iterator iterator3;
		int currentLoc[];
		ArrayList<int[]> reachable;
		int move[];
		while(counter > 0){
			//for all visited locations
			int i = 0;
			for( i = 0; i < visited1A.size(); i++) {
				currentLoc = visited1A.get(i);
				//update all reachable locations with new move data
				iterator3 = piece.moves.iterator();
				while(iterator3.hasNext()){
					move = (int[]) iterator3.next();
					int newLocation[] = {currentLoc[0] + move[0], currentLoc[1] + move[1]}; 
					while(true){						
						//if the move is to a location that is on the board and is not an obstacle and has not been visited before 
						if(newLocation[0] >= 0 && newLocation[1] >= 0 
								&& newLocation[0] < dimension && newLocation[1] < dimension
								&& board1[newLocation[0]][newLocation[1]] != -1
								&& visited1[newLocation[0]][newLocation[1]] == 0
								){
							//update the board if you never visited before or you have a lower value
							if (board1[newLocation[0]][newLocation[1]] == -2 
									|| (board1[currentLoc[0]][currentLoc[1]] + 1) < board1[newLocation[0]][newLocation[1]] ){
								board1[newLocation[0]][newLocation[1]] = board1[currentLoc[0]][currentLoc[1]] + 1;
								
								//add to visited
								visited1[newLocation[0]][newLocation[1]] = 1;
								visited1B.add( newLocation.clone() );

								if (!piece.special){
									break;
								}
							}

						}else{
							//if we still on the board, and that location has been visited before, and we have not hit an obstacle keep going
							if (piece.special && newLocation[0] >= 0 && newLocation[1] >= 0
									&& newLocation[0] < dimension && newLocation[1] < dimension 
									&& visited1[newLocation[0]][newLocation[1]] != 0
									&& board1[newLocation[0]][newLocation[1]] != -1){
								newLocation[0] = newLocation[0] + move[0];
								newLocation[1] = newLocation[1] + move[1];
								continue;
							}
							break;
						}
						newLocation[0] = newLocation[0] + move[0];
						newLocation[1] = newLocation[1] + move[1];
					}	
				}
			}
			visited1A = visited1B;
			counter--;
		}
		counter = dimension - 1;
		while(counter > 0){
			//for all visited locations
			for(int i = 0; i < visited2A.size(); i++) {
				currentLoc = visited2A.get(i);
				//update all reachable locations with new move data
				iterator3 = piece.moves.iterator();
				while(iterator3.hasNext()){
					move = (int[]) iterator3.next();
					int newLocation[] = {currentLoc[0] + move[0], currentLoc[1] + move[1]}; 
					//if the move is to a location that is on the board and is not an obstacle and has not been visited before 
					while(true){
						if(newLocation[0] >= 0 && newLocation[1] >= 0 
								&& newLocation[0] < dimension && newLocation[1] < dimension
								&& board2[newLocation[0]][newLocation[1]] != -1
								&& visited2[newLocation[0]][newLocation[1]] == 0
								){
							//update the board if you never visited before or you have a lower value
							if (board2[newLocation[0]][newLocation[1]] == -2 
									|| (board2[currentLoc[0]][currentLoc[1]] + 1) < board2[newLocation[0]][newLocation[1]] ){
								board2[newLocation[0]][newLocation[1]] = board2[currentLoc[0]][currentLoc[1]] + 1;
								
								//add to visited
								visited2[newLocation[0]][newLocation[1]] = 1;
								visited2B.add( newLocation.clone() );
								if (!piece.special){
									break;
								}
							}
						}else{
							//if we still on the board, and that location has been visited before, and we have not hit an obstacle keep going
							if (piece.special && newLocation[0] >= 0 && newLocation[1] >= 0
									&& newLocation[0] < dimension && newLocation[1] < dimension 
									&& visited2[newLocation[0]][newLocation[1]] != 0
									&& board2[newLocation[0]][newLocation[1]] != -1){
								newLocation[0] = newLocation[0] + move[0];
								newLocation[1] = newLocation[1] + move[1];
								continue;
							}
							break;
						}
						newLocation[0] = newLocation[0] + move[0];
						newLocation[1] = newLocation[1] + move[1];
					}	
				}
			}
			visited2A = visited2B;
			counter--;
		}
	}
	@SuppressWarnings("unchecked")
	public void generatePaths(){
		//get the sp value at start
		//board1 has 0 at that location
		this.sp = board2[start[0]][start[1]];
			
		Queue lastVisited = new LinkedList();
		lastVisited.add(start);
		addPathSting2(start);
		
		int lastVisitedArray[][] = new int[dimension][dimension];
		lastVisitedArray[start[0]][start[1]] = 1;
		
		int totalVisitedArray[][] = new int[dimension][dimension];		
		
		Path potentialDeadEnd[][] = new Path[dimension][dimension];	
		
		int currentLoc[];
		int move[];
		Iterator iterator;	
			
		//loop through all locations lastVisited
		while(lastVisited.size() != 0){
			currentLoc = (int[]) lastVisited.poll();
			boolean moveFound = false;
			//add it to totalVisitedArray
		    totalVisitedArray[currentLoc[0]][currentLoc[1]] = 1;
			iterator = piece.moves.iterator();
			//loop through all moves
			while(iterator.hasNext()){
				move = (int[]) iterator.next();
				int newLocation[] = {currentLoc[0] + move[0], currentLoc[1] + move[1]}; 
				//if the move is to a location that is on the board and is not an obstacle and is not in totalVisitedArray
				//and not where you just came from
				if(newLocation[0] >= 0 && newLocation[1] >= 0 
						&& newLocation[0] < dimension && newLocation[1] < dimension
						&& board2[newLocation[0]][newLocation[1]] != -1
						&& totalVisitedArray[newLocation[0]][newLocation[1]] == 0
						&& !Arrays.equals(newLocation,currentLoc)){
				    //and the matrix sum is SP
				    if((board1[newLocation[0]][newLocation[1]] + board2[newLocation[0]][newLocation[1]]) == sp ){
						//add a path to it
						createPath(currentLoc, newLocation);
						addPathSting(currentLoc, newLocation);
						//add this new location to lastVisited
						lastVisited.add(newLocation);
						lastVisitedArray[newLocation[0]][newLocation[1]] = 1;
						moveFound = true;
				    }else{
				    	//if this move is on a straight line, then can take minus one of the sum.
				    	//this move is on a straight line if the next move in the same direction has the same or lower sp
				    	int nextLocation[] = {newLocation[0] + move[0], newLocation[1] + move[1]};  
				    	if(nextLocation[0] >= 0 && nextLocation[1] >= 0 
								&& nextLocation[0] < dimension && nextLocation[1] < dimension){ 
					    	int lastSp = (board1[newLocation[0]][newLocation[1]] + board2[newLocation[0]][newLocation[1]]);
					    	int nextSp = (board1[nextLocation[0]][nextLocation[1]] + board2[nextLocation[0]][nextLocation[1]]);
					    	if( lastSp == nextSp && (nextSp - 1) == sp ){
					    		ArrayList<Path> tempPaths = new ArrayList<Path>(); 
					    		//add a temp path to it
					    		tempPaths.add(createTempPath(currentLoc, newLocation));
					    		tempPaths.add(createTempPath(newLocation, nextLocation));
					    		nextLocation[0] = newLocation[0] + move[0];
					    		nextLocation[1] = newLocation[1] + move[1];  
					    		//keep creating temp paths to unvisited locations on the board
					    		while(nextLocation[0] >= 0 && nextLocation[1] >= 0 
					    				&& nextLocation[0] < dimension && nextLocation[1] < dimension
					    				&& board2[nextLocation[0]][nextLocation[1]] != -1
					    				&& totalVisitedArray[nextLocation[0]][nextLocation[1]] == 0){
						    		//add a temp path to it
						    		tempPaths.add(createTempPath(newLocation, nextLocation));
						    		newLocation = nextLocation.clone();
						    		//if you find a sp location
						    		nextSp = (board1[nextLocation[0]][nextLocation[1]] + board2[nextLocation[0]][nextLocation[1]]);
						    		if (nextSp == sp){
						    			//add it to visited
						    			lastVisited.add(nextLocation);
										//add all temp paths to paths
						    			paths.addAll(tempPaths);
						    			addPathSting(currentLoc, nextLocation);
						    			break;
						    		}
						    		nextLocation[0] = nextLocation[0] + move[0];
						    		nextLocation[1] = nextLocation[1] + move[1];  
					    		}

					    	}else if (nextSp == sp){
					    		//add a path to it
					    		createPath(currentLoc, newLocation);
					    		addPathSting(currentLoc, newLocation);
								//add this new location to lastVisited
								lastVisited.add(newLocation);
								lastVisitedArray[newLocation[0]][newLocation[1]] = 1;
					    		//add a path to it
								createPath(newLocation, nextLocation);
								addPathSting(newLocation, nextLocation);
								//add this new location to lastVisited
								lastVisited.add(nextLocation);
								lastVisitedArray[nextLocation[0]][nextLocation[1]] = 1;
								moveFound = true;
					    	}
				    	}
				    }
				}
			}
		}
		//for all the new moves that are optimal, repeat
	}
	
	public void addPathSting(int[] startLocation, int[] newLocation){
		ArrayList<Stack<String>> bigSeqsTemp = new ArrayList<Stack<String>>(); 
		//when creating a new path
		//find its start in a existing end of all bigPath in the list of bigPaths
		Iterator iterator = bigSeqs.iterator();
		String newLocationS = convertToSting(newLocation);
		String startLocationS = convertToSting(startLocation);
		while(iterator.hasNext()){
			Stack<String> stack = (Stack<String>) iterator.next();	
			if (startLocationS.equals(stack.peek())){
				//copy that particular bigPath
				Stack<String> tempStack = (Stack<String>) stack.clone();
				//add your new move to that copy
				tempStack.add(newLocationS);
				bigSeqsTemp.add(tempStack);
			}
		}

		bigSeqs.addAll(bigSeqsTemp);
		//add the copy to bigPaths
	}
	public void addPathSting2(int[] startLocation){
		Stack<String> tempStack = new Stack<String>();
		String startLocationS = convertToSting(startLocation);
		tempStack.add(startLocationS);
		bigSeqs.add(tempStack);
	}

	public String convertToSting(int[] newLocation){
		String string = "";
		if (newLocation[1] <= 25){
		   int i = 97 + newLocation[1];
		   string += new Character((char)i).toString();
		}else {
			   int i = 97 + newLocation[1];
			   string += Integer.toString(newLocation[1]);
		}
		int temp = (dimension - newLocation[0]);
		string += Integer.toString(temp);   
        return string;    
	}
	
	public ArrayList<GraphicText> getBoard(int boardNum){
		ArrayList<GraphicText> gBoard1 = new ArrayList<GraphicText>();
		ArrayList<GraphicText> gBoard2 = new ArrayList<GraphicText>();
		for(int y = 0; y < dimension; y++ ){
			for(int x = 0; x < dimension; x++){
				String one = Integer.toString(board1[y][x]);
				String two = Integer.toString(board2[y][x]);
				GraphicText temp1 = new GraphicText(one, y, x);
				GraphicText temp2 = new GraphicText(two, y, x);
				if(!one.equals("-2") && !one.equals("-1")){
					gBoard1.add(temp1);
				}
				if(!two.equals("-2") && !two.equals("-1")){
					gBoard2.add(temp2);
				}
			}
		}
		if(boardNum == 1){
			return gBoard1;
		}else{
			return gBoard2;
		}	
	}	
	private Path createPath(int start[], int end[]){
		Path path = new Path();
		//x,y
		path.moveTo(start[1] * this.cellWidth + this.cellWidth / 2, start[0] * this.cellWidth + this.cellWidth / 2);
		path.lineTo(end[1] * this.cellWidth + this.cellWidth / 2, end[0] * this.cellWidth + this.cellWidth / 2);
		path.close();
		paths.add(path);
		return path;
	}
	private Path createTempPath(int start[], int end[]){
		Path path = new Path();
		//x,y
		path.moveTo(start[1] * this.cellWidth + this.cellWidth / 2, start[0] * this.cellWidth + this.cellWidth / 2);
		path.lineTo(end[1] * this.cellWidth + this.cellWidth / 2, end[0] * this.cellWidth + this.cellWidth / 2);
		path.close();
		return path;
	}
}
