package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummary.getOptionalSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceSummary.setSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceTitle.getOptionalTitle;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceTitle.setTitle;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoAttribute;

public class PreferenceScreenResetter {

    private final PreferenceScreen preferenceScreen;
    private final SearchableInfoAttribute searchableInfoAttribute;

    public PreferenceScreenResetter(final PreferenceScreen preferenceScreen,
                                    final SearchableInfoAttribute searchableInfoAttribute) {
        this.preferenceScreen = preferenceScreen;
        this.searchableInfoAttribute = searchableInfoAttribute;
    }

    public void reset() {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(this::reset);
    }

    private void reset(final Preference preference) {
        unhighlightTitle(preference);
        unhighlightSummary(preference);
        unhighlightSearchableInfo(preference);
    }

    private static void unhighlightTitle(final Preference preference) {
        setTitle(
                preference,
                unhighlight(getOptionalTitle(preference)));
    }

    private static void unhighlightSummary(final Preference preference) {
        setSummary(
                preference,
                unhighlight(getOptionalSummary(preference)));
    }

    private void unhighlightSearchableInfo(final Preference preference) {
        searchableInfoAttribute.setSearchableInfo(
                preference,
                unhighlight(searchableInfoAttribute.getSearchableInfo(preference)));
    }

    private static String unhighlight(final Optional<CharSequence> optionalCharSequence) {
        return optionalCharSequence
                .map(CharSequence::toString)
                .orElse(null);
    }
}
