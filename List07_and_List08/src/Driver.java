import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Driver {
    static int[][][] board;

    public static int[] getN(int r, int c) {
        if (r - 1 >= 0)
            return getPixel(r - 1, c);
        else
            return new int[]{0, 0, 0};
    }

    public static int[] getW(int r, int c) {
        if (c - 1 >= 0)
            return getPixel(r, c - 1);
        else
            return new int[]{0, 0, 0};
    }

    public static int[] getNW(int r, int c) {
        if (r - 1 >= 0 && c - 1 >= 0)
            return getPixel(r - 1, c - 1);
        else
            return new int[]{0, 0, 0};
    }

    public static int[][][] getBoard() {
        return board;
    }

    public static void displayResults(int c, int[] b) throws IllegalAccessException {
        if (c != 0)
            System.out.printf("%-10s %-20s %-20s %-20s %-20s %-1s\n", "| Scheme ".concat(String.valueOf(c)), Entropy.calculateFullEntropy(b),
                    Entropy.calculateComponentEntropy('B', b), Entropy.calculateComponentEntropy('G', b), Entropy.calculateComponentEntropy('R', b), "|");
        else
            System.out.printf("%-10s %-20s %-20s %-20s %-20s %-1s\n", "| File", Entropy.calculateFullEntropy(b),
                    Entropy.calculateComponentEntropy('B', b), Entropy.calculateComponentEntropy('G', b), Entropy.calculateComponentEntropy('R', b), "|");
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        printSchemes();
        for (String filename : args) {
            System.out.println(filename);
            int[] data = Reader.read(filename);
            List<Double> imageResultListFull = new ArrayList<>(), imageResultListBlue = new ArrayList<>(), imageResultListGreen = new ArrayList<>(), imageResultListRed = new ArrayList<>();
            int h = Reader.getHeight(), w = Reader.getWidth();
            createBoard(data, h, w);
            displayResults(0, data);
            System.out.println("------------------------------------------------------------------------------------------------");
            for (int i = 1; i < 9; i++) {
                int[][][] diff_board = build(i);
                int[] diffArray = convert3DtoArray(diff_board);
                displayResults(i, diffArray);
                imageResultListFull.add(Entropy.calculateFullEntropy(diffArray));
                imageResultListBlue.add(Entropy.calculateComponentEntropy('B', diffArray));
                imageResultListGreen.add(Entropy.calculateComponentEntropy('G', diffArray));
                imageResultListRed.add(Entropy.calculateComponentEntropy('R', diffArray));
            }
            System.out.println("------------------------------------------------------------------------------------------------");

            printSummary(imageResultListFull, imageResultListBlue, imageResultListGreen, imageResultListRed);
        }

    }

    public static void printSummary(List<Double> imageResultListFull, List<Double> imageResultListBlue,
                                    List<Double> imageResultListGreen, List<Double> imageResultListRed) {
        System.out.println("-----------------------------------------Summary------------------------------------------------");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s %-1s\n", "| Best", "Full", "Blue", "Green", "Red", "|");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s %-1s\n", "| Minimal", Collections.min(imageResultListFull), Collections.min(imageResultListBlue)
                , Collections.min(imageResultListGreen), Collections.min(imageResultListRed), "|");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s %-1s\n", "| Index", 1 + imageResultListFull.indexOf(Collections.min(imageResultListFull)), 1 + imageResultListBlue.indexOf(Collections.min(imageResultListBlue)),
                1 + imageResultListGreen.indexOf(Collections.min(imageResultListGreen)), 1 + imageResultListRed.indexOf(Collections.min(imageResultListRed)), "|");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    public static int[][][] build(int scheme) {
        int h = board.length, w = board[0].length, d = board[0][0].length;
        int[][][] schema = new int[h][w][d];

        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                int[] subs = decide(scheme, row, column);
                for (int i = 0; i < d; i++) {
                    schema[row][column][i] =
                            control(board[row][column][i] - subs[i]);
                }

            }
        }
        return schema;
    }

    public static void printBoard(int[][][] b) {
        int w = b[0].length;
        for (int[][] bytes : b) {
            for (int column = 0; column < w; column++) {
                System.out.print(Arrays.toString(bytes[column]) + ", ");
            }
            System.out.println();
        }
    }

    public static void createBoard(int[] bytes, int h, int w) {
        int[][][] b = new int[h][w][3];
        int counter = 0;
        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                int i = 0;
                while (i < 3) {
                    b[row][column][i] = bytes[counter];
                    counter++;
                    i++;
                }
            }
        }
        Driver.board = b;
    }

    public static int[] convert3DtoArray(int[][][] b) {
        int counter = 0;
        int h = b.length, w = b[0].length, d = b[0][0].length;
        int[] data = new int[h * w * d];

        for (int[][] ints : b) {
            for (int column = 0; column < w; column++) {
                for (int i = 0; i < 3; i++) {
                    data[counter] = ints[column][i];
                    counter++;
                }
            }
        }
        return data;
    }

    public static int[] getPixel(int r, int c) {
        return getBoard()[r][c];
    }

    public static void printSchemes() {
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 1 ", "W", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 2 ", "N", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 3 ", "NW", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 4 ", "N + W - NW", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 5 ", "N + (W - NW)/2", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 6 ", "W + (N - NW)/2", "|");
        System.out.printf("%-10s %-82s %-1s\n", "| Scheme 7 ", "(N + W)/2", "|");
        System.out.printf("%-10s %-20s  %-61s %-1s\n", "| Scheme 8", "[ min(N,W)", "if NW>=max(A,B)", "|");
        System.out.printf("%-10s %-20s  %-61s %-1s\n", "| ", "[ max(N,W)", "if NW<=min(A,B)", "|");
        System.out.printf("%-10s %-20s  %-61s %-1s\n", "| ", "[ N + W - NW", "else", "|");
        System.out.println("------------------------------------------------------------------------------------------------");


    }

    public static int[] decide(int scheme, int r, int c) {
        int[] zeros = new int[3];
        if (scheme == 1) {
            return getW(r, c);
        } else if (scheme == 2) {
            return getN(r, c);
        } else if (scheme == 3) {
            return getNW(r, c);
        } else if (scheme == 4) {
            for (int i = 0; i < 3; i++) {
                zeros[i] =
                        control(getN(r, c)[i] + getW(r, c)[i] - getNW(r, c)[i]);
            }
            return zeros;
        } else if (scheme == 5) {
            for (int i = 0; i < 3; i++) {
                zeros[i] =
                        control(getN(r, c)[i] + (getW(r, c)[i] - getNW(r, c)[i]) / 2);
            }
            return zeros;
        } else if (scheme == 6) {
            for (int i = 0; i < 3; i++) {
                zeros[i] =
                        control(getW(r, c)[i] + (getN(r, c)[i] - getNW(r, c)[i]) / 2);
            }
            return zeros;
        } else if (scheme == 7) {
            for (int i = 0; i < 3; i++) {
                zeros[i] =
                        control((getN(r, c)[i] + getW(r, c)[i]) / 2);
            }
            return zeros;
        } else {
            for (int i = 0; i < 3; i++) {
                if (getNW(r, c)[i] >= Math.max(getW(r, c)[i], getN(r, c)[i]))
                    zeros[i] = control(Math.min(getW(r, c)[i], getN(r, c)[i]));
                else if (getNW(r, c)[i] <= Math.min(getW(r, c)[i], getN(r, c)[i]))
                    zeros[i] = control(Math.max(getW(r, c)[i], getN(r, c)[i]));
                else
                    zeros[i] = control(getW(r, c)[i] + getN(r, c)[i] - getNW(r, c)[i]);
            }
            return zeros;
        }
    }

    public static int control(int comp) {
        if (comp < 0) {
            comp += 256;
        }
        comp = comp % 256;
        return comp;
    }
}

