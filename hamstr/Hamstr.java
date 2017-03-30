import java.io.*;

public class Hamstr {
    public static void main(String[] args) throws Exception {
        int dailyFood = 0;
        int numberOfHamstersInShop = 0;
        int dailyConsumings[][];


        File data = new File("hamstr.in");
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            dailyFood = Integer.valueOf(br.readLine());
            numberOfHamstersInShop = Integer.valueOf(br.readLine());
            dailyConsumings = new int[numberOfHamstersInShop][2];

            int cnt = 0;
            for (String line; (line = br.readLine()) != null; ) {
                String[] split = line.split(" ");
                dailyConsumings[cnt][0] = Integer.valueOf(split[0]);
                dailyConsumings[cnt][1] = Integer.valueOf(split[1]);

                cnt++;
            }
        }


        int hamstersCount = 0;
        if (numberOfHamstersInShop == 1) {
            for (int con[] : dailyConsumings) {
                if (con[0] <= dailyFood) {
                    hamstersCount  = 1;
                    break;
                }
            }
        } else {
            //change to heap!!!!!
            int totalDaylyConsuming[] = new int[numberOfHamstersInShop];

            for (int i = 0; i < numberOfHamstersInShop; i++) {
                int con[] = dailyConsumings[i];
                totalDaylyConsuming[i] = con[0] + con[1];
            }

            sort(totalDaylyConsuming, 10000000);

            int counter = 0;
            for (int hamsterDaylyConsuming : totalDaylyConsuming) {
                if (hamsterDaylyConsuming <= dailyFood) {
                    hamstersCount ++;
                    dailyFood = dailyFood - hamsterDaylyConsuming;
                    counter++;
                } else {
                    if (isFirstHamster(counter)) {
                        for (int con[] : dailyConsumings) {
                            if (con[0] <= dailyFood) {
                                hamstersCount  = 1;
                                break;
                            }
                        }
                    } else {
                        break;
                    }

                }
            }
        }

        saveResult(hamstersCount);
    }

    private static boolean isFirstHamster(int counter) {
        return counter == 0;
    }

    private static void saveResult(int result) throws Exception {
        PrintWriter writer = new PrintWriter("hamstr.out", "UTF-8");
        writer.println(result);
        writer.close();
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
}
