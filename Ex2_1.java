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
    public static int getNumOfLinesThreads(String[] fileNames) {
        AtomicInteger numOfLines = new AtomicInteger();
        Thread[] threads = new Thread[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            final String fileName = fileNames[i];
            threads[i] = new Thread(() -> {
                try {
                    File file = new File(fileName);
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    while (reader.readLine() != null) {
                        numOfLines.incrementAndGet();
                    }
                    reader.close();
                } catch (IOException e) {
                    System.err.println("An IOException occurred while reading the file " + fileName);
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to finish before returning the result
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("A thread was interrupted while waiting to finish.");
                e.printStackTrace();
            }
        }

        return numOfLines.get();
    }
    public static int getNumOfLinesThreadPool(String[] fileNames) throws Exception {
        final AtomicInteger numLines = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(fileNames.length);

        for (final String fileName : fileNames) {
            Callable<Void> task = () -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    while (reader.readLine() != null) {
                        numLines.incrementAndGet();
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the file: " + fileName);
                    e.printStackTrace();
                }
                return null;
            };

            Future<Void> future = executor.submit(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(500);
        }

        return numLines.get();
    }
    public static void main(String [] args){
        String [] arr = createTextFiles(100, 69, 96);
        int lines1 = getNumOfLines(arr);
        int lines2 = getNumOfLinesThreads(arr);
        try{
            int lines3 = getNumOfLinesThreadPool(arr);
            System.out.println("Lines without using threads: " + lines1 + "\n" +
                               "Lines using threads: " + lines2 + "\n" +
                               "Lines using threadsPool: " + lines3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


