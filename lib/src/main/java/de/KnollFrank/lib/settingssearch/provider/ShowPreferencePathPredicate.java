package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.PreferencePath;

@FunctionalInterface
public interface ShowPreferencePathPredicate {

    boolean shallShowPreferencePath(PreferencePath preferencePath);
}