package me.trilis.au.java.test4;

import org.jetbrains.annotations.NotNull;

public class Util {
    public static @NotNull byte[] unpackingByteArray(@NotNull Byte[] objectArray) {
        var primitiveArray = new byte[objectArray.length];
        for (var i = 0; i < objectArray.length; i++) {
            primitiveArray[i] = objectArray[i];
        }
        return primitiveArray;
    }

    public static @NotNull Byte[] packingByteArray(@NotNull byte[] primitiveArray) {
        var objectArray = new Byte[primitiveArray.length];
        for (var i = 0; i < objectArray.length; i++) {
            objectArray[i] = primitiveArray[i];
        }
        return objectArray;
    }
}
