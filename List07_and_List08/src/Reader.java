import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    static int height;
    static int width;

    public static int[] read(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        File file = new File(filepath);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(filepath));
        int intNo = 0;
        int c = input.read();
        c = Driver.control(c);
        byte[] data = Files.readAllBytes(path);
        List<Integer> integers = new ArrayList<>();
        while (intNo < file.length() - 26 || c == -1) {
            if (intNo > 17) {
                integers.add((c));
            }
            intNo++;
            c = input.read();
            c = Driver.control(c);

        }
        width = ((data[13] & 0xff) << 8) | (data[12] & 0xff);
        height = ((data[15] & 0xff) << 8) | (data[14] & 0xff);
        return convertIntegers(integers);

    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }


    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
}
