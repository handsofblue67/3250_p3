import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Michael on 10/12/2015.
 */
public class WordCountWorker implements Runnable {
    private ArrayList<String> chunk;
    private TreeMap<String, Integer> counts;
    private static Integer chunkNum;
    private FileWriter fw;

    public WordCountWorker(ArrayList<String> _chunk) throws IOException {
        chunk = _chunk;
        counts = new TreeMap<>();
        //increment the number of these threads created with a static, synchronized function
        increment();
        fw = new FileWriter("chunk" + chunkNum + ".txt");
    }


    public void run() {
        for (String oneLine : chunk) {
            String[] line = oneLine.split("\\s+");
            for (String word : line) {
                word.replaceAll("\\W+", "");
                word.trim();
                Integer freq = counts.get(word); //use word=key to get value=freq
                if (freq == null) { //word doesn't exist
                    counts.put(word, 1);
                    WordCount.addResult(word, 1);
                } else { //word exists, increment count
                    counts.put(word, freq + 1);
                }
            }
        }
        printChunk();
    }


    //call when chunk is fully processed to create a new file with the appropriate name, and write out all data from tree
    //may not need to be synchronized, but there to avoid bogging down the harddrive
    public synchronized void printChunk() {
        try {
            BufferedWriter outStream = new BufferedWriter(fw);
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                outStream.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //increment the number of total chunks created, synchronized to avoid error, as chunkNum is static
    //create the chunk file
    public static synchronized void increment() {
        if (chunkNum == null) {
            chunkNum = 0;
        }
        ++chunkNum;
    }

}

