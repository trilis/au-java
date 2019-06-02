package me.trilis.au.java.hw9;

import me.trilis.au.java.hw9.TestEvaluator.TestResult.ResultType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class TestEvaluatorTest {

    @Test
    void testEmpty() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var results = TestEvaluator.runAllTests(EmptyClass.class);
        assertEquals(0, results.size());
    }

    @Test
    void testPassed() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var results = TestEvaluator.runAllTests(PassedTests.class);
        assertEquals(2, results.size());
        for (var result : results) {
            assertEquals(ResultType.PASSED, result.getType());
        }
    }

    @Test
    void testFailed() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var results = TestEvaluator.runAllTests(FailedTests.class);
        assertEquals(2, results.size());
        for (var result : results) {
            assertEquals(ResultType.FAILED, result.getType());
        }
    }

    @Test
    void testIgnored() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var results = TestEvaluator.runAllTests(IgnoredTests.class);
        assertEquals(3, results.size());
        for (var result : results) {
            switch (result.getTestName()) {
                case "test1":
                    assertEquals("message1", result.getMessage());
                    break;
                case "test2":
                    assertEquals("message2", result.getMessage());
                    break;
                case "test3":
                    assertEquals("message3", result.getMessage());
            }
            assertEquals(ResultType.IGNORED, result.getType());
        }
    }
}