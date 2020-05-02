import java.util.HashMap;
import java.util.Map;

public class Entropy {
    private static int chooseStartIndex(char color) throws IllegalAccessException {
        int start;
        if (color == 'B')
            start = 0;
        else if (color == 'G')
            start = 1;
        else if (color == 'R')
            start = 2;
        else
            throw new IllegalAccessException();
        return start;
    }

    public static double calculateFullEntropy(int[] bitMap) {
        int SymbolsQuantity = 1;
        Map<Integer, Integer> symbols = new HashMap<>();
        for (int c : bitMap) {
            SymbolsQuantity++;
            if (!symbols.containsKey(c)) {
                symbols.put(c, 1);
            } else {
                symbols.put(c, symbols.get(c) + 1);
            }
        }
        return computeEntropy(symbols, SymbolsQuantity);
    }

    public static double calculateComponentEntropy(char color, int[] bitMap) throws IllegalAccessException {
        int start = chooseStartIndex(color);
        int SymbolsQuantity = 1;
        Map<Integer, Integer> symbols = new HashMap<>();
        for (int i = start; i < bitMap.length; i += 3) {
            int c = (bitMap[i]);
            SymbolsQuantity++;

            if (!symbols.containsKey(c)) {
                symbols.put(c, 1);
            } else {
                symbols.put(c, symbols.get(c) + 1);
            }
        }
        return computeEntropy(symbols, SymbolsQuantity);
    }

    private static double computeEntropy(Map<Integer, Integer> symbols, int size) {
        double H = 0.0;
        double Pi;
        for (Map.Entry<Integer, Integer> entry : symbols.entrySet()) {
            Pi = (double) entry.getValue() / size;
            H += Pi * Math.log(1.0 / Pi) / Math.log(2.0);
        }
        return H;
    }
}
