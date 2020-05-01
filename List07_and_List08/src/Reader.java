import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Reader {
    static int height;
    static int width;

    public static byte[] read(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        byte[] data = Files.readAllBytes(path);
        width = ((data[13] & 0xff) << 8) | (data[12] & 0xff);
        height = ((data[15] & 0xff) << 8) | (data[14] & 0xff);
        return Arrays.copyOfRange(data, 18, data.length - 26);

    }


    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
}
