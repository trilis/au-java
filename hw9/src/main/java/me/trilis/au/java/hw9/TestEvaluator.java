package me.trilis.au.java.hw9;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

/**
 * This class contains methods for launching tests.
 */
public class TestEvaluator {

    /**
     * Launches tests in provided file or files, prints result of each test including
     * time its execution has taken, and also print statistics at the end of
     * execution of each test class.
     * @param args the only argument in args should be path to .class or .jar
     *             file which contain tests.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Should take exactly one argument, " +
                    "path to JAR or Java class.");
        }
        var file = new File(args[0]);
        var classes = new ArrayList<Class<?>>();
        if (file.toString().endsWith(".class")) {
            try {
                classes.add(loadClass(file));
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
                return;
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL");
                return;
            }
        } else if (file.toString().endsWith(".jar")) {
            try {
                classes.addAll(loadJAR(file));
            } catch (ClassNotFoundException e) {
                System.out.println("Classes not found");
                return;
            } catch (IOException e) {
                System.out.println("IO exception");
                return;
            }
        } else {
            throw new IllegalArgumentException("Should take path to .class or .jar file");
        }
        for (var clazz : classes) {
            System.out.println("Class " + clazz.getName());
            List<TestResult> results;
            try {
                results = runAllTests(clazz);
            } catch (IllegalAccessException e) {
                System.out.println("Illegal acces exception: " + e.getMessage());
                continue;
            } catch (InstantiationException | NoSuchMethodException e) {
                System.out.println("Failed to create instance of a class: " + e.getMessage());
                continue;
            } catch (InvocationTargetException e) {
                System.out.println("Non-test method threw exception: " + e.getTargetException().getMessage());
                continue;
            }
            int failed = 0;
            int passed = 0;
            int ignored = 0;
            for (var result : results) {
                switch (result.getType()) {
                    case IGNORED:
                        ignored++;
                        break;
                    case PASSED:
                        passed++;
                        break;
                    case FAILED:
                        failed++;
                }
                System.out.println(result);
            }
            System.out.println("Total: " + passed + " passed, " + failed +
                    " failed, " + ignored + " ignored.");
            System.out.println();
        }
    }

    /**
     * Runs all tests in the provided class.
     * @param clazz the provided class.
     * @return list of results for each test.
     * @throws IllegalAccessException if this method cannot access constructor or any of the
     * annotated methods.
     * @throws InstantiationException if the provided class can not be instantiated.
     * @throws InvocationTargetException if constructor or other non-test methods throw an exception.
     * @throws NoSuchMethodException if there is no constructor to call.
     */
    public static List<TestResult> runAllTests(Class<?> clazz) throws
            IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        var beforeMethods = new ArrayList<Method>();
        var afterMethods = new ArrayList<Method>();
        var beforeClassMethods = new ArrayList<Method>();
        var afterClassMethods = new ArrayList<Method>();
        var tests = new ArrayList<Method>();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeClass.class)) {
                beforeClassMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterClass.class)) {
                afterClassMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                tests.add(method);
            }
        }
        Object instance;
        instance = clazz.getDeclaredConstructor().newInstance();
        var results = new ArrayList<TestResult>();
        for (var method : beforeClassMethods) {
            method.invoke(instance);
        }
        for (var test : tests) {
            results.add(runTest(instance, test, beforeMethods, afterMethods));
        }
        for (var method : afterClassMethods) {
            method.invoke(instance);
        }
        return results;
    }

    private static TestResult runTest(Object instance, Method test,
                                     List<Method> beforeMethods, List<Method> afterMethods) throws InvocationTargetException, IllegalAccessException {
        for (var method : beforeMethods) {
            method.invoke(instance);
        }
        var annotation = test.getDeclaredAnnotation(Test.class);
        if (!annotation.ignore().isEmpty()) {
            return TestResult.ignoredResult(test.getName(), annotation.ignore());
        }
        TestResult result = null;
        boolean threw = false;
        long time = System.currentTimeMillis();
        try {
            test.invoke(instance);
        } catch (InvocationTargetException e) {
            threw = true;
            if (annotation.expected().isInstance(e.getTargetException())) {
                result = TestResult.passedResult(test.getName(), System.currentTimeMillis() - time);
            } else {
                result = TestResult.failedResult(test.getName(), System.currentTimeMillis() - time);
            }
        }
        if (!threw) {
            if (annotation.expected().equals(Test.NoException.class)) {
                result = TestResult.passedResult(test.getName(), System.currentTimeMillis() - time);
            } else {
                result = TestResult.failedResult(test.getName(), System.currentTimeMillis() - time);
            }
        }
        for (var method : afterMethods) {
            method.invoke(instance);
        }
        return result;
    }


    private static List<Class<?>> loadJAR(File file) throws IOException, ClassNotFoundException {
        var loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        var jarFile = new JarFile(file);
        var classes = new ArrayList<Class<?>>();
        for (var entry : Collections.list(jarFile.entries())) {
            if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                classes.add(loader.loadClass(getNameWithoutExtension(entry.getName().replace("/", "."))));
            }
        }
        return classes;
    }

    private static Class<?> loadClass(File file) throws ClassNotFoundException,
            MalformedURLException {
        var directory = file.toPath().getParent();
        var loader = new URLClassLoader(new URL[]{directory.toUri().toURL()});
        return loader.loadClass(getNameWithoutExtension(file.getName()));
    }

    private static String getNameWithoutExtension(String name) {
        return name.substring(0, name.length() - ".class".length());
    }

    /**
     * Structure for storing results of test execution.
     */
    public static class TestResult {
        public enum ResultType {
            PASSED, FAILED, IGNORED
        }

        private ResultType type;
        private String message, testName;

        public ResultType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }

        public String getTestName() {
            return testName;
        }

        private long time;

        private TestResult(String testName, ResultType type, String message, long time) {
            this.testName = testName;
            this.type = type;
            this.message = message;
            this.time = time;
        }

        public static TestResult passedResult(String testName, long time) {
            return new TestResult(testName, ResultType.PASSED, "", time);
        }

        public static TestResult failedResult(String testName, long time) {
            return new TestResult(testName, ResultType.FAILED, "", time);
        }

        public static TestResult ignoredResult(String testName, String message) {
            return new TestResult(testName, ResultType.IGNORED, message, 0);
        }

        @Override
        public String toString() {

            if (type == ResultType.IGNORED) {
                return testName + ": IGNORED, reason: " + message;
            } else if (type == ResultType.PASSED) {
                return testName + ": PASSED, time: " + time + " ms";
            } else {
                return testName + ": FAILED, time: " + time + " ms";
            }
        }
    }
}
