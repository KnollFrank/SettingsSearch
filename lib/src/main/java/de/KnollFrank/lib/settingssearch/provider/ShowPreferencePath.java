package de.KnollFrank.lib.settingssearch.provider;

import de.KnollFrank.lib.settingssearch.PreferencePath;

@FunctionalInterface
public interface ShowPreferencePath {

    boolean show(PreferencePath preferencePath);
}
