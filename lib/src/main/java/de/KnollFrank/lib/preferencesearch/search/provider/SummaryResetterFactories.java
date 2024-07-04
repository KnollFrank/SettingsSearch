package de.KnollFrank.lib.preferencesearch.search.provider;

import androidx.preference.Preference;

import java.util.Map;
import java.util.function.Function;

import de.KnollFrank.lib.preferencesearch.common.Maps;

public class SummaryResetterFactories {

    public final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass;

    public SummaryResetterFactories(final Map<Class<? extends Preference>, Function<Preference, ? extends ISummaryResetter>> summaryResetterFactoryByPreferenceClass) {
        this.summaryResetterFactoryByPreferenceClass = summaryResetterFactoryByPreferenceClass;
    }

    public SummaryResetterFactories combineWith(final SummaryResetterFactories other) {
        return new SummaryResetterFactories(
                Maps.merge(
                        this.summaryResetterFactoryByPreferenceClass,
                        other.summaryResetterFactoryByPreferenceClass));
    }
}
