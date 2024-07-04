package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.function.Function;

public class CustomPreferenceDescription {

    public final Class<? extends Preference> preferenceClass;
    public final SearchableInfoProvider<?> searchableInfoProvider;
    public final ISummarySetter summarySetter;
    public final Function<Preference, ? extends ISummaryResetter> summaryResetterFactory;

    public CustomPreferenceDescription(
            final Class<? extends Preference> preferenceClass,
            final SearchableInfoProvider<?> searchableInfoProvider,
            final ISummarySetter summarySetter,
            final Function<Preference, ? extends ISummaryResetter> summaryResetterFactory) {
        this.preferenceClass = preferenceClass;
        this.searchableInfoProvider = searchableInfoProvider;
        this.summarySetter = summarySetter;
        this.summaryResetterFactory = summaryResetterFactory;
    }
}
