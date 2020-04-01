package todelete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Reader {
    private File file;
    private byte[] bytesWithRepetitions;

    public Reader(File file) {
        this.file = file;
    }

    public byte[] getBytesWithRepetitions() {
        return bytesWithRepetitions;
    }

    public void setBytesWithRepetitions(byte[] bytesWithRepetitions) {
        this.bytesWithRepetitions = bytesWithRepetitions;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Map<String, String> generateBasicDictionary() {
        Map<String, String> dictionary = new HashMap<>();
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bytesWithRepetitions = new byte[(int) getFile().length()];
        try {
            fileStream.read(bytesWithRepetitions, 0, bytesWithRepetitions.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(bytesWithRepetitions));
        int i = 0;
        int index = 0;
        while (i < bytesWithRepetitions.length) {
            if (!dictionary.containsKey(String.valueOf((char) bytesWithRepetitions[i]))) {
                index++;
                dictionary.put(String.valueOf((char) bytesWithRepetitions[i]), String.valueOf(index));
            }
            i++;
        }
        return dictionary;
    }

}
