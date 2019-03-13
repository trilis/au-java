package me.trilis.au.java.hw5.testclasses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BigClass {
    private int a;
    Set<Integer> s;
    final boolean x = true;

    public <T, S extends T> void generics(T t, S s) {

    }

    public <T> BigClass(T t) {

    }

    public static void main(int x, Set<? extends Object> y) throws FileNotFoundException, IOException {
    }

    private class OtherClass<T extends Object & Set> extends ArrayList<T> implements List<T>, Set<T> {

        public OtherClass(T t) {

        }

        int c;

        void main(T t) {
        }
    }

    public interface Interface {

    }
}
