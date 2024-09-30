package de.KnollFrank.lib.settingssearch.provider;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ConnectedPreferenceScreens;
import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.settingssearch.PreferenceScreensProvider;
import de.KnollFrank.lib.settingssearch.fragment.Fragments;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogs;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreenProvider {

    private final Fragments fragments;
    private final PreferenceDialogs preferenceDialogs;
    private final PreferenceScreensProvider preferenceScreensProvider;
    private final PreferenceScreensMerger preferenceScreensMerger;
    private final IsPreferenceSearchable isPreferenceSearchable;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider;
    private final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener;
    private final boolean cacheMergedPreferenceScreens;

    private static final Map<String, MergedPreferenceScreen> mergedPreferenceScreenByFragment = new HashMap<>();

    public MergedPreferenceScreenProvider(final Fragments fragments,
                                          final PreferenceDialogs preferenceDialogs,
                                          final PreferenceScreensProvider preferenceScreensProvider,
                                          final PreferenceScreensMerger preferenceScreensMerger,
                                          final IsPreferenceSearchable isPreferenceSearchable,
                                          final SearchableInfoAttribute searchableInfoAttribute,
                                          final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider,
                                          final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener,
                                          final boolean cacheMergedPreferenceScreens) {
        this.fragments = fragments;
        this.preferenceDialogs = preferenceDialogs;
        this.preferenceScreensProvider = preferenceScreensProvider;
        this.preferenceScreensMerger = preferenceScreensMerger;
        this.isPreferenceSearchable = isPreferenceSearchable;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        this.cacheMergedPreferenceScreens = cacheMergedPreferenceScreens;
    }

    public MergedPreferenceScreen getMergedPreferenceScreen(final String rootPreferenceFragment) {
        if (!cacheMergedPreferenceScreens) {
            return computeMergedPreferenceScreen(rootPreferenceFragment);
        }
        if (!mergedPreferenceScreenByFragment.containsKey(rootPreferenceFragment)) {
            mergedPreferenceScreenByFragment.put(rootPreferenceFragment, computeMergedPreferenceScreen(rootPreferenceFragment));
        }
        return mergedPreferenceScreenByFragment.get(rootPreferenceFragment);
    }

    private MergedPreferenceScreen computeMergedPreferenceScreen(final String rootPreferenceFragment) {
        return getMergedPreferenceScreen(
                (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(
                        rootPreferenceFragment,
                        Optional.empty()));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final PreferenceFragmentCompat root) {
        return getMergedPreferenceScreen(getConnectedPreferenceScreens(root));
    }

    private MergedPreferenceScreen getMergedPreferenceScreen(final ConnectedPreferenceScreens screens) {
        // MUST compute A (which just reads screens) before B (which modifies screens)
        // A:
        final Map<Preference, PreferenceFragmentCompat> hostByPreference =
                HostByPreferenceProvider.getHostByPreference(screens.getConnectedPreferenceScreens());
        final Map<Preference, String> searchableInfoByPreference =
                new SearchableInfoByPreferenceProvider(preferenceDialogs, preferenceDialogAndSearchableInfoProvider)
                        .getSearchableInfoByPreference(screens.getConnectedPreferenceScreens());
        // B:
        final PreferenceScreensMerger.PreferenceScreenAndIsNonClickable preferenceScreenAndIsNonClickable = destructivelyMergeScreens(screens.getConnectedPreferenceScreens());
        return new MergedPreferenceScreen(
                preferenceScreenAndIsNonClickable.preferenceScreen(),
                preferenceScreenAndIsNonClickable.isNonClickable(),
                hostByPreference,
                searchableInfoByPreference,
                screens.preferencePathByPreference,
                searchableInfoAttribute);
    }

    private ConnectedPreferenceScreens getConnectedPreferenceScreens(final PreferenceFragmentCompat root) {
        final ConnectedPreferenceScreens screens = preferenceScreensProvider.getConnectedPreferenceScreens(root);
        removeInvisiblePreferences(screens.getConnectedPreferenceScreens());
        removeNonSearchablePreferences(screens.getConnectedPreferenceScreens());
        preferenceScreenGraphAvailableListener.onPreferenceScreenGraphWithoutInvisibleAndNonSearchablePreferencesAvailable(screens.preferenceScreenGraph);
        return screens;
    }

    private static void removeInvisiblePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, MergedPreferenceScreenProvider::isInvisible);
    }

    private static boolean isInvisible(final Preference preference,
                                       final PreferenceFragmentCompat host) {
        return !preference.isVisible();
    }

    private void removeNonSearchablePreferences(final Set<PreferenceScreenWithHost> screens) {
        PreferencesRemover.removePreferences(screens, this::isNonSearchable);
    }

    private boolean isNonSearchable(final Preference preference,
                                    final PreferenceFragmentCompat host) {
        return !isPreferenceSearchable.isPreferenceOfHostSearchable(preference, host);
    }

    private PreferenceScreensMerger.PreferenceScreenAndIsNonClickable destructivelyMergeScreens(final Set<PreferenceScreenWithHost> screens) {
        return preferenceScreensMerger.destructivelyMergeScreens(getPreferenceScreens(new ArrayList<>(screens)));
    }

    private static List<PreferenceScreen> getPreferenceScreens(final List<PreferenceScreenWithHost> screens) {
        return screens
                .stream()
                .map(PreferenceScreenWithHost::preferenceScreen)
                .collect(Collectors.toList());
    }
}
