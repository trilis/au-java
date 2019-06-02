package me.trilis.au.java.hw9;

public class IgnoredTests {

    @Test(ignore = "message1")
    void test1() {
        assert false;
    }

    @Test(ignore = "message2", expected = NullPointerException.class)
    void test2() {
        throw new NullPointerException();
    }

    @Test(ignore = "message3")
    void test3() {

    }
}
