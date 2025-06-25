package de.KnollFrank.lib.settingssearch.results.recyclerview;

import de.KnollFrank.lib.settingssearch.PreferencePath;

@FunctionalInterface
public interface PreferencePathDisplayer {

    CharSequence display(final PreferencePath preferencePath);
}
