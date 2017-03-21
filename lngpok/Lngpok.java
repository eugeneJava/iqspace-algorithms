import java.io.*;

public class Lngpok {

    public static void main(String[] args) throws Exception {
        int[] cards = readCards();
        sort(cards, 1000000);

        int initialJokers = countJockers(cards);

        int longestSeq = 0;
        int currentSeq = 0;
        int availableJockers = initialJokers;

        for (int i = 0; i < cards.length -1; i++) {
            int gap = cards[i + 1] - cards[i];

            if (gap > 0) {
                if (gap == 1) {
                    currentSeq++;
                    longestSeq = chooseLongest(currentSeq, longestSeq);
                } else if(gap > 1) {
                    int requidesJokers = gap - 1;
                    if (availableJockers >= requidesJokers) {
                        currentSeq++;
                        availableJockers -= requidesJokers;
                        longestSeq = chooseLongest(currentSeq, longestSeq);
                    } else {
                        currentSeq = 1;
                        availableJockers = initialJokers;
                    }
                }
            }
        }

        saveResult(longestSeq + availableJockers);
    }

    private static int countJockers(int[] cards) {
        int initialJockers = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == 0) {
                initialJockers++;
            } else {
                break;
            }
        }
        return initialJockers;
    }

    private static int[] readCards() throws IOException {
        File data = new File("lngpok.in");
        String[] cardsStr;
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            cardsStr = br.readLine().split(" ");
        }

        return convertToNumbers(cardsStr);
    }

    private static void saveResult(int result) throws Exception {
        PrintWriter writer = new PrintWriter("lngpok.out", "UTF-8");
        writer.println(result);
        writer.close();
    }

    private static int[] convertToNumbers(String[] cardsStr) {
        int[] cards = new int[cardsStr.length];
        for (int i = 0; i < cardsStr.length; i++) {
            cards[i] = Integer.valueOf(cardsStr[i]);
        }
        return cards;
    }

    private static void sort(int[] a, int range) {
        int[] hist = new int[range + 1]; //assume from=0

        for (int i = 0; i < a.length; i++) {
            hist[a[i]]++;
        }

        int j = 0;
        for (int i = 0; i < hist.length; i++) {
            int cnt = hist[i];

            for (int k = 0; k < cnt; k++) {
                a[j++] = i;
            }
        }
    }

    private static int chooseLongest(int currentSeq, int longestSeq) {
       return currentSeq > longestSeq ? currentSeq : longestSeq;
    }
}
