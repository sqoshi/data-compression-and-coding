public class prime {
    public static void main(String[] args) {
    }

    private static boolean isPrime(int n) {
        for (int j = 2; j < n; ++j) {
            if (n % j == 0) return false;
        }
        return true;
    }

    private static int m(int N) {
        StringBuilder stringBuilder = new StringBuilder();
        int zeros = 0;
        long result = 1;
        int i = 1;
        while (i < N) {
            result *= i;
            i++;
        }
        stringBuilder.append(result);

        if (stringBuilder.
                substring(stringBuilder.length() - 1).
                equals("0"))
            zeros++;

        System.out.println(result);
        return zeros;
    }
}
