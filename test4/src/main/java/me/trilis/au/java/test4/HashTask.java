package me.trilis.au.java.test4;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.RecursiveTask;

public class HashTask extends RecursiveTask<Byte[]> {

    private final File file;

    public HashTask(@NotNull File file) {
        this.file = file;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected @NotNull Byte[] compute() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        if (file.isDirectory()) {
            digest.update(file.getName().getBytes());
            var children = file.listFiles();
            assert children != null;
            var subTasks = new ArrayList<HashTask>();
            Arrays.sort(children, Comparator.comparing(File::getAbsolutePath));
            for (var child : children) {
                var newTask = new HashTask(child);
                newTask.fork();
                subTasks.add(newTask);
            }
            for (var task : subTasks) {
                digest.update(Util.unpackingByteArray(task.join()));
            }
        } else {
            DigestInputStream inputStream = null;
            try {
                inputStream = new DigestInputStream(new FileInputStream(file), digest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert inputStream != null;
            var buffer = new byte[CheckSumCalculator.BUFFER_SIZE];
            while (true) {
                try {
                    if (inputStream.read(buffer, 0, CheckSumCalculator.BUFFER_SIZE) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Util.packingByteArray(digest.digest());
    }
}
