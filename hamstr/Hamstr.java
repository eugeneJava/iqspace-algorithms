import java.io.*;

import static java.lang.System.arraycopy;

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

            sortAsc(totalDaylyConsuming);

            int counter = 0;
            for (int hamsterDaylyConsuming : totalDaylyConsuming) {
                if (hamsterDaylyConsuming <= dailyFood && hamstersCount < numberOfHamstersInShop) {
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

    public static void sortAsc(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int j = i - 1;

            if (a[i] < a[j]) {
                int k = searchIndexBinaryAsc(a, j, i);
                int e = a[i];
                int length = i - k;
                arraycopy(a, k, a, k+1, length);
                a[k] = e;
            }
        }
    }

    private static int searchIndexBinaryAsc(int[] a, int sorted, int i) {
        int start = 0;
        int end = sorted;
        while (end >= start) {
            int mid = start + ((end - start) / 2);

            if (a[mid] < a[i]) {
                start = mid + 1;
            } else if(a[mid] > a[i]) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return start;
    }
}

