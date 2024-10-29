package de.KnollFrank.lib.settingssearch.results;

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
import java.util.function.Supplier;
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

    public SearchResultsPreferenceScreenHelper(final Supplier<PreferenceScreenWithMap> preferenceScreenWithMapFactory,
                                               final PreferencePathNavigator preferencePathNavigator,
                                               final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                               final Context context) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.preferencePathByPreferenceFactory = preferencePathByPreferenceFactory;
        this.info = createInfo(preferenceScreenWithMapFactory);
        this.context = context;
    }

    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(info.preferenceScreenWithMap().preferenceScreen());
    }

    public void displaySearchResults(final List<PreferenceMatch> preferenceMatches,
                                     final String query) {
        final Info oldInfo = info;
        final Info newInfo =
                getInfo(
                        info.preferenceScreenWithMap().preferenceScreen(),
                        preferenceMatches,
                        query);
        // FK-TODO: inline method displaySearchResults()
        createSearchResultsDisplayer(newInfo).displaySearchResults(preferenceMatches);
        propertyChangeSupport.firePropertyChange("info", oldInfo, newInfo);
        info = newInfo;
    }

    private Info getInfo(final PreferenceScreen preferenceScreen,
                         final List<PreferenceMatch> preferenceMatches,
                         final String query) {
        final SearchableInfoAttribute searchableInfoAttribute = new SearchableInfoAttribute();
        final BiMap<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap =
                getPreferenceScreenWithMap(
                        preferenceScreen,
                        preferenceMatches,
                        query,
                        searchableInfoAttribute);
        return new Info(
                new PreferenceScreenWithMap(preferenceScreen, pojoEntityMap),
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

    public void displayPreferenceMatchesOnPreferenceScreen(final List<PreferenceMatch> preferenceMatches) {
        info.preferenceScreenWithMap().preferenceScreen().removeAll();
        SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                getPreferences(preferenceMatches),
                info.preferenceScreenWithMap().preferenceScreen());
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

    private SearchResultsDisplayer createSearchResultsDisplayer(final Info info) {
        return new SearchResultsDisplayer(
                info.preferenceScreenWithMap().pojoEntityMap(),
                info.searchableInfoAttribute(),
                context);
    }

    private Info createInfo(final Supplier<PreferenceScreenWithMap> preferenceScreenWithMapFactory) {
        final PreferenceScreenWithMap preferenceScreenWithMap = preferenceScreenWithMapFactory.get();
        return new Info(
                preferenceScreenWithMap,
                preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap()),
                new SearchableInfoAttribute());
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .distinct()
                .collect(Collectors.toList());
    }
}
