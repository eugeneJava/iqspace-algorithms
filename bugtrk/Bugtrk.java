import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class Bugtrk {

    public static void main(String[] args) throws Exception {
        File data = new File("bugtrk.in");
        long amount;
        long width;
        long height;
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            String[] line = br.readLine().split(" ");
            amount = Long.valueOf(line[0]);
            width = Long.valueOf(line[1]);
            height = Long.valueOf(line[2]);
        }

        long rectSize = width > height ? width : height;

        for (int i = 0; i < Long.MAX_VALUE; i++) {
            long fitsWidthAmount = rectSize / width;
            long fitsHeightAmount = rectSize / height;

            long fitsRectAmount = fitsHeightAmount * fitsWidthAmount;
            if (fitsRectAmount >= amount) {
                break;
            } else {
                rectSize++;
            }
        }

        saveResult(rectSize);
    }

    private static void saveResult(long result) throws Exception {
        PrintWriter writer = new PrintWriter("bugtrk.out", "UTF-8");
        writer.println(result);
        writer.close();
    }
}
