package me.trilis.au.java.hw5;

import me.trilis.au.java.hw5.testclasses.*;
import org.junit.jupiter.api.Test;

import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    @Test
    public void testEquals() throws FileNotFoundException,
            ClassNotFoundException, MalformedURLException {
        loadAndCompare(BigClass.class);
        loadAndCompare(EmptyClass.class);
        loadAndCompare(AbstractClass.class);
        loadAndCompare(ExtendingClass.class);
        loadAndCompare(Interface.class);
        loadAndCompare(GenericClass.class);
    }

    @Test
    public void testNotEqual() throws FileNotFoundException, MalformedURLException, ClassNotFoundException {
        var loadedFirstClass = loadGeneratedClass(ExtendingClass.class);
        var loadedSecondClass = loadGeneratedClass(GenericClass.class);
        var out = new ByteArrayOutputStream();
        Reflector.diffClasses(loadedFirstClass, loadedSecondClass, new PrintStream(out));
        assertEquals("Fields unique to the first class:" + System.lineSeparator()  +
                "[int w]" + System.lineSeparator()  +
                "Fields unique to the second class:" + System.lineSeparator()  +
                "[T t]" + System.lineSeparator()  +
                "Methods unique to the first class:" + System.lineSeparator()  +
                "[public java.lang.String lol(), public void foo()]" + System.lineSeparator()  +
                "Methods unique to the second class:" + System.lineSeparator()  +
                "[public <S> void bar(java.util.Map<? extends T, ? super S> arg0), " +
                "public void foo(java.util.Map<T, ? super T> arg0)]" + System.lineSeparator() ,
                out.toString());
    }

    @Test
    public void testBigClass() throws FileNotFoundException {
        Reflector.printStructure(BigClass.class);
        assertThat(new File("BigClass.java")).hasContent(
            "public class BigClass {" + System.lineSeparator()  +
            "    public <T> BigClass(T arg0) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    private int a;" + System.lineSeparator()  +
            "    java.util.Set<java.lang.Integer> s;" + System.lineSeparator()  +
            "    final boolean x = false;" + System.lineSeparator()  +
            "    public static void main(int arg0, java.util.Set<?> arg1) throws java.io.FileNotFoundException, java.io.IOException {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public <S extends T, T> void generics(T arg0, S arg1) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public abstract static interface Interface {" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    private class OtherClass<T extends java.util.Set> extends java.util.ArrayList<T> implements java.util.List<T>, java.util.Set<T> {" + System.lineSeparator()  +
            "        public OtherClass(me.trilis.au.java.hw5.testclasses.BigClass arg0, java.lang.Object arg1) {" + System.lineSeparator()  +
            "            throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "        }" + System.lineSeparator()  +
            "        int c;" + System.lineSeparator()  +
            "        void main(T arg0) {" + System.lineSeparator()  +
            "            throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "        }" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "}" + System.lineSeparator() );
        cleanUp("BigClass.java");
    }

    @Test
    void testEmpty() throws FileNotFoundException {
        Reflector.printStructure(EmptyClass.class);
        assertThat(new File("EmptyClass.java")).hasContent(
            "public class EmptyClass {" + System.lineSeparator()  +
            "    public EmptyClass() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "}" + System.lineSeparator() );
        cleanUp("EmptyClass.java");
    }

    @Test
    void testAbstract() throws FileNotFoundException {
        Reflector.printStructure(AbstractClass.class);
        assertThat(new File("AbstractClass.java")).hasContent(
            "public abstract class AbstractClass {" + System.lineSeparator()  +
            "    public AbstractClass() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    private int v;" + System.lineSeparator()  +
            "    protected abstract void foo();" + System.lineSeparator()  +
            "    public int bar() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "}" + System.lineSeparator() );
        cleanUp("AbstractClass.java");
    }

    @Test
    void testInterface() throws FileNotFoundException {
        Reflector.printStructure(Interface.class);
        assertThat(new File("Interface.java")).hasContent(
            "public abstract interface Interface {" + System.lineSeparator()  +
            "    public abstract int bar();" + System.lineSeparator()  +
            "    public abstract java.lang.String lol();" + System.lineSeparator()  +
            "    public abstract void foo();" + System.lineSeparator()  +
            "}");
        cleanUp("Interface.java");
    }

    @Test
    void testExtendingClass() throws FileNotFoundException {
        Reflector.printStructure(ExtendingClass.class);
        assertThat(new File("ExtendingClass.java")).hasContent(
            "public class ExtendingClass extends me.trilis.au.java.hw5.testclasses.AbstractClass implements me.trilis.au.java.hw5.testclasses.Interface {" + System.lineSeparator()  +
            "    public ExtendingClass() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    int w;" + System.lineSeparator()  +
            "    public java.lang.String lol() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public void foo() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public void lol(java.util.Set<?> arg0) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "}" + System.lineSeparator() );
        cleanUp("ExtendingClass.java");
    }

    @Test
    void testGenericClass() throws FileNotFoundException {
        Reflector.printStructure(GenericClass.class);
        assertThat(new File("GenericClass.java")).hasContent(
            "public class GenericClass<T> {" + System.lineSeparator()  +
            "    public GenericClass() {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    T t;" + System.lineSeparator()  +
            "    public <S> void bar(java.util.Map<? extends T, ? super S> arg0) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public void foo(java.util.Map<T, ? super T> arg0) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "    public void lol(java.util.Set<?> arg0) {" + System.lineSeparator()  +
            "        throw new UnsupportedOperationException();" + System.lineSeparator()  +
            "    }" + System.lineSeparator()  +
            "}" + System.lineSeparator() 
        );
        cleanUp("GenericClass.java");
    }


    private void loadAndCompare(Class<?> clazz) throws FileNotFoundException,
            MalformedURLException, ClassNotFoundException {
        var loadedClass = loadGeneratedClass(clazz);
        var out = new ByteArrayOutputStream();
        Reflector.diffClasses(clazz, loadedClass, new PrintStream(out));
        assertEquals(0, out.size());
    }

    private Class<?> loadGeneratedClass(Class<?> clazz) throws FileNotFoundException,
            ClassNotFoundException, MalformedURLException {
        Reflector.printStructure(clazz);
        var compiler = ToolProvider.getSystemJavaCompiler();
        var file = new File(clazz.getSimpleName() + ".java");
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                null, null, null);
        compiler.getTask(null, fileManager, null,
                Collections.singletonList("-parameters"), null,
                fileManager.getJavaFileObjects(file)).call();
        var loader = URLClassLoader.newInstance(new URL[]{new File(".").toURI().toURL()});
        var result =  Class.forName(clazz.getSimpleName(), true, loader);
        cleanUp(clazz.getSimpleName() + ".java");
        cleanUp(clazz.getSimpleName() + ".class");
        return result;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void cleanUp(String fileName) {
        new File(fileName).delete();
    }
}