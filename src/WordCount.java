/**
 * Created by Michael on 10/12/2015.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.*;

public class WordCount {
    public static void main(String[] args) {
        //final long startTime = System.currentTimeMillis();
        File dir = new File(args[0]);
        try {
            if (args.length != 3 || (Integer.parseInt(args[1]) < 10 || Integer.parseInt(args[1]) > 5000) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > 100)) {
                System.out.println("Usage: java WordCount <file|directory> <chunk size 10-5000> <num of threads 1-100>");
            } else {
                int chunkNum = 0;
                int chunkSize = Integer.parseInt(args[1]);
                //number of threads that will be in the pool
                ExecutorService ex = Executors.newFixedThreadPool(Integer.parseInt(args[2]));
                Scanner scan = null;
                if (dir.isDirectory()) { //if a directory is to be processed
                    File[] fileList = dir.listFiles(); //create array of all of the files in the directory
                    Arrays.sort(fileList);
                    ArrayList<String> chunk = new ArrayList<>(); //create new list to store a chunk of given size
                    for (File file : fileList) { //iterate through every file in the directory
                        chunkNum = 0;
                        scan = new Scanner(new FileReader(file)); //stream to current file
                        while (scan.hasNextLine()) { //while not the end of file, and chunk is not full
                            chunk.add(scan.nextLine().toLowerCase()); //read one line into the list
                            if (chunk.size() == chunkSize) { //if chunk size broke the loop
                                ex.submit(new WordCountWorker(chunk.toArray(new String[chunk.size()]), String.format("%s_%s.chunk", file.getName(), chunkNum++))); //submit new thread to executor
                                chunk.clear();
                            }
                        }
                        scan.close();
                        if (chunk != null && chunk.size() != chunkSize) {
                            ex.submit(new WordCountWorker(chunk.toArray(new String[chunk.size()]), String.format("%s_%s.chunk", file.getName(), chunkNum++))); //submit new thread to executor); // submit partial chunk if reached end of a file
                            chunk.clear();
                        }
                    }
                } else if (dir.isFile()) { //if only one file is to be processed
                    scan = new Scanner(new FileReader(dir)); //create new stream
                    ArrayList<String> chunk = new ArrayList<>(); //create new list to hold the chunk
                    while (scan.hasNextLine()) { //loop until end of file
                        chunk.add(scan.nextLine().toLowerCase()); //add a line from file to chunk
                        if (chunk.size() == chunkSize || !scan.hasNextLine()) { //if chunk size broke the loop
                            ex.submit(new WordCountWorker(chunk.toArray(new String[chunk.size()]), String.format("%s_%s.chunk", dir.getName(), chunkNum++))); //submit new thread to executor); // submit partial chunk if reached end of a file
                            chunk.clear();
                        }
                    }
                    scan.close(); //close file
                }
                //final long endReadTime = System.currentTimeMillis();
                //System.out.println(String.format("main finished reading the file/directory, and it took: %s:%s seconds",  (endReadTime - startTime) / 1000, (endReadTime - startTime) % 1000));
                ex.shutdown(); //ask executor to terminate
                ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); //wait for all processes to finish
                WordCountWorker.printResults(); //print the final tree from the WordCountWorker class
                //final long endTime = System.currentTimeMillis();
                //System.out.println(String.format("total execution time: %s:%s seconds",  (endTime - startTime) / 1000, (endTime - startTime) % 1000));
            }

        } catch (Exception e){
            if (e.toString().equals("java.lang.NullPointerException")) {
                System.out.println(String.format("No such file/directory: %s", args[0]));
            } else {
                e.printStackTrace();
            }
        }
    }
}

