package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.PreferenceEntityPath;

@FunctionalInterface
public interface ShowPreferencePathPredicate {

    boolean showPreferencePath(PreferenceEntityPath preferencePath);
}
