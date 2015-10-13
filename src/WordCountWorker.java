import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Created by Michael on 10/12/2015.
 */
public class WordCountWorker implements Runnable {
    private int chunkSize;
    private int lineCount;
    private BufferedReader br;
    WordCountWorker(BufferedReader _br, int _chunkSize) {
        br = _br;
        chunkSize = _chunkSize;
        lineCount = 0;
    }

    public void run() {

    }
}
