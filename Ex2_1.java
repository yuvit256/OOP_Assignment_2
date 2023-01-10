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
                for (int j = 0; j < bound; j++) {
                    writer.write(Integer.toString(rand.nextInt(bound)) + "Yuval and Maor" + "\n");
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
        final AtomicInteger numLines = new AtomicInteger(0);

        for (final String fileName : fileNames) {
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    while (reader.readLine() != null) {
                        numLines.incrementAndGet();
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the file: " + fileName);
                    e.printStackTrace();
                }
            }).start();
        }

        return numLines.get();
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
}


