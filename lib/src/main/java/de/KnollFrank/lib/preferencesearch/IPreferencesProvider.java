package de.KnollFrank.lib.preferencesearch;

import java.util.List;

public interface IPreferencesProvider<T extends IPreferenceItem> {

    List<T> getPreferences();
}
