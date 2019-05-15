package me.trilis.au.java.test2;

import me.trilis.au.java.test2.testClasses.ClassWithOneClassDependency;
import me.trilis.au.java.test2.testClasses.ClassWithOneInterfaceDependency;
import me.trilis.au.java.test2.testClasses.ClassWithoutDependencies;
import me.trilis.au.java.test2.testClasses.InterfaceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {
    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("me.trilis.au.java.test2.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "me.trilis.au.java.test2.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("me.trilis.au.java.test2.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertNotNull(instance.dependency);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "me.trilis.au.java.test2.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("me.trilis.au.java.test2.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    public void testLoops() {
        assertThrows(InjectionCycleException.class, () -> Injector.initialize(
                "me.trilis.au.java.test2.testClasses.ClassWithLoop",
                Arrays.asList("me.trilis.au.java.test2.testClasses.ClassWithLoopOne",
                        "me.trilis.au.java.test2.testClasses.ClassWithLoopTwo")
        ));
    }
}