import java.io.IOException;
import java.util.Arrays;

public class Driver {
    static byte[][][] board;

    public static byte[] getN(int r, int c) {
        if (r - 1 >= 0)
            return getPixel(r - 1, c);
        else
            return new byte[]{(char) 0, (char) 0, (char) 0};
    }

    public static byte[] getW(int r, int c) {
        if (c - 1 >= 0)
            return getPixel(r, c - 1);
        else
            return new byte[]{(char) 0, (char) 0, (char) 0};
    }

    public static byte[] getNW(int r, int c) {
        if (r - 1 >= 0 && c - 1 >= 0)
            return getPixel(r - 1, c - 1);
        else
            return new byte[]{(char) 0, (char) 0, (char) 0};
    }

    public static byte[][][] getBoard() {
        return board;
    }


    public static void main(String[] args) throws IOException, IllegalAccessException {
        byte[] data = Reader.read("/home/piotr/Documents/data-compression-and-coding/Tests/images/example0.tga");
        int h = Reader.getHeight();
        int w = Reader.getWidth();
        System.out.println(h + " " + w);
        System.out.println("Full= " + Entropy.calculateFullEntropy(data));
        System.out.println("B= " + Entropy.calculateComponentEntropy('B', data));
        System.out.println("G= " + Entropy.calculateComponentEntropy('G', data));
        System.out.println("R= " + Entropy.calculateComponentEntropy('R', data));
        System.out.println();
        createBoard(data, h, w);
        int scheme = 1;
        System.out.println(
                Entropy.calculateFullEntropy(convert3DtoArray(build(scheme))));
        System.out.println(
                Entropy.calculateComponentEntropy('B',
                        convert3DtoArray(build(scheme))));
        System.out.println(Entropy.calculateComponentEntropy('G', convert3DtoArray(build(scheme))));
        System.out.println(Entropy.calculateComponentEntropy('R', convert3DtoArray(build(scheme))));
    }

    public static byte[][][] build(int scheme) {
        int h = board.length, w = board[0].length, d = board[0][0].length;
        byte[][][] schema = new byte[h][w][d];

        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                schema[row][column] = decide(scheme, row, column);
                if (schema[row][column] != board[row][column] && column>=1          )
                    System.out.println(Arrays.toString(schema[row][column]) + " " +
                            Arrays.toString(board[row][column]));
            }
        }
        return schema;
    }

    public static void printBoard(byte[][][] b) {
        int w = b[0].length;
        for (byte[][] bytes : b) {
            for (int column = 0; column < w; column++) {
                System.out.print(Arrays.toString(bytes[column]) + ", ");
            }
            System.out.println();
        }
    }

    public static void createBoard(byte[] bytes, int h, int w) {
        byte[][][] b = new byte[h][w][3];
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

    public static byte[] convert3DtoArray(byte[][][] b) {
        int counter = 0;
        int h = b.length, w = b[0].length, d = b[0][0].length;
        byte[] data = new byte[h * w * d];

        for (byte[][] bytes : b) {
            for (int column = 0; column < w; column++) {
                for (int i = 0; i < 3; i++) {
                    data[counter] = bytes[column][i];
                    counter++;
                }
            }
        }
        return data;
    }

    public static byte[] getPixel(int r, int c) {
        return getBoard()[r][c];
    }

    public static void setPixel(int r, int c, byte[] BGR) {
        board[r][c] = BGR;
    }

    public static byte[] decide(int scheme, int r, int c) {
        byte[] zeros = new byte[3];
        if (scheme == 1) {
            return getW(r, c);
        } else if (scheme == 2) {
            return getN(r, c);
        } else if (scheme == 3) {
            return getNW(r, c);
        } else if (scheme == 4) {
            for (int i = 0; i < 3; i++) {
                zeros[i] = getN(r, c)[i];
                zeros[i] += getW(r, c)[i];
                zeros[i] -= getNW(r, c)[i];
                zeros[i] %= 256;
            }
            return zeros;
        } else
            return zeros;
    }
}

