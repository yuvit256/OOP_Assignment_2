import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtendThread extends Thread{
    private String filename;
    private AtomicInteger numLines = new AtomicInteger(0);

    public ExtendThread(){
        this.filename = "";
    }
    public ExtendThread(String filename){
        this.filename = filename;
    }
    @Override
    public void run() {
        try {
            File file = new File(this.filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) {
                this.numLines.incrementAndGet();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("An IOException occurred while reading the file " + this.filename);
            e.printStackTrace();
        }
    }
    public AtomicInteger getNumLine()
    {
        return this.numLines;
    }
}
