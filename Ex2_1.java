import java.io.*;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Ex2_1 {
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
    public static int getNumOfLinesThreadPool(String[] fileNames)
    {
        AtomicInteger lineCount = new AtomicInteger(0);
        ExecutorService pool = Executors.newFixedThreadPool(fileNames.length);
        for (final String fileName : fileNames)
        {
            ThreadCallable tc = new ThreadCallable(fileName);
            try{
                lineCount.addAndGet((int)tc.call());
            }catch (Exception e){
                e.printStackTrace();
            }
            Future<Void> future = pool.submit(tc);
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