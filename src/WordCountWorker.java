import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by Michael on 10/12/2015.
 */
public class WordCountWorker implements Runnable {
    private ArrayList<String> chunk;
    private TreeMap<String, Integer> counts;
    private static int chunkNum = 0;

    WordCountWorker(ArrayList<String> _chunk) {
        chunk = _chunk;
        counts = new TreeMap<>();
        ++chunkNum;
    }

    public void run() {
        for (String oneLine : chunk) {
            String[] line = oneLine.split("\\s+");
            for(String word : line) {
                Integer freq = counts.get(word); //use word=key to get value=freq
                if (freq == null) { //word doesn't exist
                    counts.put(word, 1);
                } else { //word exists, increment count
                    counts.put(word, freq + 1);
                }
            }
        }

        FileOutputStream oFile = null;
        ObjectOutputStream oChunk = null;
        try {
            oFile = new FileOutputStream("chunk" + chunkNum + ".txt");
            oChunk = new ObjectOutputStream(oFile);
            for (int i = 0; i < counts.size(); ++i) {
                oChunk.writeObject(counts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

