import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String[] args)
    {
        ForkJoinPool pool = new ForkJoinPool();
        FolderProcessor system = new FolderProcessor("d:\\");
        pool.execute(system);

       while (!system.isDone()){}

        pool.shutdown();

        Node result = system.join();
        result.print();

    }
}