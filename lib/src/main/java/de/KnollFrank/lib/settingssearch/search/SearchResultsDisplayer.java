package de.KnollFrank.lib.settingssearch.search;

import android.content.Context;

import java.util.List;
import java.util.Map;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsDisplayer {

    private final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final Context context;

    public SearchResultsDisplayer(
            final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
            final SearchableInfoAttribute searchableInfoAttribute,
            final Context context) {
        this.pojoEntityMap = pojoEntityMap;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.context = context;
    }

    public void displaySearchResults(final List<PreferenceMatch> preferenceMatches) {
        highlight(preferenceMatches);
    }

    private void highlight(final List<PreferenceMatch> preferenceMatches) {
        final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                new PreferenceMatchesHighlighter(
                        () -> MarkupFactory.createMarkups(context),
                        pojoEntityMap,
                        searchableInfoAttribute);
        preferenceMatchesHighlighter.highlight(preferenceMatches);
    }
}
