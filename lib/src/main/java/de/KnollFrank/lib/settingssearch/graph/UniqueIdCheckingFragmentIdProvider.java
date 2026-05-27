package de.KnollFrank.lib.settingssearch.graph;

import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentIdProvider;

class UniqueIdCheckingFragmentIdProvider implements FragmentIdProvider {

    private final Set<String> ids = new HashSet<>();
    private final FragmentIdProvider delegate;

    public UniqueIdCheckingFragmentIdProvider(final FragmentIdProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId(final Fragment fragment) {
        final String id = delegate.getId(fragment);
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
