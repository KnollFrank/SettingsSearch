package com.bytehamster.lib.preferencesearch;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

class PreferenceItemsBundle {

    private static final String ARGUMENT_PREFERENCE_ITEMS = "preferenceItems";

    public static void writePreferenceItems(final Bundle bundle, final List<PreferenceItem> preferenceItems) {
        bundle.putParcelableArrayList(ARGUMENT_PREFERENCE_ITEMS, new ArrayList<>(preferenceItems));
    }

    public static List<PreferenceItem> readPreferenceItems(final Bundle bundle) {
        return bundle.getParcelableArrayList(ARGUMENT_PREFERENCE_ITEMS);
    }
}
