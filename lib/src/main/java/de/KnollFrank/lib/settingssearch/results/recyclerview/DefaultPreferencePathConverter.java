package de.KnollFrank.lib.settingssearch.results.recyclerview;

import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;

public class DefaultPreferencePathConverter implements PreferencePathConverter {

    @Override
    public CharSequence toCharSequence(final PreferencePath preferencePath) {
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
