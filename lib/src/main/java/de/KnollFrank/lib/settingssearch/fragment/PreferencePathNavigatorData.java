package de.KnollFrank.lib.settingssearch.fragment;

import android.os.Bundle;

public record PreferencePathNavigatorData(int idOfSearchablePreference,
                                          int indexWithinPreferencePath) {

    private static final String ID_OF_SEARCHABLE_PREFERENCE = PreferencePathNavigatorData.class.getPackage().getName() + ".idOfSearchablePreference";
    private static final String INDEX_WITHIN_PREFERENCE_PATH = PreferencePathNavigatorData.class.getPackage().getName() + ".indexWithinPreferencePath";

    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putInt(ID_OF_SEARCHABLE_PREFERENCE, idOfSearchablePreference());
        bundle.putInt(INDEX_WITHIN_PREFERENCE_PATH, indexWithinPreferencePath());
        return bundle;
    }

    public static PreferencePathNavigatorData fromBundle(final Bundle bundle) {
        return new PreferencePathNavigatorData(
                bundle.getInt(ID_OF_SEARCHABLE_PREFERENCE),
                bundle.getInt(INDEX_WITHIN_PREFERENCE_PATH));
    }
}
