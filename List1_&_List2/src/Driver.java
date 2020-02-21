import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Driver {
    private static byte[] data;
    private static String[] data8Bit;
    private static ArrayList<Pair> FrequencyInformationList;

    public static ArrayList<Pair> getFrequencyInformationList() {
        return FrequencyInformationList;
    }

    public static void setFrequencyInformationList(ArrayList<Pair> frequencyInformationList) {
        FrequencyInformationList = frequencyInformationList;
    }

    public static String[] getData8Bit() {
        return data8Bit;
    }

    public static void setData8Bit(String[] data8Bit) {
        Driver.data8Bit = data8Bit;
    }

    public static byte[] getData() {
        return data;
    }

    public static void setData(byte[] data) {
        Driver.data = data;
    }

    public static void main(String[] args) throws IOException {
        //loadFile(new File("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/kod.txt"));
        //loadFile(new File("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/kod.txt"));
        loadFile(new File("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/test3.bin"));
        //loadFile(new File("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt"));
        to8bit();
        checkDataFrequency();
        Collections.sort(getFrequencyInformationList());
        for (Pair p : getFrequencyInformationList()){
            System.out.println(p.toStringAsChar());
        }
    }

    public static void help(String text) {
        ArrayList<String> messageList = new ArrayList<>();
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        messageList.add(binary.toString());

        System.out.println(Arrays.toString(bytes));

        for (String object : messageList) {
            System.out.println("'" + text + "' to binary: " + object);
        }

        //  // this part below to help you to save 2-bit binary converted in int
        //  // arraylist to store string
        //  ArrayList<String> stringList = new ArrayList<String>();
        //  for (int i = 0; i < text.length(); i++) {
        //      stringList.add((messageList.get(0).split(" ")[i]));
        //  }
        //  // arraylist to store int converted
        //  ArrayList<Integer> intList = new ArrayList<Integer>();
        //  for (String str : stringList) {
        //      for (int i = 0; i < str.length(); i += 2) {
        //          intList.add(Integer.parseInt(str.substring(i, i + 2), 2));
        //          System.out.print(str.substring(i, i + 2) + " ");
        //      }
        //  }
        //  System.out.println();
        //  // nowretrieve int in arraylist to convert in 2-binary if you wont
        //  for (Integer integer : intList) {
        //      System.out.print(integer + "  ");
        //  }
    }

    private static void checkDataFrequency() {
        ArrayList<Pair> resultList = new ArrayList<>();
        String[] help = getData8Bit();
        Arrays.sort(help);
        int counter = 1;
        for (int i = 0; i < help.length; i++) {
            if (i + 1 == help.length) break;
            if (help[i].equals(help[i + 1])) {
                counter++;
            }
            if (!help[i].equals(help[i + 1])) {
                resultList.add(new Pair(help[i], counter));
                counter = 1;
            }

        }
        setFrequencyInformationList(resultList);
    }

    private static void to8bit() {
        String[] help = new String[getData().length];
        ArrayList<String> messageList = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        int counter = 0;
        for (byte b : getData()) {
            int val = b;

            for (int i = 0; i < 8; i++) {
                str.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            help[counter] = String.valueOf((str));
            counter++;
            str = new StringBuilder();
        }
        setData8Bit(help);
    }

    private static void loadFile(File file) throws IOException {

        FileInputStream fileStream = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];

        fileStream.read(arr, 0, arr.length);
        setData(arr);
    }

    private static void printAsChar() {
        for (int X : getData()) {
            System.out.print((char) X);
        }
    }
}

