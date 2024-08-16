package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface ShowPreferencePath {

    boolean showPreferencePath(Preference preference);
}
