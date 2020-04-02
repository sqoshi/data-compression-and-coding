import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
        byte[] uncompressed = LZW.read(new File("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/test"));
        List<Integer> indexes = LZW.compress(uncompressed);


        //TODO: DELETE UNNECESSARY CODE // TO FILE!!
        List<String> elias_omega_indexes = new ArrayList<>();
        for (int i : indexes) {
            elias_omega_indexes.add(Elias.encode(i));
        }

        List<Byte> elias_omega_indexes_in_bytes = new ArrayList<>();
        for (String i : elias_omega_indexes) {
            System.out.println(i);
            byte[] elements = binStringToBytes(i);
            for (byte b : elements) {
                elias_omega_indexes_in_bytes.add(b);
            }
        }
        byte[] bytes_to_file = new byte[elias_omega_indexes_in_bytes.size()];
        for (int i = 0; i < elias_omega_indexes_in_bytes.size(); ++i) {
            bytes_to_file[i] = elias_omega_indexes_in_bytes.get(i);
        }
        writeBytesTofile("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code", bytes_to_file);


        //TODO: DELETE UNNECESSARY CODE // FROM FILE!!

        byte[] bytes_from_file = (getBytesFromFile("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code"));

        // for (byte b : get) {
        //   String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        // System.out.println(s1);
        //stb.append(s1);

        for (int i = 0; i < bytes_from_file.length; ++i) {

        }


        }

    static byte[] binStringToBytes(String s) {
        StringBuilder stb = new StringBuilder();
        stb.append(s);
        int zeros = 8 - s.length() % 8;
        while (zeros > 0) {
            zeros--;
            stb.append('0');
        }
        byte[] data = new byte[stb.toString().length() / 8];
        for (int i = 0; i < stb.toString().length(); i++) {
            char c = stb.toString().charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid char in binary string");
            }
        }
        return data;
    }

    public static void writeBytesTofile(String path, byte[] data) {
        File file = new File(path);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        }
    }

    public static byte[] getBytesFromFile(String path) {
        File file = new File(path);
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytesWithRepetitions = new byte[(int) file.length()];
        try {
            fileStream.read(bytesWithRepetitions, 0, bytesWithRepetitions.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesWithRepetitions;
    }
}