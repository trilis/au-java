package me.trilis.au.java.test2.testClasses;

public class ClassWithOneInterfaceDependency {

    public final Interface dependency;

    public ClassWithOneInterfaceDependency(Interface dependency) {
        this.dependency = dependency;
    }
}