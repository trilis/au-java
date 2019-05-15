package me.trilis.au.java.hw5.testclasses;

import java.util.Map;
import java.util.Set;

public class GenericClass<T> {
    T t;
    public void foo(Map<T, ? super T> map) {

    }

    public <S> void bar(Map<? extends T, ? super S> map) {

    }

    public void lol(Set<? extends Object> set) {

    }
}
