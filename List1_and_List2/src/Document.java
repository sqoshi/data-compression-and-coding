import java.util.ArrayList;

public class Document implements Comparable<Document> {
    public static int symbolsQuantity;
    public ArrayList<Document> afterSymbolList;
    private String c;
    private int repetitions;
    private float frequency;
    private char asChar;

    public Document(String c, int repetitions) {
        float help = symbolsQuantity;
        this.c = c;
        this.repetitions = repetitions;
        this.frequency = repetitions / help;
        this.asChar = (char) Integer.parseInt(c, 2);
    }

    public Document(String c, int repetitions, ArrayList<Document> afSymbolList) {
        this(c, repetitions);
        this.afterSymbolList = afSymbolList;
    }

    public ArrayList<Document> getAfterSymbolList() {
        return afterSymbolList;
    }

    public void setAfterSymbolList(ArrayList<Document> afterSymbolList) {
        this.afterSymbolList = afterSymbolList;
    }

    public char getAsChar() {
        return asChar;
    }

    public void setAsChar(char asChar) {
        this.asChar = asChar;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }


    public String toString() {
        String str = "";
        if (getAfterSymbolList() != null) str = "\n(" + getC() +
                ", " + getRepetitions() + ", " + getAsChar() + " , " + getFrequency() + ", \u001b[31m" + beautyPrint()+ " \u001B[0m)\n";
        else str = "\n(" + getC() +
                ", " + getRepetitions() + ", " + getAsChar() + " , " + getFrequency() + ")";
        return str;
    }

    private String beautyPrint() {
        StringBuilder stb = new StringBuilder();
        stb.append("[");
        for (int i = 0; i < getAfterSymbolList().size(); i++) {
            stb.append("(").append(getAfterSymbolList().get(i).getC()).append(", ").append(getAfterSymbolList().get(i).getRepetitions()).append(", ").append(getAfterSymbolList().get(i).getAsChar()).append(", ").
                    append(getAfterSymbolList().get(i).getFrequency()).append(")");
            if (i != getAfterSymbolList().size() - 1) stb.append(", ");
            else stb.append("]");
        }
        return stb.toString();
    }

    @Override
    public int compareTo(Document o) {
        //return Integer.compare(o.getFrequency(), this.getFrequency());
        return this.getC().compareTo(o.getC());
    }
}
