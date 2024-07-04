package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;

public class SummaryResetterFactories {

    public final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass;

    public SummaryResetterFactories(final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass) {
        this.summaryResetterFactoryByPreferenceClass = summaryResetterFactoryByPreferenceClass;
    }

    public SummaryResetterFactories combineWith(final SummaryResetterFactories other) {
        return new SummaryResetterFactories(
                ImmutableMap
                        .<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>>builder()
                        .putAll(this.summaryResetterFactoryByPreferenceClass)
                        .putAll(other.summaryResetterFactoryByPreferenceClass)
                        .build());
    }
}
