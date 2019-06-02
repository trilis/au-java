package me.trilis.au.java.hw9;

public class FailedTests {

    @Test
    void test1() {
        assert false;
    }

    @Test
    void test2() {
        throw new IllegalArgumentException();
    }

}
