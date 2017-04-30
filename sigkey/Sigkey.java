import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Sigkey {
    private static Map<Character, Integer> letterIndexes = new HashMap<>();
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    static {
        for (byte index = 0; index < alphabet.length(); index++) {
            letterIndexes.put(alphabet.charAt(index), (index + 1));
        }
    }

    public static void main(String[] args) throws Exception {
        int numberOfKeys = 0;
        String[] keys;

        File data = new File("sigkey.in");
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            numberOfKeys = Integer.valueOf(br.readLine());
            keys = new String[numberOfKeys];


            int cnt = 0;
            for (String line; (line = br.readLine()) != null; ) {
                keys[cnt++] = line;
            }
        }

        int pairsCount = 0;
        for (int i = 0; i < keys.length - 1; i++) {
            String key1 = keys[i];
            if (key1 != null) {
                for (int j = i + 1; j < keys.length; j++) {
                    String key2 = keys[j];
                    if (key2 != null) {
                        if (isPair(key1, key2)) {
                            keys[j] = null;
                            pairsCount++;
                            break;
                        }
                    }
                }
            }
        }

        saveResult(pairsCount);
    }

    private static boolean isPair(String key1, String key2) {
        int totalLength = key1.length() + key2.length();
        int[] keyLetterIndexes = new int[totalLength];

        for (int i = 0; i < key1.length(); i++) {
            char letter = key1.charAt(i);
            int index = letterIndexes.get(letter);
            keyLetterIndexes[i] = index;
        }

        int key2Indexes = key1.length();
        for (int i = 0; i < key2.length(); i++) {
            char letter = key2.charAt(i);
            int index = letterIndexes.get(letter);
            keyLetterIndexes[key2Indexes + i] = index;
        }

        int[] histohramm = new int[alphabet.length() + 1];
        for (int i = 0; i < keyLetterIndexes.length; i++) {
            histohramm[keyLetterIndexes[i]] ++;
        }

        for (int i = 1; i < totalLength; i++) {
            int i1 = histohramm[i];
            if (i1 != 1) {
                return false;
            }
        }

       /* if repetitions allowed
        int max = max(keyLetterIndexes);
        for (int i = 1; i < max; i++) {
            int i1 = histohramm[i];
            if (i1 < 1) {
                return false;
            }
        }*/

        return true;
    }

    private static void saveResult(int result) throws Exception {
        PrintWriter writer = new PrintWriter("sigkey.out", "UTF-8");
        writer.println(result);
        writer.close();
    }

    private static int max(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }

        return max;
    }
}
