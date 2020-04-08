package DataOperations;

import IndexesCoding.Elias;
import IndexesCoding.EliasOmega;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Puller {
    FileInputStream fileInputStream;

    public Puller() throws FileNotFoundException {
        fileInputStream = new FileInputStream(new File("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code"));
    }

    public List<Integer> read() throws IOException {
        Elias type = new EliasOmega();
        StringBuilder stb = new StringBuilder();
        int c = fileInputStream.read();
        while (c != -1) {
            StringBuilder zerosInserter = new StringBuilder();
            String code = Integer.toBinaryString(c);
            zerosInserter.append(code);
            if (code.length() != 8) {
                int zerosToAppend = 8 - code.length();
                while (zerosToAppend > 0) {
                    zerosToAppend--;
                    zerosInserter.insert(0, "0");
                }
            }
            stb.append(zerosInserter.toString());
            c = fileInputStream.read();
        }
        System.out.println("fnished reading");
        List<Integer> indexes = new ArrayList<>();
        int m = type.decode(stb.toString()) - 1;
        indexes.add(m);
        System.out.println(indexes);
        System.out.println("decodedElias");
        System.out.println(stb.toString().length());
        while (EliasOmega.getNextVal() != null) {
            System.out.println(EliasOmega.getNextVal().length());
            m = type.decode(EliasOmega.getNextVal());
            indexes.add(m);
        }
        return indexes;

    }
}
