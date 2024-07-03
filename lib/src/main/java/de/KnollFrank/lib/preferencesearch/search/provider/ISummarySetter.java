package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

@FunctionalInterface
public interface ISummarySetter<T extends Preference> {

    void setSummary(T preference, CharSequence summary);
}
