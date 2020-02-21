public class Pair implements Comparable<Pair> {
    private String c;
    private int frequency;

    public Pair(String c, int frequency) {
        this.c = c;
        this.frequency = frequency;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return
                "(" + c +
                        ", " + frequency + ")";
    }

    public String toStringAsChar() {
        int parseInt = Integer.parseInt(c, 2);
        char ch = (char) parseInt;
        return
                "(" + c +
                        ", " + frequency + ", " + ch + ")";
    }


    @Override
    public int compareTo(Pair o) {
        //return Integer.compare(o.getFrequency(), this.getFrequency());
        return this.getC().compareTo(o.getC());
    }
}
