package me.trilis.au.java.test2.testClasses;

public class ClassWithLoop {

    public final ClassWithLoopOne dependency;

    public ClassWithLoop(ClassWithLoopOne dependency) {
        this.dependency = dependency;
    }
}
