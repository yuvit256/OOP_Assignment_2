import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Task<V> extends FutureTask<V> implements Callable<V>, Comparable<Task<V>> {
    private TaskType tt;
    private Callable<V> c;

    /**
     * private constrarctor because we were asked to create a constarctor called createTask but we
     * will use the private constractor to create an object from Task.
     */
    private Task(Callable<V> c, TaskType tt){
        super(c);
        this.c = c;
        this.tt = tt;
    }
    private Task(Callable<V> c){
        this(c,TaskType.COMPUTATIONAL);
    }
    /**
     * task with a generic return type
     * @return
     * @throws Exception
     */
    @Override
    public V call() throws Exception {
        return c.call();
    }

    /**
     *We just create a new task using the private constarctor and returning the Task
     */
    public static <V> Task<V> createTask(Callable<V> c, TaskType tt){
        Task<V> t = new Task<V>(c,tt);
        return t;
    }
    /**
     *We just create a new task using the private constarctor and returning the Task
     */
    public static <V> Task<V> createTask(Callable<V> c){
        Task<V> t = new Task<V>(c);
        return t;
    }
    public TaskType getTt() {
        return tt;
    }
    public void setTt(TaskType tt) {
        this.tt = tt;
    }
    public Callable<V> getC() {
        return c;
    }
    public void setC(Callable<V> c) {
        this.c = c;
    }

    /**
     * We were asked to make the Task comparable and we are doing so by
     *  implemting comparable and overridinig the function compareto by the priority of the Task
     */
    @Override
    public int compareTo(Task<V> other) {
        int prior = this.tt.getPriorityValue() - other.getTt().getPriorityValue();
        if(prior==0){ //same priority
            return 0;
        }else if(prior>0){ // the right one is more important
            return -1;
        }
        return 1;// the left one is more important
    }
}
