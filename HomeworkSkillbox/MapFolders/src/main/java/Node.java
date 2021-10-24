import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private String name;
    private List<Node> childrenNodes;

    public Node(String name) {
        this.name = name;
        childrenNodes = new ArrayList<>();
    }

    public void addChildrenNode(String children){
        childrenNodes.add(new Node(children));
    }

    public void addChildrenNode(Node children){
        childrenNodes.add(children);
    }

    public void print(){
        print(0);
    }

    private void print(int i){
        for(int j = 0; j < i; j++)
            System.out.print("\t");
        System.out.println(name);
        for (Node node : childrenNodes){
            node.print(i+1);
        }
    }
}
