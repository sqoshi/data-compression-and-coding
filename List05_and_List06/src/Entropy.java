import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Entropy {

    static double calculate(String filepath) throws IOException {
        int SymbolsQuantity = 1;
        Map<Integer, Integer> symbols = new HashMap<>();
        FileInputStream fs = new FileInputStream(new File(filepath));
        int c = fs.read();
        symbols.put(c, 1);
        while (c != -1) {
            SymbolsQuantity++;
            if (!symbols.containsKey(c)) {
                symbols.put(c, 1);
            } else {
                symbols.put(c, symbols.get(c) + 1);
            }
            c = fs.read();
        }
        return computeEntropy(symbols, SymbolsQuantity);
    }

    static double computeEntropy(Map<Integer, Integer> symbols, int size) {
        double H = 0.0;
        double Pi = 0.0;
        for (Map.Entry<Integer, Integer> entry : symbols.entrySet()) {
            Pi = (double) entry.getValue() / size;
            H += Pi * Math.log(1 / Pi);
        }
        return H;
    }


}
