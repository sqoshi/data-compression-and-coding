package DataOperations;

import IndexesCoding.Chooser;
import IndexesCoding.Coding;
import IndexesCoding.MyBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Puller {
    FileInputStream fileInputStream;
    Coding type;

    public Puller(String name) throws FileNotFoundException {
        fileInputStream = new FileInputStream(new File("src/Data/code"));
        type = Chooser.choose(name);
    }

    public List<Integer> read() throws IOException {
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
        List<Integer> indexes = new ArrayList<>();
        int m = type.decode(stb.toString()) - 1;
        indexes.add(m);
        while (MyBuffer.getNextVal() != null) {
            m = type.decode(MyBuffer.getNextVal());
            indexes.add(m);
        }
        return indexes;

    }
}
