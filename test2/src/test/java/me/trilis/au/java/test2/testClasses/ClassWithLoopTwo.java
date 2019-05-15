package me.trilis.au.java.test2.testClasses;

public class ClassWithLoopTwo {

    public final ClassWithLoopOne dependency;

    public ClassWithLoopTwo(ClassWithLoopOne dependency) {
        this.dependency = dependency;
    }
}
