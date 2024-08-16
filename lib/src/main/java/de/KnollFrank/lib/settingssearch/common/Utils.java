package de.KnollFrank.lib.settingssearch.common;

public class Utils {

    public static <T> Class<? extends T> getClass(final String className) {
        try {
            return (Class<? extends T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
