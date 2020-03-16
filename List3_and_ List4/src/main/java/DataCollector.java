import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataCollector {
    private Map<Byte, Integer> mapOfSymbols; // SYMBOL, QUANTITY
    private byte[] textInBytes;
    private String[] textAs8BitASCII;

    public DataCollector() {
        mapOfSymbols = new HashMap<>();
        try {
            loadFile("/home/piotr/Documents/data-compression-and-coding/List3_and_ List4/src/main/java/data/text");
            insertAlphabetIntoMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getTextAs8BitASCII() {
        return textAs8BitASCII;
    }

    public void setTextAs8BitASCII(String[] textAs8BitASCII) {
        this.textAs8BitASCII = textAs8BitASCII;
    }

    public Map<Byte, Integer> getMapOfSymbols() {
        return mapOfSymbols;
    }

    public void setMapOfSymbols(Map<Byte, Integer> mapOfSymbols) {
        this.mapOfSymbols = mapOfSymbols;
    }

    void printMap(Map<Byte, Integer> map) {
        for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey().toString() + ":" + entry.getValue().toString());
        }
    }


    public void insertAlphabetIntoMap() {
        mapOfSymbols = new HashMap<>();
        int quantity_b;
        for (byte b : getTextInBytes()) {
            if (mapOfSymbols.containsKey(b)) {
                quantity_b = mapOfSymbols.get(b);
                quantity_b += 1;
            } else {
                quantity_b = 1;
            }
            mapOfSymbols.put(b, quantity_b);
        }

    }

    public byte[] getTextInBytes() {
        return textInBytes;
    }

    public void setTextInBytes(byte[] textInBytes) {
        this.textInBytes = textInBytes;
    }

    public void loadFile(String filepath) throws IOException {
        File file = new File(filepath);
        FileInputStream fs = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];
        fs.read(arr, 0, arr.length);
        setTextInBytes(arr);
    }

    private void saveFile(String filepath) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filepath), "utf-8"));
            writer.write(Arrays.toString(getTextInBytes()));
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }


}
