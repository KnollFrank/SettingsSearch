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

import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.provider.ISearchableDialogInfoOfProvider;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class SearchablePreferenceTransformer {

    private final PreferenceManager preferenceManager;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceFragmentCompat host;
    private final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider;
    private final IsPreferenceSearchable isPreferenceSearchable;

    public SearchablePreferenceTransformer(final PreferenceManager preferenceManager,
                                           final SearchableInfoProvider searchableInfoProvider,
                                           final PreferenceFragmentCompat host,
                                           final ISearchableDialogInfoOfProvider searchableInfoByPreferenceProvider,
                                           final IsPreferenceSearchable isPreferenceSearchable) {
        this.preferenceManager = preferenceManager;
        this.searchableInfoProvider = searchableInfoProvider;
        this.host = host;
        this.searchableInfoByPreferenceProvider = searchableInfoByPreferenceProvider;
        this.isPreferenceSearchable = isPreferenceSearchable;
    }

    public SearchablePreferenceScreen transform2SearchablePreferenceScreen(final PreferenceScreen preferenceScreen) {
        final PreferenceScreen searchablePreferenceScreen = preferenceManager.createPreferenceScreen(preferenceManager.getContext());
        copyAttributes(preferenceScreen, searchablePreferenceScreen);
        final ImmutableMap.Builder<Preference, SearchablePreference> searchablePreferenceByPreferenceBuilder = ImmutableMap.builder();
        copyPreferences(preferenceScreen, searchablePreferenceScreen, searchablePreferenceByPreferenceBuilder);
        return new SearchablePreferenceScreen(searchablePreferenceScreen, searchablePreferenceByPreferenceBuilder.buildOrThrow());
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
                        getSearchableInfo(preference));
        copyAttributes(preference, searchablePreference);
        return searchablePreference;
    }

    // FK-TODO: refactor
    private Optional<String> getSearchableInfo(final Preference preference) {
        final Optional<String> searchableInfo = searchableInfoProvider.getSearchableInfo(preference);
        final Optional<String> searchableInfoOfDialogOfPreference = searchableInfoByPreferenceProvider.getSearchableDialogInfoOfPreference(preference, host);
        return searchableInfo.isPresent() || searchableInfoOfDialogOfPreference.isPresent() ?
                Optional.of(join(searchableInfo, searchableInfoOfDialogOfPreference, "\n")) :
                Optional.empty();
    }

    private static String join(final Optional<String> str1, final Optional<String> str2, final String delimiter) {
        return String.join(delimiter, Lists.getPresentElements(List.of(str1, str2)));
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
