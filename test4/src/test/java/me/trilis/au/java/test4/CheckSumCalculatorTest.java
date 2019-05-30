package me.trilis.au.java.test4;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class CheckSumCalculatorTest {
    @Test
    void testConsistency() throws ExecutionException, InterruptedException, IOException, NoSuchAlgorithmException {
        assertArrayEquals(CheckSumCalculator.calculateNonParallel(new File(".")),
                CheckSumCalculator.calculateForkJoin(new File(".")));
    }

    @Test
    void testOneFile() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {
        assertArrayEquals(CheckSumCalculator.calculateNonParallel(new File("src/test/java/testFile")),
                MessageDigest.getInstance("MD5").digest("abacaba".getBytes()));
        assertArrayEquals(CheckSumCalculator.calculateForkJoin(new File("src/test/java/testFile")),
                MessageDigest.getInstance("MD5").digest("abacaba".getBytes()));
    }
}