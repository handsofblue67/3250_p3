import java.io.*;
import java.util.*;

/**
 * Created by Michael on 10/12/2015.
 * Multiple instances of this class are able to be run.
 * To be passes an array of strings (making a chunk)
 * The class will remove non-word characters count the frequency of each word, and then sort according to the frequency
 */

public class WordCountWorker implements Runnable {
    private String[] chunk; //holds the unsorted, raw chunk to be separated, each index can contain a line of text
    private HashMap<String, Integer> counts; //the structure used to count the words
    private static HashMap<String, Integer> results; //the structure used to count the words for the final results file (combines all chunk results)
    private String fileName; //name of the original file, for naming the chunk output file
    private static File output; //output folder
    private File oFile; //output file

    //constructor for a WordCountWorker object/thread
    public WordCountWorker(Object[] chunk, String fileName) throws IOException {
        /* increment the number of these threads created with a static, synchronized function
        create a unique file name, based on the original file being processed, and the thread number*/
        this.fileName = fileName;
        if (output == null){ //create new folder in the working directory to store the ouput files
            String current = System.getProperty("user.dir"); //get working directory
            output = new File(current, "output");
            output.mkdir();
            for (File file : output.listFiles()) {
                file.delete();
            }
        }
        this.oFile = new File (output, fileName); //add new file to the output directory
        this.chunk = Arrays.copyOf(chunk, chunk.length, String[].class); //parse the array of objects to o an array of strings
        this.counts = new HashMap<>();
    }

    public void run() {
        for (String oneLine : chunk) {
            String[] line = oneLine.split("\\s+"); //split lines into individual words
            for (String word : line) {
                word = word.replaceAll("_", ""); //remove underscores (not removed by \\W for some reason
                word = word.replaceAll("\\d+", ""); //remove numbers
                word = word.replaceAll("\\W+", ""); //remove non-word characters
                if (!word.equals("")) { //ignore all null strings
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
        printChunk(); // write processed chunk to file
    }

    //call when chunk is fully processed to create a new file with the appropriate name, and write out all data from tree
    //may not need to be synchronized, but there to avoid bogging down the harddrive
    public synchronized void printChunk() {
        try {
            FileWriter fw = new FileWriter(oFile);
            BufferedWriter outStream = new BufferedWriter(fw);
            TreeSet<Pair> decend = new TreeSet<>(new PairComparator());
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                decend.add(new Pair(entry.getValue(), entry.getKey())); //store each key and value (count and word) into Pair object, the sort into tree
            }

            for (Pair _pair : decend) { //after sorting is complete, write out to chunk file
                outStream.write(_pair.getWord() + "\t" + _pair.getInstances() + System.getProperty("line.separator"));
            }

            outStream.close(); //close stream
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //print results function
    //to be called as generic "WordClassWorker" after all processing is complete
    public static void printResults() {
        try { //print the results tree at the end of main
            BufferedWriter outStream = new BufferedWriter(new FileWriter(new File(output, "results.txt")));
            TreeSet<Pair> decendResults = new TreeSet<>(new PairComparator());
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

