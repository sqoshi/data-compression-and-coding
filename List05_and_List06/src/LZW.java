import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class LZW {

    public static List<Integer> compress(FileInputStream inputStream) throws IOException {
        Pusher pusher = new Pusher();
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

    public static String decompress(List<Integer> compressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char) i);

        String w = "" + (char) (int) compressed.remove(0);
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

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }

        return result.toString();
    }


    public static <T, E> T getKeyByVal(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
