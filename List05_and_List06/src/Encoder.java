import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class Encoder {
    private Map<String, Integer> dictionary;
    private byte[] dataToEncode;

    public Encoder(Map<String, Integer> dictionary, byte[] dataToEncode) {
        this.dictionary = dictionary;
        this.dataToEncode = dataToEncode;

    }

    public static void saveResultToFile(String fileContent) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code"));
        writer.write(fileContent);
        writer.close();
    }

    public Map<String, Integer> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, Integer> dictionary) {
        this.dictionary = dictionary;
    }

    public byte[] getDataToEncode() {
        return dataToEncode;
    }

    public void setDataToEncode(byte[] dataToEncode) {
        this.dataToEncode = dataToEncode;
    }

    public <K, V extends Comparable<V>> V max(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = Collections.max(map.entrySet(), Map.Entry.comparingByValue());
        return maxEntry.getValue();
    }

    public String encode(boolean saveToFile) {
        StringBuilder stb = new StringBuilder();
        //c := pierwszy symbol wejściowy
        String c = String.valueOf((char) getDataToEncode()[0]);
        int i = 1;
        //Dopóki są dane na wejściu:
        while (i < getDataToEncode().length) {
            // Wczytaj znak s.
            String s = String.valueOf((char) getDataToEncode()[i]);
            String ctd = c.concat(s);
            //Jeżeli ciąg c + s znajduje się w słowniku, przedłuż ciąg c, tj. c := c + s
            if (getDictionary().containsKey(ctd)) {
                c = ctd;
            }
            //Jeśli ciągu c + s nie ma w słowniku, wówczas:
            else {
                //wypisz kod dla c (c znajduje się w słowniku
                stb.append("").append(getDictionary().get(c));
                //dodaj ciąg c + s do słownika
                getDictionary().put(ctd, max(getDictionary()) + 1);
                //przypisz c := s.
                c = s;
            }
            i++;
        }
        //Na końcu wypisz na wyjście kod związany c.
        stb.append("").append(getDictionary().get(c));
        if (saveToFile) {
            try {
                saveResultToFile(String.valueOf(stb));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(stb);
    }
}
