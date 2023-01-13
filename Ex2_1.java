import java.io.*;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Ex2_1 {

    /**
     * This function creates an array of n text files, with names "file0.txt", "file1.txt", etc.
     * It uses a given seed to initialize a Random object,
     * and then uses that object to generate random integers between 0 and bound-1.
     * For each file, it creates a FileWriter object,
     * and then writes a line of text consisting of the string "Yuval and Maor "
     * followed by a random integer between 0 and bound-1, for a number of times determined by a random integer.
     * If any IOExceptions are thrown while creating or writing to the files,
     * it will print an error message and the stack trace of the exception.
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] fileNames = new String[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            fileNames[i] = "file" + i + ".txt";
            File file = new File(fileNames[i]);
            try (FileWriter writer = new FileWriter(file)) {
                for (int j = 0; j < rand.nextInt(bound); j++) {
                    writer.write( "Yuval and Maor " + Integer.toString(rand.nextInt(bound)) + "\n");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating the text file.");
                e.printStackTrace();
            }
        }
        return fileNames;
    }
    /**
     * This is a method that takes an array of file names as input and returns the total number of lines across all the files.
     * For each file name in the input array, it creates a BufferedReader object and uses the readLine() method to read each line of the file.
     * Each time a line is read, the lineCount variable is incremented. If any IOExceptions are thrown while reading the files,
     * it will print an error message and the stack trace of the exception. Finally, it returns the total number of lines,
     * which is the value of the lineCount variable.
     */
    public static int getNumOfLines(String[] fileNames) {
        int lineCount = 0;

        for (String fileName : fileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                while (reader.readLine() != null) {
                    lineCount++;
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }

        return lineCount;
    }
    /**
     * This method is similar to the  method we made earlier: getNumOfLines, but it uses threads to count the number of lines across multiple files.
     * It first creates an array of ExtendThread objects, one for each file name in the input array. For each file name,
     * it starts a new thread and assigns the task of counting the number of lines in that file to it.
     *
     * The threads are started one after another with a sleep of 10ms between each start, so it's not parallel.
     *
     * The method then waits for all the threads to finish executing by calling the join() method on each thread in the array.
     * Once all the threads have finished, it adds up the number of lines counted by each thread and returns the total.
     * If any InterruptedExceptions are thrown while waiting for the threads to finish,
     * it will print an error message and the stack trace of the exception.
     */
    public static int getNumOfLinesThreads(String[] fileNames)
    {
        int lineCount = 0;
        ExtendThread [] threads = new ExtendThread [fileNames.length];
        for(int i=0; i<fileNames.length; i++)
        {
            final String fileName = fileNames[i];
            threads[i] = new ExtendThread(fileName);
            threads[i].start();
            try {
                threads[i].sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            lineCount += threads[i].getNumLine().get();
        }

        for (ExtendThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("A thread was interrupted while waiting to finish.");
                e.printStackTrace();
            }
        }
        return lineCount;
    }
    /**
     * This method is also similar to the previous method getNumOfLines we made,
     * but it uses a thread pool to count the number of lines across multiple files.
     * It creates an AtomicInteger variable lineCount to store the total number of lines and an ExecutorService object
     * that represents a thread pool with a fixed number of threads equal to the number of files.
     *
     * It creates an array of Future objects, one for each file name.
     * It then submits a ThreadCallable task which counts the number of lines in the file to the thread pool, passing the file name as an argument.
     * It also tries to get the result of the task, if it throws an exception it will print the stack trace.
     *
     * It then shutdowns the thread pool and waits for all the threads to complete by checking the status of the thread pool using isTerminated() method
     * and sleep for 500ms if it's not terminated yet.
     *
     * Finally, it returns the total number of lines counted by all threads, which is the value of the lineCount variable.
     */
    public static int getNumOfLinesThreadPool(String[] fileNames)
    {
        AtomicInteger lineCount = new AtomicInteger(0);
        ExecutorService pool = Executors.newFixedThreadPool(fileNames.length);
        Future<Integer> [] futures = new Future[fileNames.length];
        for (int i=0; i<fileNames.length; i++)
        {
            String fileName = fileNames[i];
            ThreadCallable tc = new ThreadCallable(fileName);
            try{
                lineCount.addAndGet((int)tc.call());
            }catch (Exception e){
                e.printStackTrace();
            }
            futures[i] = pool.submit(tc);
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return lineCount.get();
    }
    public static void main(String [] args){
        String [] fileNames = createTextFiles(100, 70, 96);
        int lines1 = getNumOfLines(fileNames);
        int lines2 = getNumOfLinesThreads(fileNames);
        try{
            int lines3 = getNumOfLinesThreadPool(fileNames);
            System.out.println("Lines without using threads: " + lines1 + "\n" +
                               "Lines using threads: " + lines2 + "\n" +
                               "Lines using threadsPool: " + lines3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}