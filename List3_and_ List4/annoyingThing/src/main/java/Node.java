public class Node {
    private int weight;
    private int N;
    private String name;

    private Node left;
    private Node right;
    private Node parent;

    /**
     * leaf
     *
     * @param N id
     * @param name concrete 8bit pass
     */
    public Node(int N, String name) {
        this.weight = 1;
        this.N = N;
        this.name = name;

        this.left = null;
        this.right = null;
    }

    /**
     * not leaf
     *
     * @param left lchild
     * @param right rchild
     */
    public Node(int N, Node left, Node right) {
        this.weight = left.weight + right.weight;
        this.N = N;
        this.name = null;

        this.left = left;
        this.right = right;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
