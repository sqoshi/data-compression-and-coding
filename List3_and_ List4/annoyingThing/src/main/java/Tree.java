import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private Node root;
    private Node NYT;
    private int nextN;
    private Map<String, Node> nodesMap = new HashMap();
    private Map<Integer, List<Node>> nodesWeightsGroups = new HashMap();

    public Tree(Map<String, Node> nodesMap) {
        Node NYT = new Node(255, "");
        NYT.setWeight(0);
        this.NYT = NYT;
        this.root = NYT;
        this.nextN = 254;
    }

    private Node insertNode(String name) {
        Node newNode = new Node(this.nextN, name);
        Node newParent = new Node(this.NYT.getN(), this.NYT, newNode);
        this.nextN -= 2;
        this.NYT.setN(newParent.getN() - 2);
        newNode.setParent(newParent);


        if (this.NYT.getParent() != null) {
            if (this.NYT.getParent().getLeft() == this.NYT) {
                this.NYT.getParent().setLeft(newParent);
                newParent.setParent(this.NYT.getParent());
            } else if (this.NYT.getParent().getRight() == this.NYT) {
                newParent.setParent(this.NYT.getParent());
                this.NYT.getParent().setRight(newParent);
            }
        } else this.root = newParent;


        this.NYT.setParent(newParent);
        this.nodesMap.put(name, newNode);


        return newNode;
    }

    private void swapNodes(Node n1, Node n2) {
        this.swapN(n1, n2);
        this.swapParents(n1, n2);
    }

    private void swapN(Node n1, Node n2) {
        int temp = n1.getN();
        n1.setN(n2.getN());
        n2.setN(temp);
    }

    private void swapParents(Node n1, Node n2) {
        Node p1 = n1.getParent();
        Node p2 = n2.getParent();
        n1.setParent(p2);
        n2.setParent(p1);

        //additive
        if (p1.getRight() == n1) {
            p1.setRight(n2);
        } else if (p1.getLeft() == n1) {
            p1.setLeft(n2);
        }

        if (p2.getRight() == n2) {
            p2.setRight(n1);
        } else if (p2.getLeft() == n2) {
            p1.setLeft(n1);
        }
    }

    public Node getNextNode(Node previous, int ind) {
        Node node;
        if (ind == 0) node = previous.getLeft();
        else node = previous.getRight();
        return node;
    }
}
