import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.Thread.sleep;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        Task<Integer> task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        Future<Integer> sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        Future<Double> priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        Future<String> reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }
    @Test
    public void myTest(){
        CustomExecutor ce = new CustomExecutor();
        Callable<String> callable1 = () -> {
            StringBuilder sb = new StringBuilder("Yuval and Maor");
            try{sleep(1500);}
            catch(InterruptedException e){e.printStackTrace();}
            return sb.reverse().toString();
        };
        Future<String> reverseTask = ce.submit(callable1, TaskType.IO);
        try{sleep(500);}
        catch(InterruptedException e){e.printStackTrace();}
        final AtomicInteger p1 = ce.getP1();
        final AtomicInteger p2 = ce.getP2();
        final AtomicInteger p3 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + p1 + "\nIO-Bound Tasks: " + p2 + "\nUnknown Tasks: " + p3);
        final String reversed;
        try {
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger p11 = ce.getP1();
        final AtomicInteger p22 = ce.getP2();
        final AtomicInteger p33 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + p11 + "\nIO-Bound Tasks: " + p22 + "\nUnknown Tasks: " + p33);


        Callable<Double> callable2 = () -> {
            double c = 0;
            for(int i=0; i<10; i++){
                c = c+1000 * Math.pow(1.02, 5);
            }
            try{sleep(1500);}
            catch(InterruptedException e){e.printStackTrace();}
            return c;
        };
        Future<Double> computTask = ce.submit(callable2, TaskType.COMPUTATIONAL);
        try{sleep(500);}
        catch(InterruptedException e){e.printStackTrace();}
        final AtomicInteger q1 = ce.getP1();
        final AtomicInteger q2 = ce.getP2();
        final AtomicInteger q3 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + q1 + "\nIO-Bound Tasks: " + q2 + "\nUnknown Tasks: " + q3);
        final double comTask;
        try {
            comTask = computTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger q11 = ce.getP1();
        final AtomicInteger q22 = ce.getP2();
        final AtomicInteger q33 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + q11 + "\nIO-Bound Tasks: " + q22 + "\nUnknown Tasks: " + q33);


        Callable<Integer> callable3 = () -> {
            int i = 0;
            while(i<100){
                i++;
            }
            try{sleep(1500);}
            catch(InterruptedException e){e.printStackTrace();}
            return i;
        };
        Future<Integer> otherTask = ce.submit(callable3, TaskType.OTHER);
        try{sleep(500);}
        catch(InterruptedException e){e.printStackTrace();}
        final AtomicInteger k1 = ce.getP1();
        final AtomicInteger k2 = ce.getP2();
        final AtomicInteger k3 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + k1 + "\nIO-Bound Tasks: " + k2 + "\nUnknown Tasks: " + k3);
        final int othTask;
        try {
            othTask = otherTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        final AtomicInteger k11 = ce.getP1();
        final AtomicInteger k22 = ce.getP2();
        final AtomicInteger k33 = ce.getP3();
        logger.info(() -> "Computational Tasks: " + k11 + "\nIO-Bound Tasks: " + k22 + "\nUnknown Tasks: " + k33);


        ce.gracefullyTerminate();
    }

}