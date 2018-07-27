/**
 * This class define some maze property.
 *
 * @author BorisMirage
 * Time: 2018/07/26 20:51
 * Created with IntelliJ IDEA
 */

public class MazeCoord {

    final private int row;
    final private int col;

    /**
     * Pass para into class
     *
     * @param row row number (start from 0)
     * @param col column number (start from 0)
     */
    public MazeCoord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get current coord's row number.
     *
     * @return int row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Get current coord's column number.
     *
     * @return int column number
     */
    public int getCol() {
        return col;
    }

    /**
     * 
     * @param other
     * @return
     */
    public boolean equals(Object other) {

        if (other == null) {
            return false;
        }

        if (!(other instanceof MazeCoord)) {
            return false;
        }

        MazeCoord otherCoord = (MazeCoord) other;

        return this.row == otherCoord.row && this.col == otherCoord.col;
    }

    public String toString() {
        return "MazeCoord[row=" + row + ",col=" + col + "]";
    }

}
