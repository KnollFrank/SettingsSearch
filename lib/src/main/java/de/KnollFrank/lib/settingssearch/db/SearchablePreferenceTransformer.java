package de.KnollFrank.lib.settingssearch.db;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;

public class SearchablePreferenceTransformer {

    private final PreferenceManager preferenceManager;
    private final PreferenceFragmentCompat host;
    // FK-TODO: remove instance variable isPreferenceSearchable
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider;

    public SearchablePreferenceTransformer(final PreferenceManager preferenceManager,
                                           final PreferenceFragmentCompat host,
                                           final IsPreferenceSearchable isPreferenceSearchable,
                                           final SearchableInfoAndDialogInfoProvider searchableInfoAndDialogInfoProvider) {
        this.preferenceManager = preferenceManager;
        this.host = host;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoAndDialogInfoProvider = searchableInfoAndDialogInfoProvider;
    }

    public SearchablePreferenceScreenWithMap transform2SearchablePreferenceScreen(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen searchablePreferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        copyAttributes(preferenceScreen, searchablePreferenceScreen);
        final ImmutableMap.Builder<Preference, SearchablePreference> searchablePreferenceByPreferenceBuilder = ImmutableMap.builder();
        copyPreferences(preferenceScreen, searchablePreferenceScreen, searchablePreferenceByPreferenceBuilder);
        return new SearchablePreferenceScreenWithMap(searchablePreferenceScreen, searchablePreferenceByPreferenceBuilder.buildOrThrow());
    }

    private void copyPreferences(final PreferenceGroup src,
                                 final PreferenceGroup dst,
                                 final ImmutableMap.Builder<Preference, SearchablePreference> searchablePreferenceByPreferenceBuilder) {
        for (final Preference child : getFilteredDirectChildren(src)) {
            final SearchablePreference searchablePreference = createSearchablePreferenceWithAttributes(child);
            searchablePreferenceByPreferenceBuilder.put(child, searchablePreference);
            dst.addPreference(searchablePreference);
            if (child instanceof final PreferenceGroup childPreferenceGroup) {
                copyPreferences(childPreferenceGroup, searchablePreference, searchablePreferenceByPreferenceBuilder);
            }
        }
    }

    private List<Preference> getFilteredDirectChildren(final PreferenceGroup src) {
        return Preferences
                .getDirectChildren(src)
                .stream()
                .filter(preference -> isPreferenceSearchable.isPreferenceOfHostSearchable(preference, host))
                .collect(Collectors.toList());
    }

    private SearchablePreference createSearchablePreferenceWithAttributes(final Preference preference) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        preference.getContext(),
                        searchableInfoAndDialogInfoProvider.getSearchableInfo(preference, host),
                        Optional.empty());
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
        dst.setVisible(src.isVisible());
    }
}
