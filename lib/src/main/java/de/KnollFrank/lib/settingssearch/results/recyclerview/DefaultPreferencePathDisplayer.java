package de.KnollFrank.lib.settingssearch.results.recyclerview;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class DefaultPreferencePathDisplayer implements PreferencePathDisplayer {

    @Override
    public CharSequence display(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(SearchablePreferencePOJO::getTitle)
                .map(title -> title.orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}