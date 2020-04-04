import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws IOException {
        List<Integer> indexes = LZW.compress(new FileInputStream(new File("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt")));
        System.out.println((indexes));

    }

}