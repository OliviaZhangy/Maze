import java.io.*;
import javax.swing.*;

/**
 * @author BorisMirage
 * Time: 2018/07/26 20:52
 * Created with IntelliJ IDEA
 */

public class MazeViewer {

    private static final char WALL_CHAR = '1';
    private static final char FREE_CHAR = '0';

    public static void main(String[] args) {

        String fileName = "";

        try {

            if (args.length < 1) {
                System.out.println("ERROR: missing file name command line argument");
            } else {
                fileName = args[0];
                JFrame frame = readMazeFile(fileName);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }

        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileName);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static MazeFrame readMazeFile(String fileName) throws IOException {

        /* Create a File object and using Reader to read it. */
        File readMazeFile = new File(fileName);

        /* Create a input reader to read data and save to buffer. */
        InputStreamReader newReader = new InputStreamReader(new FileInputStream(readMazeFile));
        BufferedReader newBufferRead = new BufferedReader(newReader);

        /* Read first line about new maze, convert to integer. */
        String firstLine = newBufferRead.readLine();
        String[] para = firstLine.split(" ");
        int mazeRow = Integer.parseInt(para[0]);
        int mazeColumn = Integer.parseInt(para[1]);

        int[][] readMazeData = new int[mazeRow][mazeColumn];

        /* Read following lines. */
        for (int i = 0; i < mazeRow; i++) {
            String line = newBufferRead.readLine();
            for (int j = 0; j < line.length(); j++) {
                char current = line.charAt(j);

                /* Free = false, Wall = true.
                 *  Free = 0, Wall = 1. */
                if (current == WALL_CHAR) {
                    readMazeData[i][j] = -1;
                } else if (current == FREE_CHAR) {
                    readMazeData[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        /* Read last two lines to obtain elements: entry and exit. */
        String startLine = newBufferRead.readLine();
        String exitLine = newBufferRead.readLine();

        int startRow = Integer.parseInt(startLine.split(" ")[0]);
        int startColumn = Integer.parseInt(startLine.split(" ")[1]);
        int exitRow = Integer.parseInt(exitLine.split(" ")[0]);
        int exitColumn = Integer.parseInt(exitLine.split(" ")[1]);
        MazeCoord newMazeStart = new MazeCoord(startRow, startColumn);
        MazeCoord newMazeExit = new MazeCoord(exitRow, exitColumn);

        return new MazeFrame(readMazeData, newMazeStart, newMazeExit);
    }

}