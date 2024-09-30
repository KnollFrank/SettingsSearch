package de.KnollFrank.lib.settingssearch.db;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchablePreferenceTransformer {

    private final PreferenceManager preferenceManager;
    private final SearchableInfoProvider searchableInfoProvider;

    public SearchablePreferenceTransformer(final PreferenceManager preferenceManager,
                                           final SearchableInfoProvider searchableInfoProvider) {
        this.preferenceManager = preferenceManager;
        this.searchableInfoProvider = searchableInfoProvider;
    }

    public PreferenceScreen transform2SearchablePreferenceScreen(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen searchablePreferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        copyAttributes(preferenceScreen, searchablePreferenceScreen);
        copyPreferences(preferenceScreen, searchablePreferenceScreen);
        return searchablePreferenceScreen;
    }

    private void copyPreferences(final PreferenceGroup src, final PreferenceGroup dst) {
        for (final Preference child : Preferences.getDirectChildren(src)) {
            final SearchablePreference searchablePreference = createSearchablePreferenceWithAttributes(child);
            dst.addPreference(searchablePreference);
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                copyPreferences(childPreferenceGroup, searchablePreference);
            }
        }
    }

    private SearchablePreference createSearchablePreferenceWithAttributes(final Preference preference) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        preference.getContext(),
                        searchableInfoProvider.getSearchableInfo(preference));
        copyAttributes(preference, searchablePreference);
        return searchablePreference;
    }

    private static void copyAttributes(final Preference src, final Preference dst) {
        dst.setKey(src.getKey());
        dst.setIcon(src.getIcon());
        dst.setLayoutResource(src.getLayoutResource());
        dst.setSummary(src.getSummary());
        dst.setTitle(src.getTitle());
        dst.setWidgetLayoutResource(src.getWidgetLayoutResource());
        dst.setFragment(src.getFragment());
        dst.getExtras().putAll(src.getExtras());
    }
}
