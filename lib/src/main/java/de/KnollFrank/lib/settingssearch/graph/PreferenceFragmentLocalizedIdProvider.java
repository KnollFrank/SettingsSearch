package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

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
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        return Strings.prefixIdWithLanguage(delegate.getId(preferenceFragment), locale);
    }
}
