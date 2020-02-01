/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can 
    // negatively affect the outcome of the auto grading!

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    // HINT Write your own constants here to improve code readability ...

    public static String[] initialisePositions(int dimension) {
        // Distinguish the length of dimension
        // The main idea of this part is to
        // Divide this game into two parts
        // Top and Bottom are one part
        // Body is another part which contains game boards.
        if (dimension < 9 && dimension >= 4) {
            String[] matrix = new String[2+dimension+2];
            // Initialise Top and Bottom
            char[] rec = new char[dimension];
            for (int i = 0; i < dimension; i++) {
                rec [i] = (char)(65 + i);
            }
            // Build top and bottom
            matrix[0] = "  " + String.valueOf(rec) + "  ";
            matrix[1] = "";
            matrix[2+dimension+2-2] = matrix[1];
            matrix[2+dimension+2-1] = matrix[0];
            // Construct inner structure
            for (int i = 1; i <= dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    // Determine which locations should put Hounds
                    if (i == 1 && j%2 != 0) rec[j] = HOUND_FIELD;
                    // Determine which locations should put Fox
                    else if (i == dimension && j == Math.ceil(dimension/2)) rec[j] = FOX_FIELD;
                    else rec[j] = '.';
                }
                matrix[i+1] = i + " " + String.valueOf(rec) + " " + i;
            }
            return matrix;
        } else if (dimension > 9 && dimension <= 26) {
            String[] matrix = new String[2+dimension+2];
            // Initialise Top and Bottom
            char[] rec = new char[dimension];
            for (int i = 0; i < dimension; i++) {
                rec [i] = (char)(65 + i);
            }
            // Build top and bottom
            matrix[0] = "   " + String.valueOf(rec) + "   ";
            matrix[1] = "";
            matrix[2+dimension+2-2] = matrix[1];
            matrix[2+dimension+2-1] = matrix[0];
            // Construct inner structure
            for (int i = 1; i <= dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (i == 1 && j%2 != 0) rec[j] = HOUND_FIELD;
                    else if (i == dimension && j == Math.ceil(dimension/2)) rec[j] = FOX_FIELD;
                    else rec[j] = '.';
                }
                matrix[i+1] = String.format ("%02d",i)
                            + " " + String.valueOf(rec)
                            + " " + String.format ("%02d",i) ;
            }
            return matrix;
        } else {
            // If the number is out of bound
            // the code will use the default value
            // which is dimension = 8
            String[] matrix = new String[2+8+2];
            // Initialise Top and Bottom
            char[] rec = new char[8];
            for (int i = 0; i < 8; i++) {
                rec [i] = (char)(65 + i);
            }
            // Build top and bottom
            matrix[0] = "  " + String.valueOf(rec) + "  ";
            matrix[1] = "";
            matrix[10] = matrix[1];
            matrix[11] = matrix[0];
            // Construct inner structure
            for (int i = 1; i <= 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i == 1 && j%2 != 0) rec[j] = HOUND_FIELD;
                    else if (i == 8 && j == 4) rec[j] = FOX_FIELD;
                    else rec[j] = '.';
                }
                matrix[i+1] = i + " " + String.valueOf(rec) + " " + i;
            }
            // Return matrix to this method
            return matrix;
        }
    }
}
