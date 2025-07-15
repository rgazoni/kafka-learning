package io.spring.training.boot.kafkatraining.internal.utils;

public class ByteConverter {

    /**
     * Convert bytes to kilobytes (kB = 1_000 bytes).
     * @return value in kB (as a double)
     */
    public static double toKB(long bytes) {
        return bytes / 1000.0;
    }

    /**
     * Convert bytes to megabytes (mB = 1_000_000 bytes).
     * @return value in mB (as a double)
     */
    public static double toMB(long bytes) {
        return bytes / 1000.0;
    }

    /**
     * Convert bytes to gigabytes (gB = 1_000_000_000 bytes).
     * @return value in gB (as a double)
     */
    public static double toGB(long bytes) {
        return bytes / 1000.0;
    }

}
