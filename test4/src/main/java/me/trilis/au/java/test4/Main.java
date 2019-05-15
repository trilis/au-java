package me.trilis.au.java.test4;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ExecutionException, InterruptedException {
        if (args.length < 1) {
            System.out.println("No path provided");
            return;
        }
        var path = args[0];
        var file = new File(path);
        var nonParallelTime = System.nanoTime();
        var result = CheckSumCalculator.calculateNonParallel(file);
        nonParallelTime = System.nanoTime() - nonParallelTime;
        System.out.println("Check sum:");
        for (byte b : result) {
            System.out.print(b + " ");
        }
        System.out.println();
        var forkJoinTime = System.nanoTime();
        CheckSumCalculator.calculateForkJoin(file);
        forkJoinTime = System.nanoTime() - forkJoinTime;
        System.out.println("Non parallel time: " + nonParallelTime / 1e6 + " ms");
        System.out.println("Fork join time: " + forkJoinTime / 1e6 + " ms");
    }
}
