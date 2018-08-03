import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author BorisMirage
 * Time: 2018/07/26 20:49
 * Created with IntelliJ IDEA
 */

public class Maze {

   // public static final boolean FREE = false;
    // public static final boolean WALL = true;

    private MazeCoord entry;
    private MazeCoord exit;
    private int[][] data;
    private LinkedList<MazeCoord> path = new LinkedList<>();
    private int[][] visitTimes;      // Record coord visited times
    private Stack<MazeCoord> pathStack = new Stack<>();

    /**
     * Necessary data that to construct a maze.
     *
     * @param mazeData 2D int array that store the info of maze (wall, space, distance).
     * @param startLoc MazeCoord start location
     * @param exitLoc  MazeCoord exit location
     */
    public Maze(int[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {
        entry = startLoc;
        exit = exitLoc;
        data = mazeData;                // int array that store walls and distance
        visitTimes = new int[data.length][data[0].length];
    }

    /**
     * Get number of rows in maze.
     *
     * @return number of rows in maze
     */
    public int numRows() {
        return data.length;
    }


    /**
     * Get number of columns in maze.
     *
     * @return number of columns in maze.
     */
    public int numCols() {
        return data[0].length;
    }

    /**
     * Check if input location has wall.
     *
     * @param loc input location
     * @return true if input location has wall, otherwise return false.
     */
    public boolean hasWallAt(MazeCoord loc) {
        return data[loc.getRow()][loc.getCol()] == -1;
    }

    /**
     * Get entry MazeCoord.
     *
     * @return entry MazeCoord
     */
    public MazeCoord getEntryLoc() {
        return entry;
    }

    /**
     * Get exit MazeCoord.
     *
     * @return exit MazeCoord
     */
    public MazeCoord getExitLoc() {
        return exit;
    }


    /**
     * Get path from entry to exit.
     *
     * @return path from entry to exit
     */
    public LinkedList<MazeCoord> getPath() {
        return path;
    }

    /**
     * Search path from entry to exit.
     * Since the min distance was found through exit to entry, path needs to be reversed in pathOutput.
     * This method can be accessed outside of Maze class.
     *
     * @return true if there exist a path from entry to exit, otherwise false
     */
    public boolean search() {
        return searchHelper(entry,data,0,visitTimes);
    }

    private boolean searchHelper(MazeCoord a, int[][]data, int direction, int[][] visitTimes){
        //check the input and output position whether has wall or out of bound
        if(a.getRow() > numRows() - 1 || a.getRow() < 0 || a.getCol() > numCols() - 1 || a.getCol() < 0 || hasWallAt(a)){
            return false;}
        if(exit.getRow() > numRows() - 1 || exit.getRow() < 0 || exit.getCol() > numCols() - 1 || exit.getCol() < 0 || hasWallAt(exit)){
            return false;}
        if(!a.equals(entry)&& visitTimes[a.getRow()][a.getCol()] > 3){
            return false;}
        if (a.equals(exit)){
            return true;
        }
        //move up if it not comes from up
        if(direction!=2){
            visitTimes[a.getRow()][a.getCol()]++;
            MazeCoord upLoc = move(a,1);
            if(searchHelper(upLoc,data,2,visitTimes)){
                data[upLoc.getRow()][upLoc.getCol()] = Math.min(data[a.getRow()][a.getCol()]+1,data[upLoc.getRow()][upLoc.getCol()]);
                return true;
            }
        }
        //move down if it not comes from down
        if(direction!=1){
            visitTimes[a.getRow()][a.getCol()]++;
            MazeCoord downLoc = move(a,2);
            if(searchHelper(downLoc,data,1,visitTimes)){
                data[downLoc.getRow()][downLoc.getCol()] = Math.min(data[a.getRow()][a.getCol()]+1,data[downLoc.getRow()][downLoc.getCol()]);
                return true;
            }
        }
        //move left if it is not from left
        if(direction!=4){
            visitTimes[a.getRow()][a.getCol()]++;
            MazeCoord leftLoc = move(a,3);
            if(searchHelper(leftLoc,data,4,visitTimes)){
                data[leftLoc.getRow()][leftLoc.getCol()] = Math.min(data[a.getRow()][a.getCol()]+1,data[leftLoc.getRow()][leftLoc.getCol()]);
                return true;
            }
        }
        //move right if it is not from right
        if(direction!=3){
            visitTimes[a.getRow()][a.getCol()]++;
            MazeCoord rightLoc = move(a,4);
            if(searchHelper(rightLoc,data,3,visitTimes)){
                data[rightLoc.getRow()][rightLoc.getCol()] = Math.min(data[a.getRow()][a.getCol()]+1,data[rightLoc.getRow()][rightLoc.getCol()]);
                return true;
            }
        }
        return false;
    }


    /**
     * Check if input position can be moved to.
     *
     * @param c           input coord
     * @param visitRecord 2D int array record visit times
     * @return -1 if out of bound or has wall, 0 if visited more than 4 times, or 1 if available.
     */
    private int checkCoord(MazeCoord c, int[][] visitRecord) {
        if (c.getRow() > numRows() - 1 || c.getRow() < 0 || c.getCol() > numCols() - 1 || c.getCol() < 0 || hasWallAt(c)) {
            return -1;
        } else if (visitRecord[c.getRow()][c.getCol()] > 3) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Move to input MazeCoord to selected direction.
     *
     * @param coord       input MazeCoord
     * @param orientation 1 - move upward
     *                    2 - move downward
     *                    3 - move left
     *                    4 - move right
     * @return MazeCoord that after movement
     */
    private MazeCoord move(MazeCoord coord, int orientation) {
        int newCol = coord.getCol();
        int newRow = coord.getRow();

        if (orientation == 1) {
            return new MazeCoord(newRow - 1, newCol);
        } else if (orientation == 2) {
            return new MazeCoord(newRow + 1, newCol);
        } else if (orientation == 3) {
            return new MazeCoord(newRow, newCol - 1);
        } else if (orientation == 4) {
            return new MazeCoord(newRow, newCol + 1);
        } else {
            System.out.println("Orientation Error!");
            return coord;
        }
    }

    /**
     * Print distance data for debug purpose.
     */
    private void printData() {
        for (int[] aData : data) {
            System.out.println(Arrays.toString(aData));
        }
    }
}