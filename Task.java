import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Task<V> extends FutureTask<V> implements Callable<V>, Comparable<Task<V>> {
    private TaskType tt;
    private Callable<V> c;

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

    public static <V> Task<V> createTask(Callable<V> c, TaskType tt){
        Task<V> t = new Task<V>(c,tt);
        return t;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task<?> task = (Task<?>) o;
        return tt == task.tt && c.equals(task.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tt, c);
    }
}
