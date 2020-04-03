import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class LZW {

    public static List<Integer> compress(byte[] data) {
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i);
        String w = "";
        List<Integer> result = new ArrayList<Integer>();

        for (byte b : data) {
            char c = (char) b;
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
        if (!w.equals(""))
            result.add(dictionary.get(w));
        return result;
    }


    public static <T, E> T getKeyByVal(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static byte[] read(File file) throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(file);
        byte[] fileInBytes = new byte[(int) file.length()];
        try {
            fileStream.read(fileInBytes, 0, fileInBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (fileInBytes);
    }

    public static String intToBinary(int a) {
        String temp = Integer.toBinaryString(a);
        while (temp.length() != 9) {
            temp = "0" + temp;
        }
        return temp;
    }
}
