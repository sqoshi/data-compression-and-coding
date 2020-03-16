public class TreeBuilder {
    Node root;
    int alphabetLength;
    int symbolsQuantity;

    public TreeBuilder(DataCollector dataCollector) {
        alphabetLength = dataCollector.getMapOfSymbols().size();
        symbolsQuantity = dataCollector.getTextInBytes().length;
        int N = 2 * alphabetLength + 1;
        root = new Node("NYT", 0, N);//NYT
        build(dataCollector);
        sumWeights(findParent(root, "NYT"));
        printBinaryTree(root, 0);
        performSwaps(findParent(root, "NYT"));
        System.out.println();
        //swapsUpDown(root);
        printBinaryTree(root, 0);
        StringBuilder stb = new StringBuilder();
        String c = "d";
        findPath(findParent(root, c), findNode(root, c), stb);


    }

    public static void printBinaryTree(Node root, int level) {
        if (root == null)
            return;
        printBinaryTree(root.right, level + 1);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++)
                System.out.print("|\t\t\t\t\t\t");
            System.out.println("|-----------------------(" + root.name + ") W: " + root.weight + ", N: " + root.N);
        } else
            System.out.println("(" + root.name + ") W: " + root.weight + ", N: " + root.N);
        printBinaryTree(root.left, level + 1);
    }

    public void findPath(Node parent, Node child, StringBuilder stb) {
        if (parent != null)
            if (parent.N <= 2 * alphabetLength + 1) {
                if (parent.left.name.equals(child.name)) {
                    stb.append("0");
                } else if (parent.right.name.equals(child.name)) {
                    stb.append("1");
                }
                if (parent.N != 2 * alphabetLength + 1)
                    findPath(findParent(root, parent), parent, stb);
                if (parent.N == 2 * alphabetLength + 1)
                    System.out.println(stb.toString());
            }

    }

    public void swapsUpDown(Node parent) {
        if (parent.right != null) {
            if (parent.left.weight > parent.right.weight) {
                Node temp;
                temp = parent.left;
                parent.left = parent.right;
                parent.right = temp;
                parent.left.N -= 1;
                parent.right.N += 1;
            }
            swapsUpDown(parent.left);
            swapsUpDown(parent.right);
        }
    }

    public void performSwaps(Node parent) {
        if (parent.N <= 2 * alphabetLength + 1) {
                if (parent.left.weight >= parent.right.weight && parent.left.N>3) {
                    System.out.println(parent.left.N + " swap " + parent.right.N);
                    Node temp;
                    temp = parent.left;
                    parent.left = parent.right;
                    parent.right = temp;
                    parent.left.N -= 1;
                    parent.right.N += 1;

                }
            if (parent.N != 2 * alphabetLength + 1)
                performSwaps(findParent(root, parent));

        }
    }

    public void sumWeights(Node parent) {
        if (parent.N <= 2 * alphabetLength + 1) {
            parent.weight = parent.left.weight + parent.right.weight;
            if (parent.N != 2 * alphabetLength + 1)
                sumWeights(findParent(root, parent));
        }
    }

    private void build(DataCollector dataCollector) {
        for (int i = 0; i < dataCollector.getTextInBytes().length; i++) {
            chooseInsertion(String.valueOf((char) (dataCollector.getTextInBytes()[i])));
        }
    }

    public boolean containsNode(String value) {
        return containsNodeRecursive(root, value);
    }

    private boolean containsNodeRecursive(Node current, String value) {
        if (current == null) {
            return false;
        }
        if (value.equals(current.name)) {
            return true;
        }
        if (current.right != null && value.equals(current.right.name)) {
            System.out.println(current.right.N);
            return true;
        }
        return containsNodeRecursive(current.left, value);
    }

    private void chooseInsertion(String name) {
        if (!containsNode(name)) {
            insertElement(root, name);
        } else if (containsNode(name)) {
            Node node = findNode(root, name);
            if (node.name.equals(name))
                node.weight = node.weight + 1;
        }
    }

    private Node findParent(Node root, String query) {
        if (root == null) return null;

        // Found it!
        if ((root.left != null && root.right != null)
                && (root.left.name.equals(query)
                || root.right.name.equals(query))) {
            System.out.println(root.name + " " + root.N);
            return root;
        }

        Node leftResult = findParent(root.left, query);
        if (leftResult != null) return leftResult;
        Node rightResult = findParent(root.right, query);
        if (rightResult != null) return rightResult;
        return null;
    }

    private Node findParent(Node root, Node query) {
        if (root == null) return null;

        // Found it!
        if ((root.left != null && root.right != null)
                && (root.left.equals(query)
                || root.right.equals(query))) {
            System.out.println(root.name + " " + root.N);
            return root;
        }

        Node leftResult = findParent(root.left, query);
        if (leftResult != null) return leftResult;
        Node rightResult = findParent(root.right, query);
        if (rightResult != null) return rightResult;
        return null;
    }

    private Node findNode(Node root, String query) {
        // nothing to search, not found
        if (root == null) return null;

        // Found it!
        if (root.name.equals(query)) return root;

        // This is when we recurse left, stop if we found a result
        Node leftResult = findNode(root.left, query);
        if (leftResult != null) return leftResult;
        // else try to the right, stop if we found a result
        Node rightResult = findNode(root.right, query);
        if (rightResult != null) return rightResult;
        return null;
    }


    private void insertElement(Node current, String name) {
        if (current.N - 2 > 0) {
            if (current.name.equals("NYT")) {
                current.left = new Node("NYT", 0, current.N - 2);
                current.right = new Node(name, 1, current.N - 1);
                current.name = "empty";
            } else {
                current = current.left;
                insertElement(current, name);
            }
        }
    }

    public String convertByteTo8bitStr(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        int val = b;
        for (int i = 0; i < 8; i++) {
            stringBuilder.append((val & 128) == 0 ? 0 : 1);
            val <<= 1;
        }
        return stringBuilder.toString();
    }

    public class Node {
        String name;
        int weight;
        int N;
        Node left;
        Node right;

        Node(String name, int weight, int N) {
            this.name = name;
            this.weight = weight;
            this.N = N;
            right = null;
            left = null;
        }
    }
}
