import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderProcessor extends RecursiveTask<Node> {
    private final String path;
    private Node node;

    public FolderProcessor(String path) {
        this.path = path;
        node = new Node(path);
    }

    @Override
    protected Node compute() {

        List<FolderProcessor> tasks = new ArrayList<FolderProcessor>();

        File folder = new File(path);
        File files[] = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //node.addChildrenNode(file.getAbsolutePath());
                    FolderProcessor task = new FolderProcessor(file.getAbsolutePath());
                    task.fork();
                    tasks.add(task);
                }
            }
        }

        System.out.printf("%s: %d tasks ran.\n", folder.getAbsolutePath(), tasks.size());

        addResultsFromTasks(node, tasks);
        return node;
    }

    private void addResultsFromTasks(Node node, List<FolderProcessor> tasks) {
        for (FolderProcessor item : tasks) {
            node.addChildrenNode(item.join());
        }
    }
}