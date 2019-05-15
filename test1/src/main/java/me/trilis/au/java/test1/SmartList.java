package me.trilis.au.java.test1;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of the list optimized for small number of elements.
 * It uses ArrayList for data storage if number of elements is greater than 5, and
 * primitive structures (array or just pointer) otherwise.
 *
 * @param <E> the type of elements in this list.
 */
@SuppressWarnings("unchecked")
public class SmartList<E> extends AbstractList<E> implements List<E> {

    /**
     * @inheritDoc
     */
    @Override
    public void add(int index, E element) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 0) {
            data = element;
        } else if (size == 1) {
            if (index == 0) {
                data = new Object[]{element, data};
            } else {
                data = new Object[]{data, element};
            }
        } else if (size <= 4) {
            E[] newData = (E[]) new Object[size + 1];
            var j = 0;
            for (int i = 0; i <= size; i++) {
                if (index == i) {
                    newData[i] = element;
                } else {
                    newData[i] = ((E[]) data)[j];
                    j++;
                }
            }
            data = newData;
        } else if (size == 5) {
            data = new ArrayList<>(Arrays.asList((E[]) data));
            ((ArrayList<E>) data).add(index, element);
        } else {
            ((ArrayList<E>) data).add(index, element);
        }
        size++;
    }

    /**
     * @inheritDoc
     */
    @Override
    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E oldElement = null;
        if (size == 1) {
            oldElement = (E) data;
            data = null;
        } else if (size == 2) {
            oldElement = ((E[]) data)[index];
            if (index == 0) {
                data = ((E[]) data)[1];
            } else {
                data = ((E[]) data)[0];
            }
        } else if (size <= 5) {
            E[] newData = (E[]) new Object[size - 1];
            var j = 0;
            for (var i = 0; i < size; i++) {
                if (index != i) {
                    newData[j] = ((E[]) data)[i];
                    j++;
                } else {
                    oldElement = ((E[]) data)[i];
                }
            }
            data = newData;
        } else if (size == 6) {
            oldElement = ((ArrayList<E>) data).remove(index);
            data = ((ArrayList<E>) data).toArray((E[]) new Object[size - 1]);

        } else {
            oldElement = ((ArrayList<E>) data).remove(index);
        }
        size--;
        return oldElement;
    }

    /**
     * @inheritDoc
     */
    @Override
    public E set(int index, E element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            var oldElement = (E) data;
            data = element;
            return oldElement;
        } else if (size <= 5) {
            var oldElement = ((E[]) data)[index];
            ((E[]) data)[index] = element;
            return oldElement;
        } else {
            return ((ArrayList<E>) data).set(index, element);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public E get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            return (E) data;
        } else if (size <= 5) {
            return ((E[]) data)[index];
        } else {
            return ((ArrayList<E>) data).get(index);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Creates empty list.
     */
    public SmartList() {
        data = null;
    }

    /**
     * Creates list with elements from the specified collection.
     *
     * @param collection the specified collection.
     */
    public SmartList(@NotNull Collection<E> collection) {
        if (collection.size() == 0) {
            data = null;
        } else if (collection.size() == 1) {
            for (var element : collection) {
                data = element;
            }
        } else if (collection.size() <= 5) {
            data = collection.toArray();
        } else {
            data = new ArrayList<>(collection);
        }
        size = collection.size();
    }

    private int size = 0;
    private Object data;
}
