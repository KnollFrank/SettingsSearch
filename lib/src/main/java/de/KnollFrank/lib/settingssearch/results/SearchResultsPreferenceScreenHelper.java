package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
import de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

// FK-TODO: refactor
public class SearchResultsPreferenceScreenHelper {

    private Info info;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final Context context;
    private final PreferencePathNavigator preferencePathNavigator;
    private final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory;

    public record Info(PreferenceScreenWithMap preferenceScreenWithMap,
                       Map<Preference, PreferencePath> preferencePathByPreference,
                       SearchableInfoAttribute searchableInfoAttribute) {
    }

    public SearchResultsPreferenceScreenHelper(final PreferenceManager preferenceManager,
                                               final PreferencePathNavigator preferencePathNavigator,
                                               final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                               final Context context) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.preferencePathByPreferenceFactory = preferencePathByPreferenceFactory;
        this.context = context;
        this.info = createInitialInfo(preferenceManager, preferencePathByPreferenceFactory);
    }

    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(info.preferenceScreenWithMap().preferenceScreen());
    }

    public Info displaySearchResults(final List<PreferenceMatch> preferenceMatches, final String query) {
        final Info oldInfo = info;
        info = getInfo(preferenceMatches, query);
        // FK-TODO: inline method displaySearchResults()
        createSearchResultsDisplayer().displaySearchResults(preferenceMatches);
        propertyChangeSupport.firePropertyChange("info", oldInfo, info);
        return info;
    }

    public Map<Preference, PreferencePath> getPreferencePathByPreference() {
        return info.preferencePathByPreference();
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public SearchableInfoAttribute getSearchableInfoAttribute() {
        return info.searchableInfoAttribute();
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(info.preferencePathByPreference().get(preference));
    }

    private static Info createInitialInfo(
            final PreferenceManager preferenceManager,
            final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory) {
        final PreferenceScreenWithMap preferenceScreenWithMap = createEmptyPreferenceScreenWithMap(preferenceManager);
        return new Info(
                preferenceScreenWithMap,
                preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap()),
                new SearchableInfoAttribute());
    }

    private static PreferenceScreenWithMap createEmptyPreferenceScreenWithMap(final PreferenceManager preferenceManager) {
        return new PreferenceScreenWithMap(
                preferenceManager.createPreferenceScreen(preferenceManager.getContext()),
                HashBiMap.create());
    }

    private SearchResultsDisplayer createSearchResultsDisplayer() {
        return new SearchResultsDisplayer(
                info.preferenceScreenWithMap().pojoEntityMap(),
                info.searchableInfoAttribute(),
                context);
    }

    private Info getInfo(final List<PreferenceMatch> preferenceMatches,
                         final String query) {
        final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                getPreferenceScreenWithMap(
                        info.preferenceScreenWithMap().preferenceScreen(),
                        preferenceMatches,
                        query,
                        searchableInfoAttribute);
        return new Info(
                new PreferenceScreenWithMap(
                        info.preferenceScreenWithMap().preferenceScreen(),
                        pojoEntityMap),
                preferencePathByPreferenceFactory.apply(pojoEntityMap),
                searchableInfoAttribute);
    }

    private static BiMap<SearchablePreferencePOJO, SearchablePreference> getPreferenceScreenWithMap(
            final PreferenceScreen preferenceScreen,
            final List<PreferenceMatch> preferenceMatches,
            final String query,
            final SearchableInfoAttribute searchableInfoAttribute) {
        preferenceScreen.removeAll();
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                        getPreferences(preferenceMatches),
                        preferenceScreen);
        PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch(preferenceScreen);
        MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoAttribute,
                query);
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
