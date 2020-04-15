package IndexesCoding;

public class EliasDelta implements Coding {


    @Override
    public int decode(String value) {
        int L = 0;
        while (value.charAt(L) == '0') {
            L++;
            if (value.length() <= L) {
                MyBuffer.nextVal = null;
                return 1;
            }
        }
        String nplus1 = (value.substring(0, L + 1 + L));
        int N = (Integer.parseInt(nplus1, 2)) - 1;
        int rest = 0;
        String temp = value.substring(L + 1 + L, 2 * L + 1 + N);
        if ((!temp.equals("")))
            rest = Integer.parseInt(temp, 2);

        MyBuffer.nextVal = value.substring(2 * L + 1 + N);
        if (value.substring(2 * L + 1 + N).length() == 0)
            MyBuffer.nextVal = null;
        return (int) (Math.pow(2, N) + rest);
    }

    @Override
    public String encode(int number) {
        StringBuilder stb = new StringBuilder();
        double N = Math.floor(Math.log(number) / Math.log(2));
        double L = Math.floor(Math.log(N + 1) / Math.log(2));
        for (int i = 0; i < L; i++) {
            stb.append("0");
        }
        stb.append(Integer.toBinaryString((int) N + 1));
        stb.append(Integer.toBinaryString(number).substring(1));
        return stb.toString();
    }
}
