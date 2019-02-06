package trilis.me.au.java.hw2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Implementation of trie data structure for strings.
 */
public class Trie implements Serializable {

    private class Node {
        private final HashMap<Character, Node> children = new HashMap<>();
        private Node parent;
        private boolean isTerminal = false;
        private int size = 0;

        private Node() {
        }

        private Node(Node parent) {
            this.parent = parent;
        }

        private void updateSize(int change) {
            size += change;
            if (parent != null) {
                parent.updateSize(change);
            }
        }

        private StringBuilder serialize(StringBuilder output) {
            output.append(children.size());
            output.append(' ');
            if (isTerminal) {
                output.append(1);
            } else {
                output.append(0);
            }
            output.append(' ');
            for (var child : children.entrySet()) {
                output.append((int) child.getKey());
                output.append(' ');
                child.getValue().serialize(output);
            }
            return output;
        }

        private void deserialize(Scanner scanner) {
            var newSize = scanner.nextInt();
            int terminal = scanner.nextInt();
            if (terminal == 1) {
                isTerminal = true;
            } else if (terminal == 0) {
                isTerminal = false;
            } else {
                throw new IllegalStateException();
            }
            for (int i = 0; i < newSize; i++) {
                Node child = new Node(this);
                children.put((char) scanner.nextInt(), child);
                child.deserialize(scanner);
            }
        }
    }

    private Node traverse(String element) {
        var currentNode = root;
        for (int i = 0; i < element.length(); i++) {
            if (currentNode.children.containsKey(element.charAt(i))) {
                currentNode = currentNode.children.get(element.charAt(i));
            } else {
                return null;
            }
        }
        return currentNode;
    }

    /**
     * Adds the specified string to this trie.
     *
     * @param element the specified string.
     * @return true if this trie does not contain this string before,
     * false otherwise.
     */
    public boolean add(String element) {
        var currentNode = root;
        for (int i = 0; i < element.length(); i++) {
            if (currentNode.children.containsKey(element.charAt(i))) {
                currentNode = currentNode.children.get(element.charAt(i));
            } else {
                Node newNode = new Node(currentNode);
                currentNode.children.put(element.charAt(i), newNode);
                currentNode = newNode;
            }
        }
        var wasTerminal = currentNode.isTerminal;
        if (!wasTerminal) {
            currentNode.updateSize(1);
        }
        currentNode.isTerminal = true;
        return !wasTerminal;
    }

    /**
     * Checks if this trie contains the specified string.
     *
     * @param element the specified string.
     * @return true if this trie contains the specified string
     * and false otherwise.
     */
    public boolean contains(String element) {
        var currentNode = traverse(element);
        if (currentNode == null) {
            return false;
        }
        return currentNode.isTerminal;
    }

    /**
     * Removes the specified string from this trie.
     *
     * @param element the specified string.
     * @return true if this trie contained this string before,
     * false otherwise.
     */
    public boolean remove(String element) {
        var currentNode = traverse(element);
        if (currentNode == null) {
            return false;
        }
        var wasTerminal = currentNode.isTerminal;
        if (wasTerminal) {
            currentNode.updateSize(-1);
        }
        currentNode.isTerminal = false;
        return wasTerminal;
    }

    /**
     * Counts number of strings in this trie starting with the specified prefix.
     *
     * @param prefix the specified prefix.
     * @return number of strings in this trie starting with the specified prefix.
     */
    public int howManyStartWithPrefix(String prefix) {
        var currentNode = traverse(prefix);
        if (currentNode == null) {
            return 0;
        }
        return currentNode.size;
    }

    /**
     * Returns the size of this trie.
     *
     * @return the size of this trie.
     */
    public int size() {
        return root.size;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void serialize(OutputStream out) throws IOException {
        out.write(root.serialize(new StringBuilder()).toString().getBytes());
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("RedundantThrows")
    public void deserialize(InputStream in) throws IOException {
        var scanner = new Scanner(in);
        var newRoot = new Node();
        newRoot.deserialize(scanner);
        root = newRoot;
    }

    private Node root = new Node();

}
