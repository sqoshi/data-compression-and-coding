import java.io.File;
import java.util.Map;

public class Driver {
    public static void main(String[] args) {
        Reader reader = new Reader(new File("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/test"));
        Map<String, Integer> dictionary = reader.generateBasicDictionary();
        Map<String, Integer> dictionary1 = reader.generateBasicDictionary();
        //dictionary.forEach((key, value) -> System.out.print("[" + key + " : " + value + "] "));
        System.out.println(dictionary);
        Encoder encoder = new Encoder(dictionary, reader.getBytesWithRepetitions());
        String code = encoder.encode(true);
        System.out.println(dictionary + "Taki ma byc");
        System.out.println(dictionary1 + "Tak zaczynam");
        Decoder decoder = new Decoder(dictionary1, code);
        decoder.decode(false);
    }
}
