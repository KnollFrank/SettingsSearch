package de.KnollFrank.settingssearch.preference.fragment.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContent {

    public static final List<PlaceholderItem> ITEMS = new ArrayList<>();
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id(), item);
    }

    private static PlaceholderItem createPlaceholderItem(int position) {
        return new PlaceholderItem(
                String.valueOf(position),
                "Item " + position,
                makeDetails(position));
    }

    private static String makeDetails(int position) {
        return "Details about Item: " + position;
    }

    public record PlaceholderItem(String id, String content, String details) {

        @Override
        public String toString() {
            return content;
        }
    }
}