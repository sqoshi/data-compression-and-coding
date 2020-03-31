import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class Decoder {
    private Map<String, Integer> dictionary;
    private String code;

    public Decoder(Map<String, Integer> dictionary, String code) {
        this.dictionary = dictionary;
        this.code = code;
    }

    public static void saveResultToFile(String fileContent) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/decode"));
        writer.write(fileContent);
        writer.close();
    }

    public static String getKeyFromValue(Map<String, Integer> hm, Integer value) {

        for (String o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return String.valueOf(o);
            }
        }
        return null;
    }

    public Map<String, Integer> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, Integer> dictionary) {
        this.dictionary = dictionary;
    }

    public String decode(boolean saveToFile) {
        StringBuilder stb = new StringBuilder();
        char[] text = getCode().toCharArray();
        //pk := pierwszy kod skompresowanych danych
        String pk = String.valueOf(text[0]);
        //Wypisz na wyjście ciąg związany z kodem pk, tj. słownik[pk]
        stb.append(getKeyFromValue(getDictionary(), Integer.parseInt(pk)));
        int i = 1;
        //Dopóki są jeszcze jakieś słowa kodu:
        while (i < text.length) {
            System.out.println(getDictionary());
            String k = String.valueOf(text[i]);
            String pc = (String) getKeyFromValue(getDictionary(), Integer.parseInt(pk));
            if (pc != null)
                if (getDictionary().containsValue(Integer.parseInt(k))) {
                    String firstSign = ((String) (getKeyFromValue(getDictionary(), Integer.parseInt(k)))).substring(0, 1);
                    String ctd = (pc).concat(firstSign);
                    getDictionary().put(ctd, max(getDictionary()) + 1);
                    stb.append(((String) (getKeyFromValue(getDictionary(), Integer.parseInt(k)))));
                    System.out.println(ctd + " tutaj");
                } else {
                    String firstSign = (pc).substring(0, 1);
                    String ctd = (pc).concat(firstSign);
                    getDictionary().put(ctd, max(getDictionary()) + 1);
                    System.out.println(ctd + " tutaj2");
                    stb.append(ctd);
                }
            pk = k;
            i++;
        }
        System.out.println(stb);

        if (saveToFile) {
            try {
                saveResultToFile(String.valueOf(stb));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(stb);
    }

    public <K, V extends Comparable<V>> V max(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = Collections.max(map.entrySet(), Map.Entry.comparingByValue());
        return maxEntry.getValue();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
