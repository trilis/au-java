package me.trilis.au.java.hw1;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of doubly linked list with elements of the specified
 * type {@code T}.
 *
 * @param <T> the specified type.
 */
public class LinkedList<T> extends AbstractList<T> {
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
    public T remove(int index) {
        int i = 0;
        for (var iterator = iterator(); iterator.hasNext(); ) {
            var element = iterator.next();
            if (index == i) {
                iterator.remove();
                return element;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T set(int index, T element) {
        int i = 0;
        for (var iterator = listIterator(); iterator.hasNext(); ) {
            var previousElement = iterator.next();
            if (index == i) {
                iterator.set(element);
                return previousElement;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, T element) {
        int i = 0;
        for (var iterator = listIterator(); iterator.hasNext(); ) {
            iterator.next();
            if (index == i) {
                iterator.add(element);
                return;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(int index) {
        int i = 0;
        for (T element : this) {
            if (index == i) {
                return element;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<T> listIterator() {
        return new ListIterator<>() {
            private ListNode currentNode = new ListNode(null, null, headNode);
            private int currentIndex = -1;

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
                currentIndex++;
                return currentNode.value;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasPrevious() {
                return currentNode.previousNode != null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public T previous() {
                currentNode = currentNode.previousNode;
                if (currentNode == null) {
                    throw new NoSuchElementException();
                }
                currentIndex--;
                return currentNode.value;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int nextIndex() {
                return currentIndex + 1;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int previousIndex() {
                return currentIndex;
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
                currentIndex--;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void set(T t) {
                currentNode.value = t;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void add(T t) {
                var newNext = new ListNode(t, currentNode, currentNode.nextNode);
                if (currentNode.nextNode != null) {
                    currentNode.nextNode.previousNode = newNext;
                } else {
                    tailNode = newNext;
                }
                currentNode.nextNode = newNext;
                if (currentIndex == -1) {
                    headNode = newNext;
                }
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return listIterator();
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
        private T value;
        private ListNode previousNode, nextNode;

        private ListNode(T value, ListNode previousNode, ListNode nextNode) {
            this.value = value;
            this.previousNode = previousNode;
            this.nextNode = nextNode;
        }
    }


}
