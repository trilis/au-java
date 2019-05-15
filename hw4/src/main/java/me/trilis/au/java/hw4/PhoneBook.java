package me.trilis.au.java.hw4;

import me.trilis.au.java.hw4.entities.Entry;

import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * Class containing methods for accessing phone book mongo database.
 */
public class PhoneBook {

    final private Datastore datastore;

    /**
     * Creates new empty phone book.
     *
     * @param mongoClient the representations of the connection to a MongoDB instance.
     * @param dbName      the name of the database.
     */
    public PhoneBook(MongoClient mongoClient, String dbName) {
        var morphia = new Morphia();
        morphia.mapPackage("me.trilis.au.java.hw4.entities");
        datastore = morphia.createDatastore(mongoClient, dbName);
    }

    /**
     * Adds the specified entry to the phone book. If this phone book
     * already contained the entry, this method does nothing and returns false.
     *
     * @param name   the name of the specified entry.
     * @param number the number of the specified entry.
     * @return true if this entry was not in the phone book before,
     * else otherwise.
     */
    public boolean addEntry(String name, String number) {
        if (findQuery(name, number).get() != null) {
            return false;
        }
        var newEntry = new Entry(name, number);
        datastore.save(newEntry);
        return true;
    }

    /**
     * Returns all entries in this phone book with the specified name in arbitrary order.
     *
     * @param name the specified name.
     * @return list of all entries in this phone book with the specified name.
     */
    public List<Entry> getByName(String name) {
        return datastore.createQuery(Entry.class).field("name").equal(name).asList();
    }

    /**
     * Returns all entries in this phone book with the specified number in arbitrary order.
     *
     * @param number the specified number.
     * @return list of all entries in this phone book with the specified number.
     */
    public List<Entry> getByNumber(String number) {
        return datastore.createQuery(Entry.class).field("number").equal(number).asList();
    }

    /**
     * Returns all entries in this phone book in arbitrary order.
     *
     * @return the list of all entries in this phone book.
     */
    public List<Entry> getAll() {
        return datastore.createQuery(Entry.class).asList();
    }

    /**
     * Removes the specified entry from this phone book. If this phone book
     * did not contain the entry, this method does nothing and returns false.
     *
     * @param name   the name of the specified entry.
     * @param number the number of the specified entry.
     * @return true if this phone book contained the entry before,
     * false otherwise
     */
    public boolean removeEntry(String name, String number) {
        return datastore.findAndDelete(findQuery(name, number)) != null;
    }

    /**
     * Changes the name and the number of the entry in this phone book. If this phone book
     * does not contain the old entry, this method does nothing and returns false.
     * If this phone book contains both old and new entries, this method removes the old one.
     *
     * @param oldName   the name of entry to be found.
     * @param oldNumber the number of entry to be found.
     * @param newName   the new name of this entry.
     * @param newNumber the new number of this entry.
     * @return true if this phone book contains the entry, false otherwise
     */
    public boolean changeEntry(String oldName, String oldNumber, String newName, String newNumber) {
        var entry = findQuery(oldName, oldNumber).get();
        if (entry == null) {
            return false;
        }
        if (findQuery(newName, newNumber).get() != null) {
            if (!oldName.equals(newName) || !oldNumber.equals(newNumber)) {
                removeEntry(oldName, oldNumber);
            }
        } else {
            entry.change(newName, newNumber);
            datastore.save(entry);
        }
        return true;
    }

    private Query<Entry> findQuery(String name, String number) {
        var query = datastore.createQuery(Entry.class);
        query.and(
                query.criteria("name").equal(name),
                query.criteria("number").equal(number)
        );
        return query;
    }

}
