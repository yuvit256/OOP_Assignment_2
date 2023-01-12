import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomExecutor extends ThreadPoolExecutor{
    private AtomicInteger p1;
    private AtomicInteger p2;
    private AtomicInteger p3;

    static int cores = Runtime.getRuntime().availableProcessors();
    public CustomExecutor(){
        super(cores/2, cores-1, 300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        p1 = new AtomicInteger(0);
        p2 = new AtomicInteger(0);
        p3 = new AtomicInteger(0);
    }

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

    public int getCurrentMax(){
        if(p1.get()>0){return 1;}
        else if(p2.get()>0){return 2;}
        else if(p3.get()>0){return 3;}
        return 0;
    }
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        int currMax = getCurrentMax();
        if(currMax == 1){
            p1.decrementAndGet();
        }
        if(currMax == 2){
            p2.decrementAndGet();
        }
        if(currMax == 3){
            p3.decrementAndGet();
        }
    }

    public void gracefullyTerminate() {
        try{
            awaitTermination(1, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        shutdown();
    }
}
