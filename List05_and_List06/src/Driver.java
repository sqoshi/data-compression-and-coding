import DataOperations.Puller;
import DataOperations.Pusher;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws IOException {
        String filepath = "/home/piotr/Documents/data-compression-and-coding/Tests/test3.bin";
        String type = "Fibbo";
        if (args.length == 2) {
            filepath = args[1];
            type = args[0];
        }

        Pusher pusher = new Pusher(type);
        System.out.println("1");
        Puller puller = new Puller(type);
        System.out.println("1");
        List<Integer> lis = LZW.compress(pusher, filepath);
        System.out.println("1");
        List<Integer> pulled_indexes = puller.read();
        System.out.println(pulled_indexes);
        while (pulled_indexes.get(pulled_indexes.size() - 2) == 1)
            pulled_indexes.remove(pulled_indexes.size() - 1);
        LZW.decompress(pulled_indexes);
        getInformation(filepath);

    }

    public static void getInformation(String filepath) throws IOException {
        String encodedFilePath = "src/Data/code";
        String decodedFilePath = "src/Data/decode";
        double uncompressed = Entropy.calculate(filepath);
        double compressed = Entropy.calculate(encodedFilePath);
        int sizeUncompressed = (int) new File(filepath).length();
        int sizeCompressed = (int) new File(encodedFilePath).length();
        int sizeAfterDecompression = (int) new File(decodedFilePath).length();

        System.out.println("Entropy of uncompressed file: " + uncompressed);
        System.out.println("Entropy of compressed file: " + compressed);
        System.out.println("-------------------------------------------");
        System.out.println("Size of uncompressed file: " + sizeUncompressed);
        System.out.println("Size of compressed file: " + sizeCompressed);
        System.out.println("Size after decompression file: " + sizeAfterDecompression);
        System.out.println("-------------------------------------------");
        System.out.println("Compression rate = " + (double) sizeUncompressed / (double) sizeCompressed);


    }

}