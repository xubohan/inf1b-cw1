import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/** 
 * The Main class of the fox hound program.
 * 
 * It contains the main game loop where main menu interactions
 * are processed and handler functions are called.
  */
public class FoxHoundGame {

    /** 
     * This scanner can be used by the program to read from
     * the standard input. 
     * 
     * Every scanner should be closed after its use, however, if you do
     * that for StdIn, it will close the underlying input stream as well
     * which makes it difficult to read from StdIn again later during the
     * program.
     * 
     * Therefore, it is advisable to create only one Scanner for StdIn 
     * over the course of a program and only close it when the program
     * exits. Additionally, it reduces complexity. 
     */
    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    public static int dimension1 = 0;
    public static String[] playersRec;
    public static String foxPos;
    public static char turnRec;
    /**
     * Swap between fox and hounds to determine the next
     * figure to move.
     * 
     * @param currentTurn last figure to be moved
     * @return next figure to be moved
     */
    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    /**
     * The main loop of the game. Interactions with the main
     * menu are interpreted and executed here.
     * 
     * @param dim the dimension of the game board
     * @param players current position of all figures on the board in board coordinates
     */
    private static void gameLoop(int dim, String[] players) {

        // start each game with the Fox
        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false;
        while(!exit) {
            turnRec = turn;
            playersRec = players;
            foxPos = playersRec[playersRec.length-1];
            System.out.println("\n#################################");
            FoxHoundUI.displayBoardFancy(players, dim);
            if (FoxHoundUtils.isFoxWin(foxPos) ||
                FoxHoundUtils.isFoxWin2(foxPos)) {
                System.err.println("The Fox wins!!!");
                return;
            }
            if (FoxHoundUtils.isHoundWin(players,dim)) {
                System.err.println("The Hounds win!!!");
                return;
            }

            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);
            
            // handle menu choice
            switch(choice) {
                case FoxHoundUI.MENU_MOVE:
                    String[] position = FoxHoundUI.positionQuery(dim,STDIN_SCAN);
                    boolean result = FoxHoundUtils.isValidMove(dim,players,turn,position[0],position[1]);
                    if (!result) {
                         System.err.println("Move is expected to be invalid if the given destination is already occupied.");
                        continue;
                    }
                    players = FoxHoundUtils.swapPlayer(players,turn,position[0],position[1]);
                    turn = swapPlayers(turn);
                    break;
                case FoxHoundUI.MENU_SAVE:
                    Path name = FoxHoundUI.fileQuery(STDIN_SCAN);
                    if (!FoxHoundIO.saveGame(players,turn,name)) {
                        System.err.println("ERROR: Saving file failed.");
                        continue;
                    }
                    break;
                case FoxHoundUI.MENU_LOAD:
                    Path name1 = FoxHoundUI.fileQuery(STDIN_SCAN);
                    char pointer = FoxHoundIO.loadGame(players,name1);
                    System.out.println(players.length);
                    if (pointer == '#') {
                        System.err.println("ERROR: Loading from file failed.");
                        continue;
                    }
                    break;
                case FoxHoundUI.MENU_EXIT:
                    exit = true;
                    break;
                default:
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }
        }
    }

    /**
     * Entry method for the Fox and Hound game. 
     * 
     * The dimensions of the game board can be passed in as
     * optional command line argument.
     * 
     * If no argument is passed, a default dimension of 
     * {@value FoxHoundUtils#DEFAULT_DIM} is used. 
     * 
     * Dimensions must be between {@value FoxHoundUtils#MIN_DIM} and 
     * {@value FoxHoundUtils#MAX_DIM}.
     * 
     * @param args contain the command line arguments where the first can be
     * board dimensions.
     */
    public static void main(String[] args) {
        int dimension = FoxHoundUtils.DEFAULT_DIM;
        dimension1 = dimension;
        String[] players = FoxHoundUtils.initialisePositions(dimension);
        gameLoop(dimension, players);

        // Close the scanner reading the standard input stream       
        STDIN_SCAN.close();
    }
}
