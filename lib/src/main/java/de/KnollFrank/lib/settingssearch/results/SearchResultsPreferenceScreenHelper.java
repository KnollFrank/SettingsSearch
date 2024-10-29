package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.results.PreferencesDisabler.disablePreferences;
import static de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceScreenFromPOJOConverter.PreferenceScreenWithMap;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.search.MarkupFactory;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatchesHighlighter;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

// FK-TODO: refactor
public class SearchResultsPreferenceScreenHelper {

    private SearchResultsDescription searchResultsDescription;
    private final Context context;
    private final PreferencePathNavigator preferencePathNavigator;
    private final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    protected SearchResultsPreferenceScreenHelper(final PreferencePathNavigator preferencePathNavigator,
                                                  final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                                  final Context context,
                                                  final SearchResultsDescription searchResultsDescription) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.preferencePathByPreferenceFactory = preferencePathByPreferenceFactory;
        this.context = context;
        this.searchResultsDescription = searchResultsDescription;
    }

    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(searchResultsDescription.preferenceScreenWithMap().preferenceScreen());
    }

    public SearchResultsDescription displaySearchResults(final List<PreferenceMatch> preferenceMatches, final String query) {
        final SearchResultsDescription oldSearchResultsDescription = searchResultsDescription;
        searchResultsDescription = getSearchResultsDescription(preferenceMatches, query);
        propertyChangeSupport.firePropertyChange("SearchResultsDescription", oldSearchResultsDescription, searchResultsDescription);
        return searchResultsDescription;
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(searchResultsDescription.preferencePathByPreference().get(preference));
    }

    public SearchResultsDescription getSearchResultsDescription() {
        return searchResultsDescription;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private SearchResultsDescription getSearchResultsDescription(
            final List<PreferenceMatch> preferenceMatches,
            final String query) {
        final PreferenceScreen preferenceScreen = searchResultsDescription.preferenceScreenWithMap().preferenceScreen();
        final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                addPreferenceMatches2PreferenceScreen(
                        preferenceScreen,
                        preferenceMatches,
                        query,
                        searchableInfoAttribute);
        return new SearchResultsDescription(
                new PreferenceScreenWithMap(preferenceScreen, pojoEntityMap),
                preferencePathByPreferenceFactory.apply(pojoEntityMap),
                searchableInfoAttribute);
    }

    private BiMap<SearchablePreferencePOJO, SearchablePreference> addPreferenceMatches2PreferenceScreen(
            final PreferenceScreen preferenceScreen,
            final List<PreferenceMatch> preferenceMatches,
            final String query,
            final SearchableInfoAttribute searchableInfoAttribute) {
        preferenceScreen.removeAll();
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                        getPreferences(preferenceMatches),
                        preferenceScreen);
        disablePreferences(preferenceScreen);
        setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoAttribute,
                query);
        {
            final PreferenceMatchesHighlighter preferenceMatchesHighlighter =
                    new PreferenceMatchesHighlighter(
                            () -> MarkupFactory.createMarkups(context),
                            pojoEntityMap,
                            searchableInfoAttribute);
            preferenceMatchesHighlighter.highlight(preferenceMatches);
        }
        return pojoEntityMap;
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .distinct()
                .collect(Collectors.toList());
    }
}
