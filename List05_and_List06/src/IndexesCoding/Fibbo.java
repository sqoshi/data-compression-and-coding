package IndexesCoding;

import java.util.ArrayList;
import java.util.List;

public class Fibbo implements Coding {

    private static long getFibByIndex(long n) {
        long a = 0, b = 1, c = 0;
        while (n > 0) {
            n--;
            c = a + b;
            a = b;
            b = c;
        }
        return a;
    }

    private static long getFibonacci(long n) {
        long a = 0, b = 1, c = 0;

        while (c <= n) {
            c = a + b;
            a = b;
            b = c;
        }
        return a;
    }

    private static long getFibIndex(long n) {
        long a = 0, b = 1, c = 0;
        int index = 0;
        while (c <= n) {
            c = a + b;
            a = b;
            b = c;
            index++;
        }
        return index;
    }

    static boolean isPerfectSquare(int x) {
        int s = (int) Math.sqrt(x);
        return (s * s == x);
    }

    static boolean isFibonacci(int n) {
        return isPerfectSquare(5 * n * n + 4) ||
                isPerfectSquare(5 * n * n - 4);
    }

    @Override
    public int decode(String value) {
        int index = 0;
        for (int i = 0; i < value.length(); ++i) {
            if (value.charAt(i) == '1' &&
                    value.charAt(i + 1) == '1') {
                index = i + 1;
                break;
            }
        }
        String sample = (value.substring(0, index));
        int number = 0;
        for (int i = 0; i < sample.length(); i++) {
            if (sample.charAt(i) == '1') {
                number += getFibByIndex(i + 2);
            }
        }
        if (value.length() > (index + 1)) {
            int counter = 0;
            if (value.substring(index + 1).length() <= 8)
                for (int i = 0; i < value.substring(index + 1).length(); i++) {
                    if (value.substring(index + 1).charAt(i) == '0') {
                        counter++;
                    }
                }
            if (counter == value.substring(index + 1).length()) {
                MyBuffer.nextVal = null;
            } else
                MyBuffer.nextVal = value.substring(index + 1);
        } else
            MyBuffer.nextVal = null;
        return number;
    }

    @Override
    public String encode(int N) {
        List<Integer> onesPositions = new ArrayList<>();
        int fib = (int) getFibonacci(N);
        int remainder = N - fib;
        onesPositions.add((int) getFibIndex(fib));
        onesPositions.add((int) getFibIndex(remainder));
        while (remainder != 0) {
            N = remainder;
            fib = (int) getFibonacci(N);
            remainder = N - fib;
            onesPositions.add((int) getFibIndex(remainder));
        }
        onesPositions.remove(onesPositions.size() - 1);
        StringBuilder stb = new StringBuilder();
        stb.append("0".repeat(Math.max(0, onesPositions.get(0))));
        for (Integer i : onesPositions) {
            stb.setCharAt(i - 1, '1');
        }
        stb.append("1");
        return stb.substring(1);
    }
}
