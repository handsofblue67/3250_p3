/**
 * Created by Michael on 10/12/2015.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class WordCount {
    //private static TreeMap<String, Integer> results;
    public static void main(String[] args) throws IOException, InterruptedException {
        //args.length must be 3
        //create n threads, where n is args[2]

        /*each thread will be passed a chunk size decided by args[1]
        thread1 gets thread size, and the chunk to process the stream
        */

        int chunkSize = Integer.parseInt(args[1]);
        //number of threads that will be in the pool
        ExecutorService ex = Executors.newFixedThreadPool(Integer.parseInt(args[2]));
        File dir = new File(args[0]);
        Scanner scan = null;
        if (dir.isDirectory()) { //if a directory is to be processed
            File[] fileList = dir.listFiles(); //create array of all of the files in the directory
            ArrayList<String> chunk = new ArrayList<>(); //create new list to store a chunk of given size
            for (File file : fileList) { //iterate through every file in the directory
                scan = new Scanner(new FileReader(file)); //stream to current file
                while (scan.hasNextLine()) { //while not the end of file, and chunk is not full
                    chunk.add(scan.nextLine().toLowerCase()); //read one line into the list
                    if (chunk.size() == chunkSize) { //if chunk size broke the loop
                        ex.submit(new WordCountWorker(chunk.toArray(), file.getName())); //submit new thread to executor
                        chunk.clear();
                    }
                }
                scan.close();
            }
        } else if (dir.isFile()) { //if only one file is to be processed
            scan = new Scanner(new FileReader(dir)); //create new stream
            ArrayList<String> chunk = new ArrayList<>(); //create new list to hold the chunk
            while (scan.hasNextLine()) { //loop until end of file
                chunk.add(scan.nextLine().toLowerCase()); //add a line from file to chunk
                if (chunk.size() == chunkSize) { //if current chunk is full
                    ex.submit(new WordCountWorker(chunk.toArray(), dir.getName())); //submit new thread to executor
                    chunk.clear();
                }
            }
            scan.close(); //close file
        }
        ex.shutdown(); //ask executor to terminate
        ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); //wait for all processes to finish

        WordCountWorker.printResults(); //print the final tree from the WordCountWorker class
    }
}

