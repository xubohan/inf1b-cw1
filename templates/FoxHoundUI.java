import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Objects;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU =
        "\n1. Move\n2. Save Game\n3. Load Game\n4. Exit\n\nEnter 1 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu to save the game*/
    public static final int MENU_SAVE = 2;
    /**Menu to load the game*/
    public static final int MENU_LOAD = 3;
    /** Menu entry to terminate the program. */
    public static final int MENU_EXIT = 4;
    /** To record the board*/
    public static String[] matrix;

    public static void displayBoard(String[] players, int dimension) {
        classicBoard(players, dimension);
        for (String index: matrix) {
            System.out.println(index);
        }
    }
    public static void displayBoardFancy(String[] players, int dimension) {
        classicBoard(players, dimension);
        String[] sb1;
        String temp_1;
        String space;
        if(dimension > 9) space = "   ";
        else space = "  ";

        String str = "";
        for (int i = 0; i < dimension; i++) str += "|===";
        str += "|";
        System.out.println(matrix[0].replaceAll("([A-Z])","  $1 "));
        int saveValue = space.length();
        for (int i = 2; i <=1+dimension; i++) {
            System.out.println(space + str + space);
            sb1 = matrix[i].split(" ");
            temp_1 = sb1[1]
                    .replaceAll("[.]","|   ")
                    .replaceAll("F","| F ")
                    .replaceAll("H","| H ");
            temp_1 += "|";
            System.out.println(sb1[0]+ " " + temp_1 + " " + sb1[2]);
        }
        System.out.println(space + str + space);
        System.out.println(matrix[dimension+3].replaceAll("([A-Z])","  $1 "));

    }

    private static void classicBoard(String[] players, int dimension) {
        if (players == null) throw new NullPointerException();
        matrix = new String[2+dimension+2];
        char[] rec = new char[dimension];
        String fox = players[players.length-1];
        if (dimension >=4 && dimension <= 9) {
            // Initialise Top and Bottom
            for (int i = 0; i < dimension; i++) rec [i] = (char)(65 + i);
            // Build top and bottom
            matrix[0] = "  " + String.valueOf(rec) + "  ";
            matrix[1] = "";
            matrix[2+dimension+2-2] = matrix[1];
            matrix[2+dimension+2-1] = matrix[0];
            // Construct inner structure
            for (int i = 0; i < dimension; i++) rec[i] = '.';
            for (int i = 1; i <= dimension; i++) {
                matrix[i+1] = i + " " + String.valueOf(rec) + " " + i;
            }
        } else if (dimension > 9 && dimension <= 26) {
            // Initialise Top and Bottom
            for (int i = 0; i < dimension; i++) rec [i] = (char)(65 + i);
            // Build top and bottom
            matrix[0] = "   " + String.valueOf(rec) + "   ";
            matrix[1] = "";
            matrix[2+dimension+2-2] = matrix[1];
            matrix[2+dimension+2-1] = matrix[0];
            // Construct inner structure
            for (int i = 0; i < dimension; i++) rec[i] = '.';
            for (int i = 1; i <= dimension; i++) {
                matrix[i+1] = String.format ("%02d",i)
                        + " " + String.valueOf(rec)
                        + " " + String.format ("%02d",i) ;
            }
        }
        else {
            displayBoard(players,8);
        }

        for (int i = 0; i < players.length-1;i++) {
            int[] temp = rowCol(players[i], dimension);
            StringBuilder sb = new StringBuilder(matrix[temp[0]]);
            sb.replace(temp[1],temp[1]+1,"H");
            matrix[temp[0]] = sb.toString();
        }
        int[] temp = rowCol(fox,dimension);
        StringBuilder sb = new StringBuilder(matrix[temp[0]]);
        sb.replace(temp[1],temp[1]+1,"F");
        matrix[temp[0]] = sb.toString();
    }


    /**
     * Print the main menu and query the user for an entry selection.
     * 
     * @param figureToMove the figure type that has the next move
     * @param stdin a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD 
         && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = 
            figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    public static String[] positionQuery(int dim, Scanner stdin) {
        do {
            System.out.println("Provide origin and destination coordinates.");
            System.out.println("Enter two positions between A1-" + (char) ('A' + dim - 1) + dim + ":" + "\n");
            String[] temp = stdin.nextLine().split(" ");
            if (check(dim,temp) == false) {
                System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                continue;
            };
            char col = temp[0].charAt(0);
            int row = Integer.parseInt(temp[0].substring(1));
            char col1 = temp[1].charAt(0);
            int row1 = Integer.parseInt(temp[1].substring(1));

            if ((col >= 'A' && row >= 1 &&
                    row <= dim && col <= 'A' + dim &&
                    col1 >= 'A' && row1 >= 1 &&
                    row1 <= dim && col1 <= 'A' + dim))
                return temp;
        } while (true);
    }

    public static boolean check (int dim, String[] temp) {
        if (temp.length != 2) {
            return false;
        }
        String row0 = temp[0].substring(1);
        String row1 = temp[1].substring(1);
        if (!Character.isUpperCase(temp[0].charAt(0))) {
            return false;
        }
        for (int i = 0; i < row0.length() ; i++) {
            if (!Character.isDigit(row0.charAt(i)))
                return false;
        }
        if (!Character.isUpperCase(temp[1].charAt(0))) {
            return false;
        }
        for (int i = 0; i < row1.length(); i++) {
            if (!Character.isDigit(row1.charAt(i)))
                return false;
        }
        if (!(temp[0].charAt(0) >= 'A' &&
                Integer.parseInt(row0) >= 1 &&
                Integer.parseInt(row0) <= dim &&
                temp[0].charAt(0) <= 'A' + dim &&
                temp[1].charAt(0) >= 'A' &&
                Integer.parseInt(row1) >= 1 &&
                Integer.parseInt(row1) <= dim &&
                temp[1].charAt(0) <= 'A' + dim)) {
            return false;
        }
        return true;
    }

    public static int[] rowCol(String point, int dim) {
        int[] temp = new int[2];
        char col = point.charAt(0);
        int row = Integer.parseInt(point.substring(1));
        temp[0] = 1 + row;
        int colTemp = Integer.parseInt(String.valueOf(col - 'A'));
        if (dim >= 4 && dim <=9)
            temp[1] = 2 + colTemp;
        else if (dim > 9 && dim <= 26)
            temp[1] = 3 + colTemp;
        return temp;
    }

    public static Path fileQuery(Scanner stdin) {
        System.out.println("Enter file path:");
        String aName = stdin.nextLine();
        Path name = Paths.get(aName);
        return name;
    }
}







