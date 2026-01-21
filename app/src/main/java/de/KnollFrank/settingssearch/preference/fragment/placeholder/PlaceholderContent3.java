package de.KnollFrank.settingssearch.preference.fragment.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlaceholderContent3 {

    public static final List<PlaceholderContent.PlaceholderItem> ITEMS = new ArrayList<>();
    public static final Map<String, PlaceholderContent.PlaceholderItem> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private PlaceholderContent3() {
    }

    private static void addItem(final PlaceholderContent.PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.key(), item);
    }

    private static PlaceholderContent.PlaceholderItem createPlaceholderItem(final int position) {
        return new PlaceholderContent.PlaceholderItem(
                String.valueOf(position),
                "Item3 " + position,
                makeSummary(position),
                Optional.empty());
    }

    private static String makeSummary(final int position) {
        return "Details about Item3: " + position;
    }
}