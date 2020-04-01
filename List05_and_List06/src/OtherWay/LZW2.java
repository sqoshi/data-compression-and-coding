package OtherWay;

import java.io.IOException;
import java.util.*;

public class LZW2 {
    /**
     * Compress a string to a list of output symbols.
     */
    public static List<Integer> compress(String uncompressed) {
        // Build the dictionary.
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i);

        String w = "";
        List<Integer> result = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }
        System.out.println(dictionary);
        // SHIT.lzwj.compress.Output the code for w.
        byte[] filearr = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            String c = getKeyByVal(dictionary, result.get(i));
            System.out.println(c.getBytes() + " " + c);
        }
        System.out.println(Arrays.toString(filearr));
        if (!w.equals(""))
            result.add(dictionary.get(w));
        return result;
    }

    /**
     * Decompress a list of output ks to a string.
     */
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
        System.out.println(dictionary);

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        List<Integer> compressed = compress("abccd_abccd_acd_acd_acd_");
        System.out.println(compressed);
        String decompressed = decompress(compressed);
        System.out.println(decompressed);
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