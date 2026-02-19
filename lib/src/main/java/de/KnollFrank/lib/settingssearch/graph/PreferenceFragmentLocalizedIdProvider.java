package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.common.Strings;

public class PreferenceFragmentLocalizedIdProvider implements PreferenceFragmentIdProvider {

    private final LanguageCode languageCode;
    private final PreferenceFragmentIdProvider delegate;

    public PreferenceFragmentLocalizedIdProvider(final LanguageCode languageCode, final PreferenceFragmentIdProvider delegate) {
        this.languageCode = languageCode;
        this.delegate = delegate;
    }

    @Override
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        return Strings.prefixIdWithLanguage(delegate.getId(preferenceFragment), languageCode);
    }
}
