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

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    @Test
    public void testEquals() throws FileNotFoundException,
            ClassNotFoundException, MalformedURLException {
        loadAndCompare(BigClass.class);
        loadAndCompare(EmptyClass.class);
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