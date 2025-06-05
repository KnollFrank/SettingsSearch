package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.text.TextUtils;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class DefaultPreferencePathDisplayer implements PreferencePathDisplayer {

    @Override
    public CharSequence display(final PreferenceEntityPath preferencePath) {
        return TextUtils.concat("Path: ", asString(preferencePath));
    }

    private static String asString(final PreferenceEntityPath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreferenceEntity::getTitle)
                .map(title -> title.orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}
