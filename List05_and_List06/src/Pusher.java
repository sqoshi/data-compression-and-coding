import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pusher {
    FileOutputStream fos;
    String queue;

    public Pusher() throws FileNotFoundException {
        fos = new FileOutputStream(new File("/home/piotr/Documents/data-compression-and-coding/List05_and_List06/src/Data/code"));
        queue = "";
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public void updateQueue(String elias_index) {
        StringBuilder stb = new StringBuilder();
        stb.append(getQueue());
        stb.append(elias_index);
        setQueue(stb.toString());
    }

    public void sendByte() throws IOException {
        while (getQueue().length() >= 8) {
            fos.write(Integer.parseInt(getQueue().substring(0, 8), 2));
            setQueue(getQueue().substring(8));
        }
    }

    public void checkLastByte() throws IOException {
        while (getQueue().length() < 8 && !getQueue().equals("")) {
            System.out.println("koncowka " + getQueue() + " " + getQueue().length());
            int zerosToAppend = 8 - getQueue().length() - 1;
            String endOfByte = "0";
            while (zerosToAppend > 0) {
                zerosToAppend--;
                endOfByte = endOfByte.concat("0");
            }
            setQueue(getQueue().concat(endOfByte));
            System.out.println(getQueue());
            sendByte();
        }
    }

    public void push(int index) throws IOException {
        String elias_index = Elias.encode(index);
        updateQueue(elias_index);
        sendByte();
    }
}
