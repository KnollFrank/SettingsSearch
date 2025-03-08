package de.KnollFrank.settingssearch.preference.fragment.placeholder;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import de.KnollFrank.settingssearch.SettingsActivity3;

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

    private static void addItem(final PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.key(), item);
    }

    private static PlaceholderItem createPlaceholderItem(final int position) {
        return position == 1 ?
                new PlaceholderItem(
                        String.valueOf(position),
                        "Item " + position,
                        "link to SettingsActivity3",
                        // FK-TODO: schreibe einen Test für die folgende Situation: "item3 10" sollte über die Suchfunktion zwei mal gefunden werden, einmal "direkt", das andere mal über den Link des folgenden OnClickListeners, also über einen zweiten Path
                        Optional.of(context -> new Intent(context, SettingsActivity3.class))) :
                new PlaceholderItem(
                        String.valueOf(position),
                        "Item " + position,
                        makeSummary(position),
                        Optional.empty());
    }

    private static String makeSummary(final int position) {
        return "Details about Item: " + position;
    }

    public record PlaceholderItem(String key,
                                  String title,
                                  String summary,
                                  Optional<Function<Context, Intent>> intentFactory) {
    }
}