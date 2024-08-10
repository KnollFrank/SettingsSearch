package de.KnollFrank.lib.preferencesearch.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface ShowPreferencePath {

    boolean showPreferencePath(Preference preference);
}
