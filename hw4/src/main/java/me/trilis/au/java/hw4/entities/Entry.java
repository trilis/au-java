package me.trilis.au.java.hw4.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import java.util.Objects;

/**
 * Morphia entity for the phone book entries.
 */
@Entity
public class Entry {
    @Id
    private ObjectId id;

    @Indexed
    private String name;
    @Indexed
    private String number;

    public Entry() {

    }

    public Entry(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public void change(String newName, String newNumber) {
        name = newName;
        number = newNumber;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var entry = (Entry) o;
        return Objects.equals(name, entry.name) &&
                Objects.equals(number, entry.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    @Override
    public String toString() {
        return "Name='" + name + '\'' +
                ", Number='" + number + '\'';
    }
}
