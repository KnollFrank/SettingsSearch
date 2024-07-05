package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.function.Function;

public class PreferenceDescription<T extends Preference> {

    public final Class<T> preferenceClass;
    public final SearchableInfoProvider<T> searchableInfoProvider;
    public final ISummarySetter<T> summarySetter;
    public final Function<T, ? extends ISummaryResetter> summaryResetterFactory;

    public PreferenceDescription(
            final Class<T> preferenceClass,
            final SearchableInfoProvider<T> searchableInfoProvider,
            final ISummarySetter<T> summarySetter,
            final Function<T, ? extends ISummaryResetter> summaryResetterFactory) {
        this.preferenceClass = preferenceClass;
        this.searchableInfoProvider = searchableInfoProvider;
        this.summarySetter = summarySetter;
        this.summaryResetterFactory = summaryResetterFactory;
    }
}
