package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import java.util.HashSet;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;

class UniqueIdCheckingPreferenceFragmentIdProvider implements PreferenceFragmentIdProvider {

    private final Set<String> ids = new HashSet<>();
    private final PreferenceFragmentIdProvider delegate;

    public UniqueIdCheckingPreferenceFragmentIdProvider(final PreferenceFragmentIdProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        final String id = delegate.getId(preferenceFragment);
        assertUnique(id);
        return id;
    }

    private void assertUnique(final String id) {
        if (ids.contains(id)) {
            throw new IllegalStateException("Duplicate id: " + id);
        }
        ids.add(id);
    }
}
