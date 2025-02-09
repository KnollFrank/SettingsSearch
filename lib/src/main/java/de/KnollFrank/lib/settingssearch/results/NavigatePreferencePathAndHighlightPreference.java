package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathPointer;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class NavigatePreferencePathAndHighlightPreference implements INavigatePreferencePathAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final @IdRes int fragmentContainerViewId;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;

    public NavigatePreferencePathAndHighlightPreference(final PreferencePathNavigator preferencePathNavigator,
                                                        final @IdRes int fragmentContainerViewId,
                                                        final PrepareShow prepareShow,
                                                        final FragmentManager fragmentManager) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void navigatePreferencePathAndHighlightPreference(final PreferencePathPointer preferencePathPointer) {
        preferencePathNavigator
                .navigatePreferencePath(preferencePathPointer)
                .ifPresent(
                        fragmentOfPreferenceScreen ->
                                showPreferenceScreenAndHighlightPreference(
                                        fragmentOfPreferenceScreen,
                                        preferencePathPointer.preferencePath.getPreference()));
    }

    private void showPreferenceScreenAndHighlightPreference(
            final PreferenceFragmentCompat fragmentOfPreferenceScreen,
            final SearchablePreference preference2Highlight) {
        prepareShow.prepareShow(fragmentOfPreferenceScreen);
        showFragment(
                fragmentOfPreferenceScreen,
                _fragmentOfPreferenceScreen ->
                        preference2Highlight.getKey().ifPresent(keyOfPreference2Highlight -> {
                            highlightPreference(_fragmentOfPreferenceScreen, keyOfPreference2Highlight);
                            showDialog(_fragmentOfPreferenceScreen.findPreference(keyOfPreference2Highlight), preference2Highlight);
                        }),
                true,
                fragmentContainerViewId,
                fragmentManager);
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
        // FK-TODO: siehe Implementierung von scrollToPreference() und ziehe den Code für den RecyclerView heraus.
        preferenceFragment.scrollToPreference(keyOfPreference2Highlight);
        PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                keyOfPreference2Highlight,
                preferenceFragment,
                Duration.ofSeconds(1));
    }

    private static void showDialog(final Preference preference, final SearchablePreference searchablePreference) {
        if (!searchablePreference.hasPreferenceMatchWithinSearchableInfo()) {
            return;
        }
        if (preference instanceof final DialogPreference dialogPreference) {
            dialogPreference.getPreferenceManager().showDialog(dialogPreference);
        } else if (preference.getOnPreferenceClickListener() != null) {
            // FK-TODO: or use "preference.performClick();" instead?
            preference.getOnPreferenceClickListener().onPreferenceClick(preference);
        }
    }
}
