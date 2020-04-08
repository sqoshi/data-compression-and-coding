package IndexesCoding;

public class EliasOmega implements Elias {
    static String nextVal;

    public static String getNextVal() {
        return nextVal;
    }

    @Override
    public String encode(int number) {
        StringBuilder stb = new StringBuilder();
        stb.append("0");
        int k = number;
        while (k > 1) {
            String binary = Integer.toBinaryString(k);
            stb.insert(0, binary);
            k = binary.length() - 1;
        }
        return (stb.toString());
    }

    @Override
    public int decode(String value) {
        int num = 1;
        while (value.length() > 0 && value.charAt(0) == '1') {
            value = value.substring(1);
            int len = num;
            num = 1;
            for (int i = 0; i < len; i++) {
                num <<= 1;
                if (value.length() > 0) {
                    if (value.charAt(0) == '1')
                        num |= 1;
                    value = value.substring(1);
                }
            }
        }
        if (value.length() > 2) {
            nextVal = value.substring(1);
        } else
            nextVal = null;
        return num;
    }

}
