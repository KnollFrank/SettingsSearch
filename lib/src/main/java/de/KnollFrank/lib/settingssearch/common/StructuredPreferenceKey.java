package de.KnollFrank.lib.settingssearch.common;

import java.util.OptionalInt;

// FK-TODO: refactor
public class StructuredPreferenceKey {

    private static final String PREFIX = "graphical_item:::";
    private static final String DELIMITER = ":::";

    public static String buildKey(final int index, final String logicalKey, final String title) {
        return PREFIX + index + DELIMITER + (logicalKey == null ? "null" : logicalKey) + DELIMITER + (title == null ? "null" : title);
    }

    public static boolean isStructuredKey(final String key) {
        return key != null && key.startsWith(PREFIX);
    }

    public static OptionalInt getIndex(final String key) {
        if (!isStructuredKey(key)) return OptionalInt.empty();
        try {
            final String[] parts = key.split(DELIMITER);
            return OptionalInt.of(Integer.parseInt(parts[1]));
        } catch (final Exception e) {
            return OptionalInt.empty();
        }
    }

    public static String getLogicalKey(final String key) {
        if (!isStructuredKey(key)) return key;
        try {
            final String[] parts = key.split(DELIMITER);
            if (parts.length >= 3 && !"null".equals(parts[2])) {
                return parts[2];
            }
        } catch (final Exception ignored) {
        }
        return key;
    }
}
