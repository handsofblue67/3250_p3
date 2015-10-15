import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by Michael on 10/12/2015.
 */
public class WordCountWorker implements Runnable {
    private ArrayList<String> chunk;
    private TreeMap<String, Integer> counts;
    private static int chunkNum = 0;

    public WordCountWorker(ArrayList<String> _chunk) {
        chunk = _chunk;
        counts = new TreeMap<>();
        //increment the number of these threads created with a static, synchronized function
        increment();
    }


    public void run() {
        for (String oneLine : chunk) {
            String[] line = oneLine.split("\\s+");
            for (String word : line) {
                word.replaceAll("\\W+", "");
                Integer freq = counts.get(word); //use word=key to get value=freq
                if (freq == null) { //word doesn't exist
                    counts.put(word, 1);
                } else { //word exists, increment count
                    counts.put(word, freq + 1);
                }
            }
        }
        printChunk();
    }

    public synchronized void printChunk() {
        try {
            BufferedWriter outStream = new BufferedWriter(new FileWriter("chunk" + chunkNum + ".txt"));
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                outStream.write(entry.getKey() + "\t" + entry.getValue() + "\n");

            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static synchronized void increment() {
        ++chunkNum;
    }
}

