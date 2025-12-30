package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.text.TextUtils;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceWithinGraph;

public class DefaultPreferencePathDisplayer implements PreferencePathDisplayer {

    @Override
    public CharSequence display(final PreferencePath preferencePath) {
        return TextUtils.concat("Path: ", asString(preferencePath));
    }

    private static String asString(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreferenceWithinGraph::searchablePreference)
                .map(SearchablePreference::getTitle)
                .map(title -> title.orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}
