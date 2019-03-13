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
        assertEquals("Fields unique to the first class:\n" +
                "[int w]\n" +
                "Fields unique to the second class:\n" +
                "[T t]\n" +
                "Methods unique to the first class:\n" +
                "[public java.lang.String lol(), public void foo()]\n" +
                "Methods unique to the second class:\n" +
                "[public <S> void bar(java.util.Map<? extends T, ? super S> arg0), " +
                "public void foo(java.util.Map<T, ? super T> arg0)]\n",
                out.toString());
    }

    @Test
    public void testBigClass() throws FileNotFoundException {
        Reflector.printStructure(BigClass.class);
        assertThat(new File("BigClass.java")).hasContent(
            "public class BigClass {\n" +
            "    public <T> BigClass(T arg0) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    private int a;\n" +
            "    java.util.Set<java.lang.Integer> s;\n" +
            "    final boolean x = false;\n" +
            "    public static void main(int arg0, java.util.Set<?> arg1) throws java.io.FileNotFoundException, java.io.IOException {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public <S extends T, T> void generics(T arg0, S arg1) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public abstract static interface Interface {\n" +
            "    }\n" +
            "    private class OtherClass<T extends java.util.Set> extends java.util.ArrayList<T> implements java.util.List<T>, java.util.Set<T> {\n" +
            "        public OtherClass(me.trilis.au.java.hw5.testclasses.BigClass arg0, java.lang.Object arg1) {\n" +
            "            throw new UnsupportedOperationException();\n" +
            "        }\n" +
            "        int c;\n" +
            "        void main(T arg0) {\n" +
            "            throw new UnsupportedOperationException();\n" +
            "        }\n" +
            "    }\n" +
            "}\n");
    }

    @Test
    void testEmpty() throws FileNotFoundException {
        Reflector.printStructure(EmptyClass.class);
        assertThat(new File("EmptyClass.java")).hasContent(
            "public class EmptyClass {\n" +
            "    public EmptyClass() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "}\n");
    }

    @Test
    void testAbstract() throws FileNotFoundException {
        Reflector.printStructure(AbstractClass.class);
        assertThat(new File("AbstractClass.java")).hasContent(
            "public abstract class AbstractClass {\n" +
            "    public AbstractClass() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    private int v;\n" +
            "    protected abstract void foo();\n" +
            "    public int bar() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "}\n");
    }

    @Test
    void testInterface() throws FileNotFoundException {
        Reflector.printStructure(Interface.class);
        assertThat(new File("Interface.java")).hasContent(
            "public abstract interface Interface {\n" +
            "    public abstract int bar();\n" +
            "    public abstract java.lang.String lol();\n" +
            "    public abstract void foo();\n" +
            "}");
    }

    @Test
    void testExtendingClass() throws FileNotFoundException {
        Reflector.printStructure(ExtendingClass.class);
        assertThat(new File("ExtendingClass.java")).hasContent(
            "public class ExtendingClass extends me.trilis.au.java.hw5.testclasses.AbstractClass implements me.trilis.au.java.hw5.testclasses.Interface {\n" +
            "    public ExtendingClass() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    int w;\n" +
            "    public java.lang.String lol() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public void foo() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public void lol(java.util.Set<?> arg0) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "}\n");
    }

    @Test
    void testGenericClass() throws FileNotFoundException {
        Reflector.printStructure(GenericClass.class);
        assertThat(new File("GenericClass.java")).hasContent(
            "public class GenericClass<T> {\n" +
            "    public GenericClass() {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    T t;\n" +
            "    public <S> void bar(java.util.Map<? extends T, ? super S> arg0) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public void foo(java.util.Map<T, ? super T> arg0) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "    public void lol(java.util.Set<?> arg0) {\n" +
            "        throw new UnsupportedOperationException();\n" +
            "    }\n" +
            "}\n"
        );
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
        return Class.forName(clazz.getSimpleName(), true, loader);
    }
}