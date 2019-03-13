package me.trilis.au.java.hw3;

import java.util.*;

/**
 * Implementation of set using binary search tree.
 *
 * @param <E> the type of elements in this set.
 */
public class TreeSet<E> extends AbstractSet<E> implements MyTreeSet<E> {

    /** Creates empty set with natural ordering as comparator. */
    @SuppressWarnings("unchecked")
    public TreeSet() {
        comparator = (o1, o2) -> ((Comparable<? super E>) o1).compareTo(o2);
    }

    /**
     * Creates empty set with the specified comparator.
     *
     * @param comparator the specified comparator.
     */
    public TreeSet(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    /** @inheritDoc */
    @Override
    public boolean add(E e) {
        var currentNode = root;
        if (currentNode == null) {
            root = new Node(e, null);
            size++;
            return true;
        }
        while (true) {
            if (comparator.compare(e, currentNode.value) < 0) {
                if (currentNode.left == null) {
                    currentNode.left = new Node(e,  currentNode);
                    size++;
                    return true;
                }
                currentNode = currentNode.left;
            } else if (comparator.compare(e, currentNode.value) > 0) {
                if (currentNode.right == null) {
                    currentNode.right = new Node(e,  currentNode);
                    size++;
                    return true;
                }
                currentNode = currentNode.right;
            } else {
                return false;
            }
        }
    }

    /** @inheritDoc */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        var node = find(root, (E) o);
        if (node == null || !node.value.equals(o)) {
            return false;
        }
        root = remove(root, (E) o);
        size--;
        return true;
    }

    /** @inheritDoc */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {

            /** @inheritDoc */
            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            /** @inheritDoc */
            @Override
            public E next() {
                if (currentNode == null) {
                    throw new NoSuchElementException();
                }
                var value = currentNode.value;
                currentNode = nextNode(currentNode);
                return value;
            }

            private Node currentNode = leftmostNode();

        };
    }

    /** @inheritDoc */
    @Override
    public int size() {
        return size;
    }

    /** @inheritDoc */
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public E next() {
                if (currentNode == null) {
                    throw new NoSuchElementException();
                }
                var value = currentNode.value;
                currentNode = previousNode(currentNode);
                return value;
            }

            private Node currentNode = rightmostNode();

        };
    }

    /** @inheritDoc */
    @Override
    public MyTreeSet<E> descendingSet() {
        return descendingSet;
    }

    /** @inheritDoc */
    @Override
    public E first() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return leftmostNode().value;
    }

    /** @inheritDoc */
    @Override
    public E last() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return rightmostNode().value;
    }

    /** @inheritDoc */
    @Override
    public E lower(E e) {
        var node = find(root, e);
        if (node == null) {
            return null;
        }
        if (comparator.compare(node.value, e) < 0) {
            return node.value;
        }
        var previousNode = previousNode(node);
        if (previousNode == null) {
            return null;
        }
        return previousNode.value;
    }

    /** @inheritDoc */
    @Override
    public E floor(E e) {
        var node = find(root, e);
        if (node == null) {
            return null;
        }
        if (comparator.compare(node.value, e) <= 0) {
            return node.value;
        }
        var previousNode = previousNode(node);
        if (previousNode == null) {
            return null;
        }
        return previousNode.value;
    }

    /** @inheritDoc */
    @Override
    public E ceiling(E e) {
        var node = find(root, e);
        if (node == null) {
            return null;
        }
        if (comparator.compare(node.value, e) >= 0) {
            return node.value;
        }
        var nextNode = nextNode(node);
        if (nextNode == null) {
            return null;
        }
        return nextNode.value;
    }

    /** @inheritDoc */
    @Override
    public E higher(E e) {
        var node = find(root, e);
        if (node == null) {
            return null;
        }
        if (comparator.compare(node.value, e) > 0) {
            return node.value;
        }
        var nextNode = nextNode(node);
        if (nextNode == null) {
            return null;
        }
        return nextNode.value;
    }


    private Node leftmostNode() {
        var currentNode = root;
        while (currentNode.left != null) {
            currentNode = currentNode.left;
        }
        return currentNode;
    }

    private Node rightmostNode() {
        var currentNode = root;
        while (currentNode.right != null) {
            currentNode = currentNode.right;
        }
        return currentNode;
    }

    private Node find(Node currentNode, E e) {
        if (currentNode == null) {
            return null;
        }
        if (comparator.compare(e, currentNode.value) < 0) {
            if (currentNode.left != null) {
                return find(currentNode.left, e);
            }
        } else if (comparator.compare(e, currentNode.value) > 0) {
            if (currentNode.right != null) {
                return find(currentNode.right, e);
            }
        }
        return currentNode;
    }

    private Node nextNode(Node currentNode) {
        if (currentNode.right != null) {
            currentNode = currentNode.right;
            while (currentNode.left != null) {
                currentNode = currentNode.left;
            }
        } else {
            while (currentNode.parent != null && currentNode == currentNode.parent.right) {
                currentNode = currentNode.parent;
            }
            currentNode = currentNode.parent;
        }
        return currentNode;
    }

    private Node previousNode(Node currentNode) {
        if (currentNode.left != null) {
            currentNode = currentNode.left;
            while (currentNode.right != null) {
                currentNode = currentNode.right;
            }
        } else {
            while (currentNode.parent != null && currentNode == currentNode.parent.left) {
                currentNode = currentNode.parent;
            }
            currentNode = currentNode.parent;
        }
        return currentNode;
    }

    private Node remove(Node node, E e) {
        if (node == null) {
            return null;
        }
        if (comparator.compare(e, node.value) < 0) {
            var newNode = remove(node.left, e);
            node.left = newNode;
            if (newNode != null) {
                newNode.parent = node;
            }
            return node;
        } else if (comparator.compare(e, node.value) > 0) {
            var newNode = remove(node.right, e);
            node.right = newNode;
            if (newNode != null) {
                newNode.parent = node;
            }
            return node;
        }
        if (node.left == null) {
            return node.right;
        }
        if (node.right == null) {
            return node.left;
        }
        node.value = nextNode(node).value;
        var newNode = remove(node.right, node.value);
        node.right = newNode;
        if (newNode != null) {
            newNode.parent = node;
        }
        node.right = remove(node.right, node.value);
        return node;
    }

    private Node root = null;
    private final Comparator<? super E> comparator;
    private int size = 0;
    private final DescendingSet descendingSet = new DescendingSet();

    private class Node {
        private E value;
        private Node left = null;
        private Node right = null;
        private Node parent;

        private Node(E value, Node parent) {
            this.value = value;
            this.parent = parent;
        }
    }

    private class DescendingSet extends AbstractSet<E> implements MyTreeSet<E> {

        /** @inheritDoc */
        @Override
        public Iterator<E> iterator() {
            return TreeSet.this.descendingIterator();
        }

        /** @inheritDoc */
        @Override
        public int size() {
            return TreeSet.this.size();
        }

        /** @inheritDoc */
        @Override
        public Iterator<E> descendingIterator() {
            return TreeSet.this.iterator();
        }

        /** @inheritDoc */
        @Override
        public MyTreeSet<E> descendingSet() {
            return TreeSet.this;
        }

        /** @inheritDoc */
        @Override
        public E first() {
            return TreeSet.this.last();
        }

        /** @inheritDoc */
        @Override
        public E last() {
            return TreeSet.this.first();
        }

        /** @inheritDoc */
        @Override
        public E lower(E e) {
            return TreeSet.this.higher(e);
        }

        /** @inheritDoc */
        @Override
        public E floor(E e) {
            return TreeSet.this.ceiling(e);
        }

        /** @inheritDoc */
        @Override
        public E ceiling(E e) {
            return TreeSet.this.floor(e);
        }

        /** @inheritDoc */
        @Override
        public E higher(E e) {
            return TreeSet.this.lower(e);
        }

        /** @inheritDoc */
        @Override
        public boolean add(E e) {
            return TreeSet.this.add(e);
        }

        /** @inheritDoc */
        @Override
        public boolean remove(Object o) {
            return TreeSet.this.remove(o);
        }

    }

}