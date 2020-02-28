public class prime {
    public static void main(String[] args) {
        System.out.println(isPrime(11));
        System.out.println(isPrime(12));
    }

    private static boolean isPrime(int n) {
        for (int j = 2; j < n; ++j) {
            if (n % j == 0) return false;
        }
        return true;
    }
}
