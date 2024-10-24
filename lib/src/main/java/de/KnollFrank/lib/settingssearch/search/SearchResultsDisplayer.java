package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

class SearchResultsDisplayer {

    private final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final PreferenceScreen preferenceScreen;
    private final Context context;

    public SearchResultsDisplayer(
            final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
            final SearchableInfoAttribute searchableInfoAttribute,
            final PreferenceScreen preferenceScreen,
            final Context context) {
        this.pojoEntityMap = pojoEntityMap;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.preferenceScreen = preferenceScreen;
        this.context = context;
    }

    public void displaySearchResults(final List<PreferenceMatch> preferenceMatches) {
        highlight(preferenceMatches);
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    private void highlight(final List<PreferenceMatch> preferenceMatches) {
        final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                new PreferenceMatchesHighlighter(
                        () -> MarkupFactory.createMarkups(context),
                        pojoEntityMap,
                        searchableInfoAttribute);
        preferenceMatchesHighlighter.highlight(preferenceMatches);
    }

    private List<Preference> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> pojoEntityMap.get(preferenceMatch.preference()))
                .collect(Collectors.toList());
    }
}
