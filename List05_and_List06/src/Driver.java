import DataOperations.Puller;
import DataOperations.Pusher;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws IOException {
        String filepath = "/home/piotr/Documents/data-compression-and-coding/" +
                "List05_and_List06/src/Data/" +
                //"test";
                //"pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt";
                "z14.jpg";
        String type = "Elias Omega";

        Pusher pusher = new Pusher(type);
        Puller puller = new Puller(type);

        List<Integer> lis = LZW.compress(pusher, filepath);
        System.out.println(lis);
        List<Integer> pulled_indexes = puller.read();
        System.out.println(pulled_indexes);
        while (pulled_indexes.get(pulled_indexes.size() - 1) == 1)
            pulled_indexes.remove(pulled_indexes.size() - 1);
        LZW.decompress(pulled_indexes);
        getInformations(filepath);

    }

    public static void getInformations(String filepath) throws IOException {
        String decodedFilePath = "/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code";
        double uncompressed = Entropy.calculate(filepath);
        double compressed = Entropy.calculate(decodedFilePath);
        int sizeUncompressed = (int) new File(filepath).length();
        int sizeCompressed = (int) new File(decodedFilePath).length();

        System.out.println("Entropy of uncompressed file: " + uncompressed);
        System.out.println("Entropy of compressed file: " + compressed);
        System.out.println("-------------------------------------------");
        System.out.println("Size of uncompressed file: " + sizeUncompressed);
        System.out.println("Size of compressed file: " + sizeCompressed);
        System.out.println("-------------------------------------------");
        System.out.println("Compression rate = " + (double) sizeUncompressed / (double) sizeCompressed);


    }

}