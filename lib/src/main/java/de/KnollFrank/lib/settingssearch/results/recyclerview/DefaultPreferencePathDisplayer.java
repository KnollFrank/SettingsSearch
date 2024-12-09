package de.KnollFrank.lib.settingssearch.results.recyclerview;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;

public class DefaultPreferencePathDisplayer implements PreferencePathDisplayer {

    @Override
    public CharSequence display(final PreferencePath preferencePath) {
        return preferencePath
                .preferences()
                .stream()
                .map(
                        searchablePreferencePOJO ->
                                searchablePreferencePOJO
                                        .getTitle()
                                        .orElse("?"))
                .collect(Collectors.joining(" > "));
    }
}
