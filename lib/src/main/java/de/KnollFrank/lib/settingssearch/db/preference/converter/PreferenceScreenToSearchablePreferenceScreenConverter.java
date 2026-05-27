package de.KnollFrank.lib.settingssearch.db.preference.converter;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.google.common.collect.BiMap;

import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.FragmentClassOfActivity;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: Stelle die vorherige Version dieser Klasse wieder her, die war um Klassen besser.
public class PreferenceScreenToSearchablePreferenceScreenConverter {

    private final PreferenceToSearchablePreferenceConverter preferenceToSearchablePreferenceConverter;

    public PreferenceScreenToSearchablePreferenceScreenConverter(final PreferenceToSearchablePreferenceConverter preferenceToSearchablePreferenceConverter) {
        this.preferenceToSearchablePreferenceConverter = preferenceToSearchablePreferenceConverter;
    }

    public SearchablePreferenceScreenWithMap convertPreferenceScreen(
            // FK-TODO: refactor by using PreferenceScreenOfHostOfActivity preferenceScreen for the following 5 parameters
            final List<Preference> preferences,
            final Fragment host,
            final ActivityDescription activityDescription,
            final Optional<String> title,
            final Optional<String> summary,
            final String id) {
        final BiMap<SearchablePreference, Preference> searchablePreferences =
                preferenceToSearchablePreferenceConverter.convertPreferences(
                        preferences,
                        List.of(),
                        id,
                        host);
        return new SearchablePreferenceScreenWithMap(
                new SearchablePreferenceScreen(
                        id,
                        // FK-TODO: use preferenceScreen.asPreferenceFragmentOfActivity().asFragmentClassOfActivity()
                        new FragmentClassOfActivity<>(
                                (Class<Fragment>) host.getClass(),
                                activityDescription),
                        Strings.stringToString(title),
                        Strings.stringToString(summary),
                        searchablePreferences.keySet()),
                searchablePreferences);
    }

    public SearchablePreferenceScreenWithMap convertPreferenceScreen(
            final PreferenceScreenOfHostOfActivity preferenceScreen,
            final String id) {
        return convertPreferenceScreen(
                preferenceScreen.preferences(),
                preferenceScreen.hostOfPreferenceScreen(),
                preferenceScreen.activityOfHost(),
                preferenceScreen.title(),
                preferenceScreen.summary(),
                id);
    }
}
