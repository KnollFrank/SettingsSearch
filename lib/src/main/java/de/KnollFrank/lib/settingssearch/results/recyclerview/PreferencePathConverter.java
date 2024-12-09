package de.KnollFrank.lib.settingssearch.results.recyclerview;

import de.KnollFrank.lib.settingssearch.PreferencePath;

@FunctionalInterface
public interface PreferencePathConverter {

    CharSequence toCharSequence(final PreferencePath preferencePath);
}
