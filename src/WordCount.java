/**
 * Created by Michael on 10/12/2015.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class WordCount {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //args.length must be 3
        //create a treemap which all thread will add to, and will be printed out into the master output
        //create n threads, where n is args[2]
        /*each thread will be passed a chunk size decided by args[1]
        thread1 gets thread size, and the chunk to process the stream
        */

        int chunkSize = Integer.parseInt(args[1]);
        //number of threads that will be in the pool
        ExecutorService exec = Executors.newFixedThreadPool(Integer.parseInt(args[2]));
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
                    while (scan.hasNextLine() && linesOfChunkRead++ <= chunkSize) {
                        chunk.add(scan.nextLine().trim());
                    }
                    if (linesOfChunkRead > chunkSize) {
                        exec.submit(new WordCountWorker(chunk));
                        linesOfChunkRead = 0;
                    }
                }
            }
            exec.shutdown();
        }
    }
}

