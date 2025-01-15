package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.threeten.bp.Duration;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class ShowPreferenceScreenAndHighlightPreference implements IShowPreferenceScreenAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final @IdRes int fragmentContainerViewId;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;

    public ShowPreferenceScreenAndHighlightPreference(final PreferencePathNavigator preferencePathNavigator,
                                                      final @IdRes int fragmentContainerViewId,
                                                      final PrepareShow prepareShow,
                                                      final FragmentManager fragmentManager) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void showPreferenceScreenAndHighlightPreference(final SearchablePreference preference) {
        showPreferenceScreenAndHighlightPreference(
                preferencePathNavigator.navigatePreferencePath(preference.getPreferencePath()),
                preference.getKey());
    }

    private void showPreferenceScreenAndHighlightPreference(
            final PreferenceFragmentCompat fragmentOfPreferenceScreen,
            final Optional<String> keyOfPreference2Highlight) {
        prepareShow.prepareShow(fragmentOfPreferenceScreen);
        showFragment(
                fragmentOfPreferenceScreen,
                _fragmentOfPreferenceScreen ->
                        keyOfPreference2Highlight.ifPresent(
                                _keyOfPreference2Highlight -> {
                                    highlightPreference(
                                            _fragmentOfPreferenceScreen,
                                            _keyOfPreference2Highlight);
                                    showDialog(
                                            _fragmentOfPreferenceScreen,
                                            _keyOfPreference2Highlight);
                                }),
                true,
                fragmentContainerViewId,
                fragmentManager);
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
        preferenceFragment.scrollToPreference(keyOfPreference2Highlight);
        PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                keyOfPreference2Highlight,
                preferenceFragment,
                Duration.ofSeconds(1));
    }

    private static void showDialog(final PreferenceFragmentCompat preferenceFragment, final String keyOfPreference) {
        final Preference preference = preferenceFragment.findPreference(keyOfPreference);
        if (preference instanceof DialogPreference dialogPreference) {
            dialogPreference.getPreferenceManager().showDialog(dialogPreference);
        } else if (preference.getOnPreferenceClickListener() != null) {
            preference.performClick();
        }
    }
}
