package de.KnollFrank.lib.settingssearch.test;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentToPreferencesConverter;
import de.KnollFrank.lib.settingssearch.graph.PreferencesOfFragment;

public class TestFragmentToPreferencesConverter {

    public static final FragmentToPreferencesConverter INSTANCE = fragment -> {
        if (fragment instanceof final PreferenceFragmentCompat p) {
            return Optional.of(
                    new PreferencesOfFragment(
                            de.KnollFrank.lib.settingssearch.common.Preferences.getImmediateChildren(p.getPreferenceScreen()),
                            Optional.ofNullable(p.getPreferenceScreen().getTitle()).map(Object::toString),
                            Optional.ofNullable(p.getPreferenceScreen().getSummary()).map(Object::toString)));
        }
        return Optional.empty();
    };
}
