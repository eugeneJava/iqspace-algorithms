import java.io.*;
import java.util.Locale;
import java.util.Arrays;

import static java.lang.System.arraycopy;

public class Discnt {

    public static void main(String[] args) throws Exception {
        File data = new File("discnt.in");
        String[] pricesStr;
        double discountInPercents = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            pricesStr = br.readLine().split(" ");
            discountInPercents = Integer.valueOf(br.readLine());
        }

        double discount = discountInPercents / 100;
        double[] prices = covertToDouble(pricesStr);

        //sort(prices);
	Arrays.sort(prices);	

        double totalPrice = calculateTotalPrice(prices, discount);

        saveTotalPrice(totalPrice);
    }

    private static void saveTotalPrice(double totalPrice) throws Exception {
        PrintWriter writer = new PrintWriter("discnt.out", "UTF-8");
        writer.println(String.format(Locale.ENGLISH,"%.2f",totalPrice));
        writer.close();
    }

    private static double calculateTotalPrice(double[] prices, double discount) {
        int discountsNumber = prices.length / 3;

        double totalPrice = 0;
        for (int i = 0; i < discountsNumber; i++) {
            double saved = prices[i] * discount;
            totalPrice = totalPrice + (prices[i] - saved);
        }

        for (int i = discountsNumber; i < prices.length; i++) {
            totalPrice += prices[i];
        }

        return totalPrice;
    }


    private static double[] covertToDouble(String[] pricesStr) {
        double[] prices = new double[pricesStr.length];
        for (int i = 0; i < pricesStr.length; i++) {
            prices[i] = Double.valueOf(pricesStr[i]);
        }
        return prices;
    }

    public static void sort(double[] prices) {
        for (int i = 1; i < prices.length; i++) {
            int j = i - 1;

            if (prices[i] > prices[j]) {
                int k = searchIndexBinary(prices, j, i);
                double price = prices[i];

                int length = i - k;
                arraycopy(prices, k, prices, k + 1, length);
                prices[k] = price;
            }
        }
    }

    private static int searchIndexBinary(double[] a, int sorted, int i) {
        int start = 0;
        int end = sorted;
        while (end >= start) {
            int mid = start + ((end - start) / 2);

            if (a[mid] > a[i]) {
                start = mid + 1;
            } else if (a[mid] < a[i]) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return start;
    }
}
