import DataOperations.Pusher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {

    public static List<Integer> compress(Pusher pusher, String filepath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filepath));
        int dictSize = 257;
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i + 1);
        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        int c = inputStream.read();
        while (c != -1) {
            String wc = w + (char) c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                pusher.push(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + (char) c;
            }
            c = inputStream.read();
        }
        if (!w.equals("")) {
            result.add(dictionary.get(w));
            pusher.push(dictionary.get(w));
        }
        pusher.checkLastByte();
        return (result);
    }


    public static String decompress(List<Integer> compressed) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("src/Data/decode"));
        int dictSize = 257;
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i + 1, "" + (char) i);

        String w = "" + (char) (int) compressed.remove(0);
        for (char c : w.toCharArray()) {
            fos.write(c);
        }
        StringBuilder result = new StringBuilder(w);
        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);
            for (char c : entry.toCharArray()) {
                fos.write(c);
            }

            dictionary.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }

        return result.toString();
    }


}
