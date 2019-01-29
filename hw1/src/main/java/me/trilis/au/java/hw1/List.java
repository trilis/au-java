package me.trilis.au.java.hw1;

import java.util.Iterator;

/**
 * Implementation of doubly linked list with elements of the specified
 * type {@code T}.
 *
 * @param <T> the specified type.
 */
public class List<T> implements Iterable<T> {

    private ListNode headNode = null;
    private ListNode tailNode = null;

    /**
     * Adds the specified element of type {@code T} to the end of this list.
     *
     * @param value the specified element.
     */
    public void add(T value) {
        if (tailNode == null) {
            tailNode = headNode = new ListNode(value, null, null);
        } else {
            tailNode.nextNode = new ListNode(value, tailNode, null);
            tailNode = tailNode.nextNode;
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}, which
     * can additionally remove the element it points to.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            ListNode currentNode = new ListNode(null, null, headNode);

            @Override
            public boolean hasNext() {
                return currentNode.nextNode != null;
            }

            @Override
            public T next() {
                currentNode = currentNode.nextNode;
                if (currentNode == null) {
                    return null;
                }
                return currentNode.value;
            }

            /**
             * Removes the element this iterator points to.
             */
            public void remove() {
                if (currentNode.nextNode == null) {
                    if (currentNode.previousNode == null) {
                        headNode = tailNode = null;
                    } else {
                        currentNode.previousNode.nextNode = null;
                        tailNode = currentNode.previousNode;
                    }
                } else if (currentNode.previousNode == null) {
                    currentNode.nextNode.previousNode = null;
                    headNode = currentNode.nextNode;
                } else {
                    currentNode.nextNode.previousNode = currentNode.previousNode;
                    currentNode.previousNode.nextNode = currentNode.nextNode;
                }
            }

        };
    }

    private class ListNode {
        private final T value;
        private ListNode previousNode, nextNode;

        private ListNode(T value, ListNode previousNode, ListNode nextNode) {
            this.value = value;
            this.previousNode = previousNode;
            this.nextNode = nextNode;
        }
    }


}
