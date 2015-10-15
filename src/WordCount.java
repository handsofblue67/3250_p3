/**
 * Created by Michael on 10/12/2015.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.*;

public class WordCount {
    private static TreeMap<String, Integer> results;
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        //args.length must be 3
        //create a treemap which all thread will add to, and will be printed out into the master output
        //create n threads, where n is args[2]
        /*each thread will be passed a chunk size decided by args[1]
        thread1 gets thread size, and the chunk to process the stream
        */

        int chunkSize = Integer.parseInt(args[1]);
        //number of threads that will be in the pool
        ExecutorService ex = Executors.newFixedThreadPool(Integer.parseInt(args[2]));
        File dir = new File(args[0]);
        File[] fileList = dir.listFiles();
        Scanner scan = null;
        int linesOfChunkRead = 0;
        boolean endOfFile = false;
        String[] temp;
        if(fileList != null) {
            for (int i = 0; i < fileList.length; ++i) {
                File file = fileList[i];
                if (file.isFile()) {
                    scan = new Scanner(new FileReader(file));
                    ArrayList<String> chunk = new ArrayList();
                    while (scan.hasNextLine() && linesOfChunkRead++ < chunkSize) {
                        chunk.add(scan.nextLine().toLowerCase());
                    }
                    if (linesOfChunkRead >= chunkSize) {
                        ex.submit(new WordCountWorker(chunk));
                        linesOfChunkRead = 0;
                    }
                }
            }
        }
        ex.shutdown();
        ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        try {
            BufferedWriter outStream = new BufferedWriter(new FileWriter("results.txt"));
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                outStream.write(entry.getKey() + "\t" + entry.getValue() + System.getProperty("line.separator"));
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //add a entry to the result tree, if the string key already exists, increment the value
    public static synchronized void addResult(String _word, Integer _num) {
        if (results == null) {
            results = new TreeMap<>();
        }
        Integer _freq = results.get(_word);
        if (_freq == null) {
            results.put(_word, _num);
        } else {
            results.put(_word, _freq + 1);
        }
    }
}

