




/******************************************************************************
 *  @(#)Maze.java
 *
 *  Generates an n-by-n maze.
 *
 *	Compilation:  javac Maze.java
 *  Execution:    java Maze.java n
 *  Dependencies: Std2Draw.java
 *
 *	@author Ali Mostafa Almousa
 *  @version 3.10 2022/2/22
 *
 ******************************************************************************/

/******************************************************************************
 *                  I M P O R T A N T
 *
 * Do not modify any thing after this line until instructed below
 *
 ******************************************************************************/
import java.beans.Visibility;
import java.util.Arrays;
import java.util.Random;


public class Maze {
    private int n;                  // dimension of maze
    private boolean[][] north;      // is there a wall to north of cell i, j
    private boolean[][] east;       // is there a wall to east of cell i, j
    private boolean[][] south;      // is there a wall to south of cell i, j
    private boolean[][] west;       // is there a wall to west of cell i, j
    private boolean[][] visited;    // flag a visited cell
    private boolean done = false;   // general flag
	private Random rand;            // random number generator

	// The maze constructor
	// generate a random maze and displays it
    public Maze(int n) {
        this.n = n;
    	rand = new Random();

		// Initialize sequence from system current time
    	rand.setSeed(System.currentTimeMillis());

        Std2Draw.setXscale(0, n+2);
        Std2Draw.setYscale(0, n+2);
        init();
        generate();
    }

    private void init() {
        // initialize border cells as already visited
        visited = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;
            visited[x][n+1] = true;
        }
        for (int y = 0; y < n+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
        }


        // initialze all walls as present
        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;
            }
        }
    }


    // generate the maze
    private void generate(int x, int y) {

        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor
            while (true) {
                double r = rand.nextInt(4);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;
                    generate(x, y+1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);
    }

    // draw the maze
    public void draw() {
        Std2Draw.setPenColor(Std2Draw.RED);
        Std2Draw.filledCircle(n/2.0+0.5, n/2.0+0.5, 0.375);
        Std2Draw.filledCircle(1.5, 1.5, 0.375);

        Std2Draw.setPenColor(Std2Draw.BLACK);
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) Std2Draw.line(x, y, x+1, y);
                if (north[x][y]) Std2Draw.line(x, y+1, x+1, y+1);
                if (west[x][y])  Std2Draw.line(x, y, x, y+1);
                if (east[x][y])  Std2Draw.line(x+1, y, x+1, y+1);
            }
        }
        Std2Draw.show();
        Std2Draw.pause(1000);
    }

	// draw a blue dot in the right visited cell
	private void markCell(int x, int y) {
        Std2Draw.setPenRadius(0);
        Std2Draw.setPenColor(Std2Draw.BLUE);
        Std2Draw.filledCircle(x+0.5, y+0.5, 0.2);
        Std2Draw.show();
        Std2Draw.pause(30);
	}

	// draw a magenta circle in the wrong visited cell
	private void unmarkCell(int x, int y) {
        Std2Draw.setPenRadius(0);
        Std2Draw.setPenColor(Std2Draw.WHITE);
        Std2Draw.filledCircle(x+0.5, y+0.5, 0.25);
        Std2Draw.setPenRadius(0.002);
        Std2Draw.setPenColor(Std2Draw.MAGENTA);
        Std2Draw.circle(x+0.5, y+0.5, 0.2);
        Std2Draw.show();
        Std2Draw.pause(10);
	}

    // a main method to test the program
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Maze maze = new Maze(n);
        Std2Draw.enableDoubleBuffering();
        maze.draw();
        maze.solve();
    }



    // solve the maze using a stack
    // start from the red dot at the lower left it has x=1, y=1
    // target is the red dot at the middle, it has x=n/2, y=n/2
    // mark the right cell using markCell method
    // mark the wrong cell using unmarkCell method
    // IMPORTANT: elements of visited data field is set to true
    // when generating the maze and it's used for solving too to save memory
    // thus it's the logic is flipped over:
    // visited[i][j] = true --> not visited
    // visited[i][j] = false --> visited
    public void solve() {
    	ArrayStack<int[]> stack = new ArrayStack<>();
    	int[] target = {n/2, n/2};
    	// Add the starting dot to the stack
    	stack.top(new int[] {1,1});
    	
    	while(!stack.isEmpty()) {
    		// Peek node X
    		int[] point = stack.glimpse();

    		// Get a random valid neighbor to node A
    		int[] validNeighbor = randomValidNeighbor(point[0], point[1]);
    		// ----->VALID NEIGHBOR FOUND<-----
    		if(validNeighbor.length != 0) {
    			// Valid neighbor is the target (DONE)
    			if (validNeighbor[0] == target[0] && validNeighbor[1] == target[1] ) {
    				Std2Draw.picture(1, n+0.9, "profile.png", 2.5, 2.5);		// Show a picture of the programmer
    				Std2Draw.setPenColor(Std2Draw.RED);							// Set pen color to red
    				Std2Draw.text(n-4, n-3, "Success!");						// Write success message
    				Std2Draw.text(1.1, n-0.2-0.4, "GitHub:");					// Write the GitHub of the programmer
    				Std2Draw.text(1.1, n-0.6-0.4, "aloooosh811");				
    				Std2Draw.show();
    				Std2Draw.pause(10);
    				done = true;
    				break;
    			}
    			// Valid neighbor is not the target
    			else {
    				stack.top(validNeighbor);								// Add neighbor to stack
    				visited[validNeighbor[0]][validNeighbor[1]] = false;	// Flag as visited 
    				markCell(validNeighbor[0], validNeighbor[1]);			// Mark as right cell
    			}
    		}
    		// ----->VALID NEIGHBOR NOT FOUND<-----
    		else {
    			unmarkCell(point[0], point[1]);		// Mark node A as wrong cell
    			stack.retrieve();					// Remove from stack
    		}
    		
    	}

    }
    
    /*
     * helper function that returns an
     * int[] array with two elements x and y
     * respectively which are the coordinates
     * of a random valid neighbor of a given
     * point. it returns an empty array if
     * no valid neighbor is found.
     * There is a room for optimization by
     * choosing the neighbor selectively rather
     * than randomly.
     */
    public int[] randomValidNeighbor(int x, int y) {
    	
    	if(!north[x][y] && visited[x][y+1]) return new int[] {x, y+1};			// North neighbor is not a wall and not visited
    	else if(!south[x][y] && visited[x][y-1]) return new int[] {x, y-1};		// South neighbor is not a wall and not visited
    	else if(!east[x][y] && visited[x + 1][y]) return new int[] {x + 1, y}; 	// East neighbor is not a wall and not visited
    	else if(!west[x][y] && visited[x - 1][y]) return new int[] {x - 1, y};	// West neighbor is not a wall and not visited
    	return new int[] {};													// No valid neighbor 
    }
}