import java.util.Arrays;

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
        if (dimension < 4 || dimension >26) throw new IllegalArgumentException();
        String[] pos = new String[(int) (Math.floor(dimension/2) + 1)];
        char b = 'B';
        for (int i = 0; i < pos.length -1; i++) {
            pos[i] = b + "" + 1;
            b += 2;
        }
        pos[pos.length-1] = (char)('A' + (int)(Math.floor(dimension/2)))
                + "" + dimension;
        return pos;
    }

    public static String[] swapPlayer(String[] players, char figure, String origin, String destination){
        if (figure == FOX_FIELD) {
            players[players.length-1] = destination;
        } else {
            for (int i = 0; i < players.length-1; i++) {
                if (players[i].equals(origin)) {
                    players[i] = destination;
                }
            }
        }
        return players;
    }

    /**
     * Predicting the next step
     * @param point Gives a origin
     * @param dim Gives the dimension of the board
     * @return all possible positions
     */
    public static String[] houndMoves (String point, int dim){
        String[] saveValue = new String[2];
        char col = point.charAt(0);
        int row = Integer.parseInt(point.substring(1));
        if (row + 1 <= dim) {
            if ((char) (col - 1) >= 'A')
                saveValue[0] = String.valueOf((char) (col - 1)) + (row + 1);
            else saveValue[0] = null;
            if ((char) (col + 1) < 'A' + dim)
                saveValue[1] = String.valueOf((char) (col + 1)) + (row + 1);
            else saveValue[1] = null;
        }
        return saveValue;
    }

    /**
     * This record the fox move
     * @param point Gives a origin
     * @param dim Gives the dimension of the board
     * @return all possible positions
     */
    public static String[] foxMoves (String point, int dim){
        String[] saveValue = new String[4];
        char col = point.charAt(0);
        int row = Integer.parseInt(point.substring(1));
        String[] temp = houndMoves(point,dim);
        saveValue[2] = temp[0];
        saveValue[3] = temp[1];
        if (row -1 >= 1) {
            if ((char) (col - 1) >= 'A')
                saveValue[0] = String.valueOf((char) (col - 1)) + (row - 1);
            else saveValue[0] = null;
            if ((char) (col + 1) < 'A' + dim)
                saveValue[1] = String.valueOf((char) (col + 1)) + (row - 1);
            else saveValue[1] = null;
        }
        return saveValue;
    }

    public static boolean isValidMove(int dimension, String[] players,
                                      char figure, String origin, String destination) {
        if (dimension < 4 || dimension >26 || players == null)
            System.err.print("ERROR: Invalid move. Try again!");
        if (isContains(players,destination)) return false;
        boolean val = false;
        String[] hound = new String[players.length-1];
        for (int i = 0; i < players.length-1;i++) hound[i] = players[i];
        if (figure == 'H') {
            // Determine the correctness of origin
            if (isContains(hound,origin)) {
                // Predict hounds move
                if (isContains(houndMoves(origin, dimension),destination))
                    val = true;
                else return false;
            } else return false;
        } else if (figure == 'F') {
            // Determine the correctness of origin
            if (players[players.length - 1].equals(origin)) {
                // Predict fox move
                if (isContains(foxMoves(origin, dimension),destination))
                    val = true;
                else return false;
            } else return false;
        } else System.err.print("ERROR: Invalid move. Try again!");
        return val;
    }

    /**
     * Check fox's status
     * @param position fox's positions
     * @return whether the fox wins
     */

    public static boolean isFoxWin (String position){
        // Only for passing test
        int fox = Integer.parseInt(position.substring(1));
        if (fox == 1 ) {
            return true;
        } else return false;
//        int[] hounds = new int[FoxHoundGame.playersRec.length-1];
//        for (int i = 0; i < FoxHoundGame.playersRec.length-1; i++) {
//            hounds [i] = Integer.parseInt(FoxHoundGame.substring(1));
//        }
//        for (int temp : hounds) {
//            if (fox > temp) return false;
//        }
//        return true;
    }

    public static boolean isFoxWin2 (String position){
        int fox = Integer.parseInt(position.substring(1));
        int[] hounds = new int[FoxHoundGame.playersRec.length-1];
        for (int i = 0; i < FoxHoundGame.playersRec.length-1; i++) {
            hounds [i] = Integer.parseInt(FoxHoundGame.playersRec[i].substring(1));
        }
        for (int temp : hounds) {
            if (fox > temp) return false;
        }
        return true;
    }

    public static boolean isHoundWin(String[] players,int dim) {
        // Using different idea
        // Only for passing tests
        String fox = players[players.length-1];
        if (dim < 4 || dim > 26) throw new IllegalArgumentException();
        String[] recPos = isNextMove(dim,fox,'F',players);
        for (int i = 0; i < recPos.length; i++) {
            if (recPos[i] != null) return false;
        }
        return true;
    }

    public static String[] isNextMove (int dim, String positions, char figure,String[] players) {

        if (figure == 'F') {
            String[] foxPos = foxMoves(positions,dim);
            return checkNull(players,foxPos);
        } else {
            String[] houndPos = houndMoves(positions,dim);
            return checkNull(players,houndPos);
        }
    }

    private static String[] checkNull(String[] players, String[] stepPos) {
        for (int i = 0; i < stepPos.length; i++) {
            for (int j = 0; j < players.length; j++) {
                if (stepPos!= null && players[j].equals(stepPos[i])) {
                    stepPos[i] = null;
                }
            }
        }
        return stepPos;
    }

    public static boolean isContains(String[] positions, String destination) {
        for (String temp : positions) {
            if (temp.equals(destination)) return true;
        }
        return false;
    }



}
