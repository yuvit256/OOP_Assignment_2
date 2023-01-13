import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomExecutor extends ThreadPoolExecutor{
    private AtomicInteger p1;
    private AtomicInteger p2;
    private AtomicInteger p3;

    static int cores = Runtime.getRuntime().availableProcessors(); // the amount of cores

    /**
     * The constractor we use super because we extends ThreadPoolExecutor so we use his constractor
     * and just zeroing the AtomicIntegers
     */
    public CustomExecutor(){
        super(cores/2, cores-1, 300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        p1 = new AtomicInteger(0);
        p2 = new AtomicInteger(0);
        p3 = new AtomicInteger(0);
    }
    public AtomicInteger getP1() {
        return p1;
    }
    public AtomicInteger getP2() {
        return p2;
    }
    public AtomicInteger getP3() {
        return p3;
    }

    /**
     * We updates the counters for the Task type in the PriortyBlockingQueue
     */
    private void update (int type){
        if(type == 1){
            p1.incrementAndGet();
        }
        if(type == 2){
            p2.incrementAndGet();
        }
        if(type == 3){
            p3.incrementAndGet();
        }
    }

    /**
     *  We were asked to submit the task into the PriortyBlockingQueue and we are doing so by
     *  checking first if the Task isnt null and then we want to update the counters and we are adding
     *  the task to the pool by the function excute witch return a future val.
     */
    public <V> Future<V> submit(Task<V> t){
        update (t.getTt().getPriorityValue());
        if (t == null) throw new NullPointerException();
        execute(t);
        return t;
    }
    public <V> Future<V> submit(Callable <V> c, TaskType tt){
        Task<V> t = Task.createTask(c, tt);
        return submit(t);
    }
    @Override
    public <V> Future<V> submit(Callable <V> c){
        Task<V> t = Task.createTask(c);
        return submit(t);
    }

    /**
     * We were asked to get the current max priority task in the priorityQueue and we are doing so
     * by checking the counters
     */
    public int getCurrentMax(){
        if(p1.get()>0){return 1;}
        else if(p2.get()>0){return 2;}
        else if(p3.get()>0){return 3;}
        return 0;
    }

    /**
     * we override this function because we want after we done with the mission to decrece the counters
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        int currMax = getCurrentMax();
        if(currMax == 1){
            p1.decrementAndGet();
        } else if (currMax == 2) {
            p2.decrementAndGet();
        }else if(currMax == 3){
            p3.decrementAndGet();
        }
    }

    /**
     * we were asked to stop the instance of CustomExecutor by not letting another Tasks to the Queue
     * to finish all the Tasks in the Queue and finishing all the Tasks in the Threadpool
     */
    public void gracefullyTerminate() {
        try{
            awaitTermination(1, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        shutdown();
    }
}
