/**
 * Created by Michael on 10/12/2015.
 */
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
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
        FileReader reader = null;

        for (int i = 0; i < fileList.length; ++i) {
            File file = fileList[i];
            if(file.isFile()) {
                try {
                    reader = new FileReader(new File(file.getName()));
                    while(reader.hasNext()) {

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    FileReader.close();
                }


            }
        }

    }

    }
}
