import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author BorisMirage
 * Time: 2018/07/26 20:49
 * Created with IntelliJ IDEA
 */

public class Maze {

    public static final boolean FREE = false;
    public static final boolean WALL = true;

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
     * Get represented value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @return represented value store in 2D int array mazeData
     */
    private int getData(MazeCoord coord) {
        return data[coord.getRow()][coord.getCol()];
    }

    /**
     * Get value store in mazeData based on input MazeCoord.
     *
     * @param coord input MazeCoord
     * @param value value to be set
     */
    private void setData(MazeCoord coord, int value) {
        data[coord.getRow()][coord.getCol()] = value;
    }

    /**
     * Add one to represented coord in visitTimes.
     *
     * @param coord input coord
     */
    private void addVisit(MazeCoord coord) {
        visitTimes[coord.getRow()][coord.getCol()] += 1;
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

        if (hasWallAt(entry) || hasWallAt(exit)) {
            return false;
        }

        /* One-element maze */
        if (entry.equals(exit)) {
            path.add(exit);
            return true;
        }

        /* Set initial data into mazeData */
        setData(entry, 1);

        /* Fill each reachable MazeCoord in maze and put min distance into it */
        tryPath(entry);

        /* Check if exit is visited hence assure if there is a path or not */
        if (getData(exit) != Integer.MAX_VALUE - 1) {
            traceBackPath(exit);
            printData();
            path = new LinkedList<>(pathOutput(pathStack));
            return true;
        }
        return false;
    }

    /**
     * Pop each element in stack into LinkedList path and return it as final path output.
     *
     * @param s stack that store entire path
     * @return path LinkedList
     */
    private LinkedList<MazeCoord> pathOutput(Stack<MazeCoord> s) {
        LinkedList<MazeCoord> inOrderPath = new LinkedList<>();

        /* Make path in order */
        while (s.size() != 0) {
            inOrderPath.add(s.pop());
        }
        return inOrderPath;
    }

    /**
     * Trace min distance recursively from exit to entry, which is in inverse order.
     * Hence, use FIFO stack to store when adding.
     * Pop each MazeCoord in pathOutput to reverse path in order.
     *
     * @param pos current MazeCoord
     */
    private void traceBackPath(MazeCoord pos) {

        /* Store current position */
        pathStack.push(pos);

        /* If current position is not entry (path finish point), continue process */
        if (!pos.equals(entry)) {
            traceBackPath(findMinNext(pos));
        }
    }

    /**
     * Find min distance in four possible direction based on input MazeCoord.
     * Have to check the availability of each direction first to avoid wall or out of boundary.
     *
     * @param coord input MazeCoord
     * @return next MazeCoord that has min distance to entry
     */
    private MazeCoord findMinNext(MazeCoord coord) {
        int min = getData(coord);
        MazeCoord next = coord;
        for (int i = 1; i < 5; i++) {
            if (checkCoord(move(coord, i), visitTimes) > -1 && getData(move(coord, i)) < min) {
                min = getData(move(coord, i));
                next = move(coord, i);
            }
        }
        return next;
    }

    /**
     * Greedy method to fill the mazeData array with min distance to entry.
     *
     * @param coord input MazeCoord
     */
    private void greedy(MazeCoord coord) {
        for (int i = 1; i < 5; i++) {
            if (checkCoord(move(coord, i), visitTimes) > -1) {
                setMin(coord, move(coord, i));
            }
        }
    }

    /**
     * Fill mazeData recursively with min distance from entry to exit.
     *
     * @param current current MazeCoord
     */
    private void tryPath(MazeCoord current) {
        greedy(current);
        MazeCoord next;
        for (int i = 1; i < 5; i++) {
            if (checkCoord(move(current, i), visitTimes) > -1) {
                setMin(move(current, i), current);
            }
        }
        if (checkCoord(move(current, 1), visitTimes) > 0) {
            next = move(current, 1);
            addVisit(next);
            setMin(current, next);
            System.out.println("Current: " + current.toString() + " " + getData(current) + " Next: " + next.toString() + " " + getData(next));
            tryPath(next);
        }
        if (checkCoord(move(current, 2), visitTimes) > 0) {
            next = move(current, 2);
            addVisit(next);
            setMin(current, next);
            System.out.println("Current: " + current.toString() + " " + getData(current) + " Next: " + next.toString() + " " + getData(next));
            tryPath(next);
        }
        if (checkCoord(move(current, 3), visitTimes) > 0) {
            next = move(current, 3);
            addVisit(next);
            setMin(current, next);
            System.out.println("Current: " + current.toString() + " " + getData(current) + " Next: " + next.toString() + " " + getData(next));
            tryPath(next);
        }
        if (checkCoord(move(current, 4), visitTimes) > 0) {
            next = move(current, 4);
            addVisit(next);
            setMin(current, next);
            System.out.println("Current: " + current.toString() + " " + getData(current) + " Next: " + next.toString() + " " + getData(next));
            tryPath(next);
        }
    }

    /**
     * Set min distance in two continues coord.
     *
     * @param cur  coord 1
     * @param next coord 2
     */
    private void setMin(MazeCoord cur, MazeCoord next) {
        int curData = getData(cur);
        int nextData = getData(next);
        if (curData + 1 < nextData) {
            setData(next, curData + 1);
        }
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
        } else if (visitRecord[c.getRow()][c.getCol()] > 4) {
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