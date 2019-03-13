package me.trilis.au.java.test2.testClasses;

public class ClassWithLoopOne {

    public final ClassWithLoopTwo dependency;

    public ClassWithLoopOne(ClassWithLoopTwo dependency) {
        this.dependency = dependency;
    }
}
