package de.KnollFrank.lib.settingssearch.graph;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.Strings;

public class PreferenceFragmentLocalizedIdProvider implements PreferenceFragmentIdProvider {

    private final Locale locale;
    private final PreferenceFragmentIdProvider delegate;

    public PreferenceFragmentLocalizedIdProvider(final Locale locale, final PreferenceFragmentIdProvider delegate) {
        this.locale = locale;
        this.delegate = delegate;
    }

    @Override
    public String getId(final Fragment fragment) {
        return Strings.prefixIdWithLocale(delegate.getId(fragment), locale);
    }
}
