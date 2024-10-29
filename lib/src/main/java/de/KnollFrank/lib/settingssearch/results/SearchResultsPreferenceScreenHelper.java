package de.KnollFrank.lib.settingssearch.results;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.SearchResultsDisplayer;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreenHelper {

    private final Info info;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final Context context;
    private final PreferencePathNavigator preferencePathNavigator;

    public record Info(SearchableInfoAttribute searchableInfoAttribute,
                       PreferenceScreenWithMap preferenceScreenWithMap,
                       Map<Preference, PreferencePath> preferencePathByPreference) {
    }

    public SearchResultsPreferenceScreenHelper(final Supplier<PreferenceScreenWithMap> preferenceScreenWithMapFactory,
                                               final PreferencePathNavigator preferencePathNavigator,
                                               final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory,
                                               final Context context) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.info = createInfo(preferenceScreenWithMapFactory, preferencePathByPreferenceFactory);
        this.context = context;
    }

    // 1:
    public void setPreferenceScreen(final PreferenceFragmentCompat preferenceFragment) {
        preferenceFragment.setPreferenceScreen(info.preferenceScreenWithMap().preferenceScreen());
    }

    // 2:
    public void displaySearchResults(final List<PreferenceMatch> preferenceMatches,
                                     final String query) {
        PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch(info.preferenceScreenWithMap().preferenceScreen());
        new PreferenceScreenResetter(info.preferenceScreenWithMap().preferenceScreen(), info.searchableInfoAttribute()).resetPreferenceScreen();
        MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                info.preferenceScreenWithMap().preferenceScreen(),
                info.searchableInfoAttribute(),
                query);
        createSearchResultsDisplayer().displaySearchResults(preferenceMatches);
        propertyChangeSupport.firePropertyChange("info", null, info);
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

    private SearchResultsDisplayer createSearchResultsDisplayer() {
        return new SearchResultsDisplayer(
                info.preferenceScreenWithMap().pojoEntityMap(),
                info.searchableInfoAttribute(),
                info.preferenceScreenWithMap().preferenceScreen(),
                context);
    }

    private static Info createInfo(
            final Supplier<PreferenceScreenWithMap> preferenceScreenWithMapFactory,
            final Function<BiMap<SearchablePreferencePOJO, SearchablePreference>, Map<Preference, PreferencePath>> preferencePathByPreferenceFactory) {
        final PreferenceScreenWithMap preferenceScreenWithMap = preferenceScreenWithMapFactory.get();
        return new Info(
                new SearchableInfoAttribute(),
                preferenceScreenWithMap,
                preferencePathByPreferenceFactory.apply(preferenceScreenWithMap.pojoEntityMap()));
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }
}
