package de.KnollFrank.lib.settingssearch.graph;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.FragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.Strings;

public class FragmentLocalizedIdProvider implements FragmentIdProvider {

    private final Locale locale;
    private final FragmentIdProvider delegate;

    public FragmentLocalizedIdProvider(final Locale locale, final FragmentIdProvider delegate) {
        this.locale = locale;
        this.delegate = delegate;
    }

    @Override
    public String getId(final Fragment fragment) {
        return Strings.prefixIdWithLocale(delegate.getId(fragment), locale);
    }
}
