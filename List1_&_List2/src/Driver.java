import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Driver {
    private static byte[] data;
    private static String[] data8Bit;
    private static ArrayList<Document> InformationList;


    public static void main(String[] args) throws IOException {
        loadFile("kod.txt");
        to8bit();
        System.out.println("1");
        checkDataFrequency();
        System.out.println("2");
        Collections.sort(getInformationList());
        System.out.println("3");
        findAllSymbolsAfter();
        System.out.println("4");
        System.out.println(getInformationList().toString());
        System.out.println("5");
        System.out.println(calculateEntrophy());
        System.out.println(calculateConditionalEntrophy("01110000"));
        saveResultInFile();

    }

    private static double calculateConditionalEntrophy(String str) {
        int index = -1;
        for (int i = 0; i < getInformationList().size(); i++) {
            if (getInformationList().get(i).getC().equals(str)) {
                index = i;
                break;
            }
        }
        double H = 0;
        for (int i = 0; i < getInformationList().get(index).getAfterSymbolList().size(); i++) {
            H += getInformationList().get(index).getAfterSymbolList().get(i).getFrequency()
                    * Math.log(getInformationList().get(index).getAfterSymbolList().get(i).getFrequency()) / Math.log(2);
        }
        return (-1) * H;
    }

    private static double calculateEntrophy() {
        double H = 0;
        for (int i = 0; i < getInformationList().size(); i++) {
            H += getInformationList().get(i).getFrequency() * Math.log(getInformationList().get(i).getFrequency()) / Math.log(2);
        }
        return (-1) * H;
    }

    private static void findAllSymbolsAfter() {

        System.out.println(getInformationList().size());

        for (int i = 0; i < getInformationList().size(); i++) {
            System.out.println(i);//zakomentowanie
            findSymbolsAfter(getInformationList().get(i).getC());
        }
    }

    /***
     * Metoda jest obszerna z powodu potrzeby wyliczenia ilosci powtorzen danego znaku po str.
     * Zatem podzielenie go na mniejsze metody prowadzi tylko do skomplikowania kodu.
     * @param str - konkretny znak po ktorym wyszukiwane sa pozostale znaki, oczywiscie bez potworzen.
     */
    private static void findSymbolsAfter(String str) {
        ArrayList<String> symbolsAfterList = new ArrayList<>();
        int ctr = 0;
        //tworzenie listy ssymboli wystepujacych po danym symbolu str
        for (int i = 0; i < getData().length; i++) {
            if (getData8Bit()[i].equals(str) && i + 1 != getData().length) ctr++;
            if (getData8Bit()[i].equals(str) && i + 1 != getData().length && !symbolsAfterList.contains(getData8Bit()[i + 1])) {
                symbolsAfterList.add(getData8Bit()[i + 1]);
            }
        }
        //    System.out.println(symbolsAfterList + " " + ctr);
        //znalezienie indexu symbolu na liscie glownej
        int index = -1;
        for (int i = 0; i < getInformationList().size();
             i++) {
            if (getInformationList().get(i).getC().equals(str)) index = i;
        }
        //utworzenie podlisty w liscie glownej w ktorej przetrzymywane beda informacje o czestosciach symboli wystepujacych po symbolu str
        getInformationList().get(index).afterSymbolList = new ArrayList<>();

        for (int i = 0; i < symbolsAfterList.size(); i++) {
            int counter = 1;
            for (int j = 1; j < getData8Bit().length; j++) {
                if (symbolsAfterList.get(i).equals(getData8Bit()[j]) && getData8Bit()[j - 1].equals(str)) {
                    counter++;
                }
            }
            getInformationList().get(index).afterSymbolList.add(new Document(symbolsAfterList.get(i), counter - 1));
            getInformationList().get(index).afterSymbolList.get(getInformationList().get(index).afterSymbolList.size() - 1).setFrequency((float) (counter - 1) / ctr);
        }
        // System.out.println(getInformationList().get(index).afterSymbolList);
    }



    private static void checkDataFrequency() {
        ArrayList<Document> resultList = new ArrayList<>();
        String[] help = new String[getData().length];
        System.arraycopy(getData8Bit(), 0, help, 0, getData().length);
        Arrays.sort(help);
        int counter = 1;
        for (int i = 0; i < help.length; i++) {
            if (i + 1 == help.length) break;
            if (help[i].equals(help[i + 1])) {
                counter++;
            }
            if (!help[i].equals(help[i + 1])) {
                resultList.add(new Document(help[i], counter));
                counter = 1;
            }

        }
        setInformationList(resultList);
    }

    private static void to8bit() {
        String[] help = new String[getData().length];
        StringBuilder str = new StringBuilder();
        int counter = 0;
        for (byte b : getData()) {
            int val = b;

            for (int i = 0; i < 8; i++) {
                str.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            help[counter] = String.valueOf((str));
            counter++;
            str = new StringBuilder();
        }
        setData8Bit(help);
    }

    private static void loadFile(String filename) throws IOException {
        File file = new File("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/" + filename);
        FileInputStream fileStream = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];

        fileStream.read(arr, 0, arr.length);
        setData(arr);
        Document.symbolsQuantity = getData().length;
    }

    private static void saveResultInFile() {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("/home/piotr/Documents/data-compression-and-coding/List1_&_List2/src/data/result.txt"), "utf-8"));
            Collections.sort(getInformationList());
            writer.write(InformationList.toString());
        } catch (IOException ex) {
            // Report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public static ArrayList<Document> getInformationList() {
        return InformationList;
    }

    public static void setInformationList(ArrayList<Document> informationList) {
        InformationList = informationList;
    }

    public static String[] getData8Bit() {
        return data8Bit;
    }

    public static void setData8Bit(String[] data8Bit) {
        Driver.data8Bit = data8Bit;
    }

    public static byte[] getData() {
        return data;
    }

    public static void setData(byte[] data) {
        Driver.data = data;
    }


    private static void printAsChar() {
        for (int X : getData()) {
            System.out.print((char) X);
        }
    }
}

