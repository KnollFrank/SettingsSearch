package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.fragment.app.Fragment;

import java.util.Optional;

public class DefaultFragmentIdProvider implements FragmentIdProvider {

    @Override
    public String getId(final Fragment fragment) {
        final String className = fragment.getClass().getName();
        return Optional
                .ofNullable(fragment.getArguments())
                .map(arguments -> className + " " + arguments)
                .orElse(className);
    }
}
