package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.os.Bundle;

import java.util.Optional;
import java.util.OptionalInt;

import de.KnollFrank.lib.settingssearch.common.Bundles;

class PreferencePathNavigatorDataConverter {

    private static final String ID_OF_SEARCHABLE_PREFERENCE = PreferencePathNavigatorData.class.getPackage().getName() + ".idOfSearchablePreference";
    private static final String INDEX_WITHIN_PREFERENCE_PATH = PreferencePathNavigatorData.class.getPackage().getName() + ".indexWithinPreferencePath";

    public static Bundle toBundle(final PreferencePathNavigatorData preferencePathNavigatorData) {
        final Bundle bundle = new Bundle();
        bundle.putInt(ID_OF_SEARCHABLE_PREFERENCE, preferencePathNavigatorData.idOfSearchablePreference());
        bundle.putInt(INDEX_WITHIN_PREFERENCE_PATH, preferencePathNavigatorData.indexWithinPreferencePath());
        return bundle;
    }

    public static Optional<PreferencePathNavigatorData> fromBundle(final Bundle bundle) {
        final Bundles bundles = new Bundles(bundle);
        final OptionalInt idOfSearchablePreference = bundles.getOptionalInt(ID_OF_SEARCHABLE_PREFERENCE);
        final OptionalInt indexWithinPreferencePath = bundles.getOptionalInt(INDEX_WITHIN_PREFERENCE_PATH);
        return idOfSearchablePreference.isPresent() && indexWithinPreferencePath.isPresent() ?
                Optional.of(
                        new PreferencePathNavigatorData(
                                idOfSearchablePreference.orElseThrow(),
                                indexWithinPreferencePath.orElseThrow())) :
                Optional.empty();
    }
}
