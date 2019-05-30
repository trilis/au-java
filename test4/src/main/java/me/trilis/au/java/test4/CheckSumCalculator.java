package me.trilis.au.java.test4;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class CheckSumCalculator {

    protected static final int BUFFER_SIZE = 256;

    @SuppressWarnings("StatementWithEmptyBody")
    public static @NotNull byte[] calculateNonParallel(@NotNull File file) throws NoSuchAlgorithmException, IOException {
        var digest = MessageDigest.getInstance("MD5");
        if (file.isDirectory()) {
            digest.update(file.getName().getBytes());
            var children = file.listFiles();
            assert children != null;
            Arrays.sort(children, Comparator.comparing(File::getAbsolutePath));
            for (var child : children) {
                digest.update(calculateNonParallel(child));
            }
        } else {
            var inputStream = new DigestInputStream(new FileInputStream(file), digest);
            var buffer = new byte[BUFFER_SIZE];
            while (inputStream.read(buffer, 0, BUFFER_SIZE) != -1) {

            }
            inputStream.close();
        }
        return digest.digest();
    }

    public static @NotNull byte[] calculateForkJoin(@NotNull File file) throws ExecutionException, InterruptedException {
        var pool = new ForkJoinPool();
        var task = new HashTask(file);
        pool.execute(task);
        return Util.unpackingByteArray(task.get());
    }
}
