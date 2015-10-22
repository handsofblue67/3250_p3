import java.io.*;
import java.util.*;

/**
 * Created by Michael on 10/12/2015.
 */
public class WordCountWorker implements Runnable {
    private String[] chunk;
    private HashMap<String, Integer> counts;
    private static Integer threadNum; //the unique thread number
    private static HashMap<String, Integer> results; //map treemap of the final output
    private String fileName; //name of the original file
    private static File output; //output folder
    private File oFile; //output file
    private TreeSet<Pair> decend;
    private static TreeSet<Pair> decendResults;

    public WordCountWorker(Object[] _chunk, String _fileName) throws IOException {
        //increment the number of these threads created with a static, synchronized function
        increment();
        fileName = _fileName;
        chunk = Arrays.copyOf(_chunk, _chunk.length, String[].class);
        counts = new HashMap<>();
        if (output == null){
            String current = System.getProperty("user.dir");
            output = new File(current, "output");
            output.mkdir();
        }
        oFile = new File (output, fileName + "_" + threadNum + ".chunk");
    }

    public void run() {
        for (String oneLine : chunk) {
            String[] line = oneLine.split("\\s+");
            for (String word : line) {
                word = word.replaceAll("_", "");
                word = word.replaceAll("\\d+", "");
                word = word.replaceAll("\\W+", "");
                if (!word.equals("")) {
                    word.trim();
                    Integer freq = counts.get(word); //use word=key to get value=freq
                    if (freq == null) { //word doesn't exist
                        counts.put(word, 1);
                    } else { //word exists, increment count
                        counts.put(word, freq + 1);
                    }
                    addResult(word);
                }
            }
        }
        printChunk();
    }

    //call when chunk is fully processed to create a new file with the appropriate name, and write out all data from tree
    //may not need to be synchronized, but there to avoid bogging down the harddrive
    public synchronized void printChunk() {
        try {
            FileWriter fw = new FileWriter(oFile);
            BufferedWriter outStream = new BufferedWriter(fw);
            decend = new TreeSet<>(new PairComparator());
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                decend.add(new Pair(entry.getValue(), entry.getKey()));
            }

            for (Pair _pair : decend) {
                outStream.write(_pair.getWord() + "\t" + _pair.getInstances() + System.getProperty("line.separator"));
            }

            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //increment the number of total chunks created, synchronized to avoid error, as chunkNum is static
    //create the chunk file
    public static synchronized void increment() {
        if (threadNum == null) {
            threadNum = 0;
        }
        ++threadNum;
    }

    //add a entry to the result tree, if the string key already exists, increment the value
    public static synchronized void addResult(String _word) {
        if (results == null) { //create new results map(this is the large one that will be the final output
            results = new HashMap<>();
        }
        Integer _freq = results.get(_word); //check to see if the word exists within the map
        if (_freq == null) { //if the word is unique, insert with a value of 1
            results.put(_word, 1);
        } else { //otherwise increment the value
            results.put(_word, _freq + 1);
        }
    }

    public static void printResults() {
        try { //print the results tree at the end of main
            BufferedWriter outStream = new BufferedWriter(new FileWriter(new File(output, "results.txt")));
            decendResults = new TreeSet<>(new PairComparator());
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                decendResults.add(new Pair(entry.getValue(), entry.getKey()));
            }
            for (Pair _pair : decendResults) {
                outStream.write(_pair.getWord() + "\t" + _pair.getInstances() + System.getProperty("line.separator"));
            }
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

