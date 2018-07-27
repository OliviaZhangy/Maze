/**
 * @author BorisMirage
 * Time: 2018/07/26 20:51
 * Created with IntelliJ IDEA
 */

public class MazeCoord {

    final private int row;
    final private int col;

    public MazeCoord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

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
