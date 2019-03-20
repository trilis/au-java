package me.trilis.au.java.hw4;

import com.github.fakemongo.Fongo;
import me.trilis.au.java.hw4.entities.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {

    private PhoneBook phoneBook;
    private HashSet<Entry> answers;

    @BeforeEach
    public void init() {
        phoneBook = new PhoneBook(new Fongo("database").getMongo(), "database");
        answers = new HashSet<>();
    }

    @Test
    public void add() {
        assertTrue(phoneBook.addEntry("Alexey", "111"));
        assertFalse(phoneBook.addEntry("Alexey", "111"));
        answers.add(new Entry("Alexey", "111"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.addEntry("Alexey", "222"));
        answers.add(new Entry("Alexey", "222"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.addEntry("Arkady", "333"));
        answers.add(new Entry("Arkady", "333"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.addEntry("Alina", "333"));
        answers.add(new Entry("Alina", "333"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.addEntry("Anta", "444"));
        answers.add(new Entry("Anta", "444"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));
    }

    @Test
    public void remove() {
        add();
        assertFalse(phoneBook.removeEntry("Max", "777"));

        assertTrue(phoneBook.removeEntry("Alexey", "111"));
        assertFalse(phoneBook.removeEntry("Alexey", "111"));
        answers.remove(new Entry("Alexey", "111"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.removeEntry("Alexey", "222"));
        answers.remove(new Entry("Alexey", "222"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.removeEntry("Arkady", "333"));
        answers.remove(new Entry("Arkady", "333"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.removeEntry("Alina", "333"));
        answers.remove(new Entry("Alina", "333"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.removeEntry("Anta", "444"));
        answers.remove(new Entry("Anta", "444"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));
    }

    @Test
    public void getByName() {
        add();
        assertIterableEquals(new HashSet<>(List.of(
                new Entry("Alexey", "111"),
                new Entry("Alexey", "222"))
                ),
                new HashSet<>(phoneBook.getByName("Alexey"))
        );
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Arkady", "333"))),
                new HashSet<>(phoneBook.getByName("Arkady"))
        );
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Alina", "333"))),
                new HashSet<>(phoneBook.getByName("Alina"))
        );
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Anta", "444"))),
                new HashSet<>(phoneBook.getByName("Anta"))
        );
        assertIterableEquals(new HashSet<>(),
                new HashSet<>(phoneBook.getByName("Max"))
        );
    }

    @Test
    public void getByNumber() {
        add();
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Alexey", "111"))),
                new HashSet<>(phoneBook.getByNumber("111"))
        );
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Alexey", "222"))),
                new HashSet<>(phoneBook.getByNumber("222"))
        );
        assertIterableEquals(new HashSet<>(List.of(
                new Entry("Arkady", "333"),
                new Entry("Alina", "333"))
                ),
                new HashSet<>(phoneBook.getByNumber("333"))
        );
        assertIterableEquals(new HashSet<>(
                        List.of(new Entry("Anta", "444"))),
                new HashSet<>(phoneBook.getByNumber("444"))
        );
        assertIterableEquals(new HashSet<>(),
                new HashSet<>(phoneBook.getByNumber("777"))
        );
    }

    @Test
    public void change() {
        add();
        assertFalse(phoneBook.changeEntry("Max", "777", "Max", "888"));

        assertTrue(phoneBook.changeEntry("Anta", "444", "Nata", "555"));
        answers.remove(new Entry("Anta", "444"));
        answers.add(new Entry("Nata", "555"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.changeEntry("Alina", "333", "Max", "555"));
        answers.remove(new Entry("Alina", "333"));
        answers.add(new Entry("Max", "555"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.changeEntry("Alexey", "111", "Alexey", "555"));
        answers.remove(new Entry("Alexey", "111"));
        answers.add(new Entry("Alexey", "555"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.changeEntry("Alexey", "222", "Max", "555"));
        answers.remove(new Entry("Alexey", "222"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));

        assertTrue(phoneBook.changeEntry("Max", "555", "Max", "555"));
        assertIterableEquals(answers, new HashSet<>(phoneBook.getAll()));
    }
}