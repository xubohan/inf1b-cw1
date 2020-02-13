import java.io.*;
import java.nio.file.Path;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */
public class FoxHoundIO {

    public static boolean saveGame (String[] players, char turn, Path name) {
        if (players.length != 5) throw new IllegalArgumentException();
        if (name == null) throw new NullPointerException();
        if (!new File(String.valueOf(name.getParent())).isDirectory())
            return false;
        if (players == null) throw new NullPointerException();
        File file = new File(String.valueOf(name));
        try {
            if (!file.createNewFile()) return false;
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String temp = turn + " ";
            for (int i = 0; i < players.length; i++) {
                temp = temp + players[i] + " ";
            }
            bw.write(temp.trim());
            bw.flush();
            bw.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static char loadGame(String[] players, Path input) {
        if (input == null) throw new NullPointerException();
        if (players.length !=5) throw new IllegalArgumentException();
        File file = new File(String.valueOf(input));
        String[] elem;
        if (!file.exists()) {
            return '#';
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int index = 0;
            String ind;
            String temp = null;
            while((ind = br.readLine())!= null) {
                temp = ind;
                index++;
            }
            if (index != 1) return '#';
            br.close();
            elem = temp.split(" ");
        } catch (IOException e) {
            return '#';
        }
        String hf = "HF";
        CharSequence cs = elem[0];
        if (!hf.contains(cs)) return '#';
        int maxNum = 1;
        char max = 'A';
        if (elem[0].length() != 1) return '#';
        for (int i = 1; i < elem.length; i++) {
            if (!Character.isUpperCase(elem[i].charAt(0))) return '#';
            String a = elem[i].substring(1);
            for (int j = 0; j < a.length(); j++) {
                if (!Character.isDigit(a.charAt(j))) return '#';
            }
            int aInt = Integer.parseInt(a);
            if (maxNum <= aInt) maxNum = aInt;
            if (max <= elem[i].charAt(0)) max = elem[i].charAt(0);
            if (max < 'A' || max > 'H' || maxNum > 8 || maxNum <1) return '#';
            players[i-1] = elem[i];
        }
        if (elem[0].charAt(0) == 'F' || elem[0].charAt(0) == 'H') {

            return elem[0].charAt(0);
        }
        return '#';
    }

}
