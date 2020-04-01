import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataCollector {
    private File file;
    private byte[] data;
    private String[] data8bit;

    public DataCollector(String filepath) {
        this.file = new File(filepath);
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void read() throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(getFile());
        byte[] fileInBytes = new byte[(int) getFile().length()];
        try {
            fileStream.read(fileInBytes, 0, fileInBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setData(fileInBytes);
    }

    public void translate() {
        String[] strings8bit = new String[(int) getFile().length()];
        for (int i = 0; i < getFile().length(); i++)
            strings8bit[i] = String.format("%8s", Integer.toBinaryString((byte) getData()[i] & 0xFF)).replace(' ', '0');
        setData8bit(strings8bit);
    }

    public String[] getData8bit() {
        return data8bit;
    }

    public void setData8bit(String[] data8bit) {
        this.data8bit = data8bit;
    }
}
