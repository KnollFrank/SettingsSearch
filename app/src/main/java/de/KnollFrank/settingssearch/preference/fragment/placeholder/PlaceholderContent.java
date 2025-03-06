package de.KnollFrank.settingssearch.preference.fragment.placeholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                        // FK-FIXME: "item3 10" sollte über die Suchfunktion zwei mal gefunden werden, einmal "direkt", das andere mal über den Link des folgenden OnClickListeners, also über einen zweiten Path
                        Optional.of(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(final View view) {
                                        view.getContext().startActivity(createIntentForPosition1(view.getContext()));
                                    }
                                })) :
                new PlaceholderItem(
                        String.valueOf(position),
                        "Item " + position,
                        makeSummary(position),
                        Optional.empty());
    }

    public static Intent createIntentForPosition1(final Context context) {
        return new Intent(context, SettingsActivity3.class);
    }

    private static String makeSummary(final int position) {
        return "Details about Item: " + position;
    }

    public record PlaceholderItem(String key,
                                  String title,
                                  String summary,
                                  Optional<View.OnClickListener> onClickListener) {
    }
}