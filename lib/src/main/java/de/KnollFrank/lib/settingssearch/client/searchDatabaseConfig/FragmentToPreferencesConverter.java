package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment;

@FunctionalInterface
public interface FragmentToPreferencesConverter {

    Optional<PreferencesOfFragment> getPreferences(Fragment fragment);
}
