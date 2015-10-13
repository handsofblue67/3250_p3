/**
 * Created by Michael on 10/12/2015.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class WordCount {
    public static void main(String[] args) {
        //args.length must be 3
        //create a treemap which all thread will add to, and will be printed out into the master output
        //create n threads, where n is args[2]
        /*each thread will be passed a chunk size decided by args[1]
        thread1 gets thread size, and the chunk to process the stream
        */

        int numTasks = Integer.parseInt(args[1]);
        //number of threads that will be in the pool
        ExecutorService tpes = Executors.newFixedThreadPool(Integer.parseInt(args[2]));
        File dir = new File(args[0]);
        File[] fileList = dir.listFiles();
        BufferedReader br = null;
        boolean endOfFile = false;
        //create an array of WordCountWorker thread objects
        ArrayList<WordCountWorker> workerList = new ArrayList<>();

        for (int i = 0; i < Integer.parseInt(args[2]); ++i) {
            workerList.add(new WordCountWorker(br, Integer.parseInt(args[1])));
        }

        for (int i = 0; i < fileList.length; ++i) {
            File file = fileList[i];
            if(file.isFile()) {
                try {
                    br = new BufferedReader(new FileReader(file.getName()));
                    while(!endOfFile) {
                        //create each thread
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }finally {
                    br.close();
                }


            }
        }

    }

}
