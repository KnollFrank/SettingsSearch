package de.KnollFrank.lib.settingssearch.results.recyclerview;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;

@FunctionalInterface
public interface PreferencePathDisplayer {

    CharSequence display(final PreferenceEntityPath preferencePath);
}
