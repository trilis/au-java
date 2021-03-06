package me.trilis.au.java.hw1;

/**
 * Implementation of hash table with strings as both keys and values.
 * Closed addressing is used as a method of collision resolution
 */
public class HashTable {

    static private final int DEFAULT_BUCKET_NUMBER = 10;
    static private final double DEFAULT_LOAD_FACTOR = 0.75;
    static private final int GROWTH_FACTOR = 2;

    private LinkedList<Entry>[] buckets;
    private int size = 0;
    private final double loadFactor;

    /**
     * Creates new hash table with default initial bucket number (10) and
     * default load factor (0.75).
     */
    public HashTable() {
        this(DEFAULT_BUCKET_NUMBER, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates new hash table with the specified initial bucket number
     * and default load factor (0.75).
     *
     * @param bucketNumber the specified initial bucket number.
     */
    public HashTable(int bucketNumber) {
        this(bucketNumber, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates new hash table with the specified initial bucket number
     * and the specified load factor.
     *
     * @param bucketNumber the specified initial bucket number.
     * @param loadFactor   the specified load factor.
     */
    @SuppressWarnings("unchecked")
    public HashTable(int bucketNumber, double loadFactor) {
        buckets = new LinkedList[bucketNumber];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        this.loadFactor = loadFactor;
    }

    private LinkedList<Entry> getBucket(String string) {
        var hash = Math.abs(string.hashCode());
        return buckets[hash % buckets.length];
    }

    /**
     * Increases bucket number by {@code GROWTH_FACTOR} times and internally
     * reorganizes this hash table.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        var entries = new LinkedList<Entry>();
        for (LinkedList<Entry> bucket : buckets) {
            entries.addAll(bucket);
        }
        buckets = new LinkedList[buckets.length * GROWTH_FACTOR];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
        size = 0;
        for (var entry : entries) {
            this.put(entry.key, entry.value);
        }
    }

    /**
     * Returns the size of this hash table.
     *
     * @return the size of this hash table.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if this hash table contains the specified key string.
     *
     * @param key the specified key string.
     * @return true if this hash table contains the specified key
     * or false otherwise.
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        var bucket = getBucket(key);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value corresponding to the specified key.
     *
     * @param key the specified key string.
     * @return the value corresponding to the key string
     * if this hash table contains the specified key or null otherwise.
     */
    public String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        var bucket = getBucket(key);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Puts in this hash table the specified value mapped to the specified key.
     *
     * @param key   the specified key string.
     * @param value the specified value string.
     * @return previous value mapped to the specified key or null if this hash
     * table didn't contain the specified key.
     */
    public String put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        var bucket = getBucket(key);
        for (var entry : bucket) {
            if (entry.key.equals(key)) {
                String previousValue = entry.value;
                entry.value = value;
                return previousValue;
            }
        }
        size++;
        bucket.add(new Entry(key, value));
        if (buckets.length * loadFactor < size) {
            rehash();
        }
        return null;
    }

    /**
     * Removes the specified key with the value mapped to it from this hash table.
     *
     * @param key the specified key string.
     * @return the removed value if this hash table contained the specified key
     * or null otherwise.
     */
    public String remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        var bucket = getBucket(key);
        Entry entry;
        for (var iterator = bucket.iterator(); iterator.hasNext(); ) {
            entry = iterator.next();
            if (entry.key.equals(key)) {
                iterator.remove();
                size--;
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Removes all entries from this hash table.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        buckets = new LinkedList[DEFAULT_BUCKET_NUMBER];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private class Entry {
        private final String key;
        private String value;

        private Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
