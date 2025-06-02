package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.text.TextUtils;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class DefaultPreferencePathDisplayer implements PreferencePathDisplayer {

    @Override
    public CharSequence display(final PreferenceEntityPath preferenceEntityPath) {
        return TextUtils.concat("Path: ", asString(preferenceEntityPath));
    }

    private static String asString(final PreferenceEntityPath preferenceEntityPath) {
        return preferenceEntityPath
                .preferences()
                .stream()
                .map(SearchablePreferenceEntity::getTitle)
                .map(title -> title.orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}
