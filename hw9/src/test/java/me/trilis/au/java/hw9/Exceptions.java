package me.trilis.au.java.hw9;

import java.util.NoSuchElementException;

public class Exceptions {

    @Test(expected = NoSuchElementException.class)
    void test1() {

    }

    @Test
    void test2() {
        throw new NoSuchElementException();
    }

    @Test(expected = NoSuchElementException.class)
    void test3() {
        throw new NoSuchElementException();
    }

}
