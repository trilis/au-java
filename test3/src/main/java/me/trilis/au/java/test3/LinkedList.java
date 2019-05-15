package me.trilis.au.java.test3;

import java.util.*;

/**
 * Implementation of linked list with elements of the specified
 * type {@code T}.
 *
 * @param <T> the specified type.
 */
public class LinkedList<T> extends AbstractSet<T> implements Set<T> {
    private ListNode headNode = null;
    private ListNode tailNode = null;

    /**
     * {@inheritDoc}
     */
    public boolean add(T value) {
        if (tailNode == null) {
            tailNode = headNode = new ListNode(value, null, null);
        } else {
            tailNode.nextNode = new ListNode(value, tailNode, null);
            tailNode = tailNode.nextNode;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private ListNode currentNode = new ListNode(null, null, headNode);
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasNext() {
                return currentNode.nextNode != null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public T next() {
                currentNode = currentNode.nextNode;
                if (currentNode == null) {
                    throw new NoSuchElementException();
                }
                return currentNode.value;
            }

            /**
             * {@inheritDoc}
             */
            @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        int i = 0;
        for (T ignored : this) {
            i++;
        }
        return i;
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