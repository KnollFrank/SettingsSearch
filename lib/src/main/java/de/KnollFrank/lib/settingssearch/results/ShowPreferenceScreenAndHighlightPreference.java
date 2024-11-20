package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import org.threeten.bp.Duration;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;

public class ShowPreferenceScreenAndHighlightPreference implements IShowPreferenceScreenAndHighlightPreference {

    private final PreferencePathNavigator preferencePathNavigator;
    private final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference;
    private final @IdRes int fragmentContainerViewId;
    private final PrepareShow prepareShow;
    private final FragmentManager fragmentManager;

    public ShowPreferenceScreenAndHighlightPreference(
            final PreferencePathNavigator preferencePathNavigator,
            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
            final @IdRes int fragmentContainerViewId,
            final PrepareShow prepareShow,
            final FragmentManager fragmentManager) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.preferencePathByPreference = preferencePathByPreference;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.prepareShow = prepareShow;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void showPreferenceScreenAndHighlightPreference(final SearchablePreferencePOJO preference) {
        showPreferenceScreenAndHighlightPreference(
                preferencePathNavigator.navigatePreferencePath(preferencePathByPreference.get(preference)),
                preference.key());
    }

    private void showPreferenceScreenAndHighlightPreference(
            final PreferenceFragmentCompat fragmentOfPreferenceScreen,
            final Optional<String> keyOfPreference2Highlight) {
        prepareShow.prepareShow(fragmentOfPreferenceScreen);
        showFragment(
                fragmentOfPreferenceScreen,
                _fragmentOfPreferenceScreen ->
                        keyOfPreference2Highlight.ifPresent(
                                _keyOfPreference2Highlight ->
                                        highlightPreference(
                                                _fragmentOfPreferenceScreen,
                                                _keyOfPreference2Highlight)),
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
}
