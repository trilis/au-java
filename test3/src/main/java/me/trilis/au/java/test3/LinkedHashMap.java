package me.trilis.au.java.test3;

import java.util.*;

/**
 * Implementation of map interface using hash table and linked list.
 * Iteration order in this map is equal to insertion order of entries.
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 */
public class LinkedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    static private final int BUCKET_NUMBER = 10;
    static private final double LOAD_FACTOR = 0.75;
    static private final int GROWTH_FACTOR = 2;

    private final LinkedList<Entry<K, V>> ordering;
    private LinkedList<Entry<K, V>>[] buckets;
    private int size = 0;

    /**
     * Creates new empty hash map.
     */
    @SuppressWarnings("unchecked")
    public LinkedHashMap() {
        ordering = new LinkedList<>();
        buckets = new LinkedList[BUCKET_NUMBER];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    /** @inheritDoc */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return ordering;
    }

    private LinkedList<Entry<K, V>> getBucket(Object key) {
        var hash = Math.abs(key.hashCode());
        return buckets[hash % buckets.length];
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        var entries = new LinkedList<Entry<K, V>>();
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            entries.addAll(bucket);
        }
        buckets = new LinkedList[buckets.length * GROWTH_FACTOR];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
        for (var entry : entries) {
            putWithoutOrdering(entry.getKey(), entry.getValue());
        }
    }

    private V putWithoutOrdering(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        var bucket = getBucket(key);
        for (var entry : bucket) {
            if (entry.getKey().equals(key)) {
                V previousValue = entry.getValue();
                remove(entry.getKey());
                bucket.add(new MapEntry(key, value));
                return previousValue;
            }
        }
        size++;
        bucket.add(new MapEntry(key, value));
        if (buckets.length * LOAD_FACTOR < size) {
            rehash();
        }
        return null;
    }

    /** @inheritDoc
     * @implSpec if this key was already present in the map, this method
     * will remove old entry and insert a new one, so changed entry will be moved
     * to the end of iteration ordering.
     * */
    @Override
    public V put(K key, V value) {
        var result = putWithoutOrdering(key, value);
        ordering.add(new MapEntry(key, value));
        return result;
    }

    /** @inheritDoc */
    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        var bucket = getBucket(key);
        for (var entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /** @inheritDoc */
    @Override
    public V remove(Object key) {
        var bucket = getBucket(key);
        Entry<K, V> entry;
        for (var iterator = bucket.iterator(); iterator.hasNext(); ) {
            entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();
                ordering.remove(new MapEntry(entry.getKey(), entry.getValue()));
                size--;
                return entry.getValue();
            }
        }
        return null;
    }


    private class MapEntry implements Entry<K, V> {

        private final K key;
        private V value;

        private MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            var previousValue = this.value;
            this.value = value;
            return previousValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapEntry mapEntry = (MapEntry) o;
            return Objects.equals(key, mapEntry.key) &&
                    Objects.equals(value, mapEntry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
