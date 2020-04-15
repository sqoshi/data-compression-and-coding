package IndexesCoding;

public class EliasGamma implements Coding {

    @Override
    public int decode(String value) {
        MyBuffer.nextVal = value;
        int N = 0;
        while (value.charAt(N) == '0') {
            N++;
            if (value.length() <= N) {
                MyBuffer.nextVal = null;
                return 1;
            }
        }
        String binary = value.substring(N, 2 * N + 1);
        int x = Integer.parseInt(binary, 2);

        MyBuffer.nextVal = value.substring(2 * N + 1);
        if (value.substring(2 * N + 1).length() == 0)
            MyBuffer.nextVal = null;

        return x;
    }

    @Override
    public String encode(int number) {
        StringBuilder stb = new StringBuilder();
        double N = Math.floor(Math.log(number) / Math.log(2));
        while (N > 0) {
            N--;
            stb.append("0");
        }
        stb.append(Integer.toBinaryString(number));
        return stb.toString();
    }
}
