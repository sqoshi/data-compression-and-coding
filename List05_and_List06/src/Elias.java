public class Elias {
    static String nextVal;

    public static String encode(int number) {
        StringBuilder stb = new StringBuilder();
        stb.append("0");
        int k = number;
        while (k > 1) {
            String binary = Integer.toBinaryString(k);
            stb.insert(0, binary);
            k = binary.length() - 1;

        }

        return stb.toString();
    }

    static int decode(String value) {
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
        if (value.length() > 2)
            nextVal = value.substring(1);
        else
            nextVal = null;
        return num;
    }

    public static void main(String[] args) {
        String to = (encode(137));
        // byte[] data = binStringToBytes(to);
        // System.out.println(Arrays.toString(data));
        // writeBytesTofile("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code", data);
        //System.out.println(Arrays.toString(getBytesFromFile("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code")));
        //  byte[] get = (getBytesFromFile("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code"));
        StringBuilder stb = new StringBuilder();
        // for (byte b : get) {
        //   String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        // System.out.println(s1);
        //stb.append(s1);
        // }
        System.out.println(stb.toString());
        System.out.println(decode("11110001010001011011001000"));
        while (getNextVal() != null)
            System.out.println(decode(getNextVal()));
    }

    public static String getNextVal() {
        return nextVal;
    }

}
