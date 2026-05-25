package de.KnollFrank.lib.settingssearch.test;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentToPreferencesConverter;
import de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment;

public class TestFragmentToPreferencesConverter {

    public static final FragmentToPreferencesConverter INSTANCE =
            fragment ->
                    fragment instanceof final PreferenceFragmentCompat preferenceFragment ?
                            Optional.of(
                                    new PreferencesOfFragment(
                                            de.KnollFrank.lib.settingssearch.common.Preferences.getImmediateChildren(preferenceFragment.getPreferenceScreen()),
                                            Optional.ofNullable(preferenceFragment.getPreferenceScreen().getTitle()).map(Object::toString),
                                            Optional.ofNullable(preferenceFragment.getPreferenceScreen().getSummary()).map(Object::toString))) :
                            Optional.empty();
}
