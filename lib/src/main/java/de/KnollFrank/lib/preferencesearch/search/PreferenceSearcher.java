package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.Summaries4MatchingSearchableInfosAdapter.addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetter;

class PreferenceSearcher {

    private final PreferenceScreen preferenceScreen;
    private final PreferenceScreenResetter preferenceScreenResetter;
    private final Map<Preference, ? extends SummarySetter> summarySetterByPreference;
    private final ISearchableInfoProviderInternal searchableInfoProviderInternal;

    public PreferenceSearcher(final PreferenceScreen preferenceScreen,
                              final PreferenceScreenResetter preferenceScreenResetter,
                              final Map<Preference, ? extends SummarySetter> summarySetterByPreference,
                              final ISearchableInfoProviderInternal searchableInfoProviderInternal) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceScreenResetter = preferenceScreenResetter;
        this.summarySetterByPreference = summarySetterByPreference;
        this.searchableInfoProviderInternal = searchableInfoProviderInternal;
    }

    public static PreferenceSearcher createPreferenceSearcher(
            final MergedPreferenceScreen mergedPreferenceScreen,
            final ISearchableInfoProviderInternal searchableInfoProviderInternal) {
        return new PreferenceSearcher(
                mergedPreferenceScreen.preferenceScreen,
                new PreferenceScreenResetter(mergedPreferenceScreen.summaryResetterByPreference),
                mergedPreferenceScreen.summaryResetterByPreference,
                searchableInfoProviderInternal);
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private void prepareSearch(final String needle) {
        preferenceScreenResetter.reset(preferenceScreen);
        addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoProviderInternal,
                summarySetterByPreference,
                needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                Preferences
                        .getAllPreferences(preferenceScreen)
                        .stream()
                        .filter(preference -> !(preference instanceof PreferenceGroup))
                        .map(preference -> PreferenceMatcher.getPreferenceMatches(preference, needle))
                        .collect(Collectors.toList()));
    }
}
