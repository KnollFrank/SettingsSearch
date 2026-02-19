package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.common.LanguageCode;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;

public class ContinueWithPreferencePathNavigation<C> {

    private final FragmentActivity activity;
    private final ViewGroup parent;
    private final @IdRes int fragmentContainerViewId;
    private final Function<Consumer<MergedPreferenceScreen<C>>, SearchPreferenceFragments<C>> createSearchPreferenceFragments;
    private final LanguageCode languageCode;
    private final C actualConfiguration;

    private ContinueWithPreferencePathNavigation(final FragmentActivity activity,
                                                 final ViewGroup parent,
                                                 final @IdRes int fragmentContainerViewId,
                                                 final Function<Consumer<MergedPreferenceScreen<C>>, SearchPreferenceFragments<C>> createSearchPreferenceFragments,
                                                 final LanguageCode languageCode,
                                                 final C actualConfiguration) {
        this.activity = activity;
        this.parent = parent;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.createSearchPreferenceFragments = createSearchPreferenceFragments;
        this.languageCode = languageCode;
        this.actualConfiguration = actualConfiguration;
    }

    public static <C> void continueWithPreferencePathNavigation(
            final FragmentActivity activity,
            final ViewGroup parent,
            final @IdRes int fragmentContainerViewId,
            final Function<Consumer<MergedPreferenceScreen<C>>, SearchPreferenceFragments<C>> createSearchPreferenceFragments,
            final LanguageCode languageCode,
            final C actualConfiguration) {
        final var continueWithPreferencePathNavigation =
                new ContinueWithPreferencePathNavigation<>(
                        activity,
                        parent,
                        fragmentContainerViewId,
                        createSearchPreferenceFragments,
                        languageCode,
                        actualConfiguration);
        continueWithPreferencePathNavigation.continueWithPreferencePathNavigation();
    }

    private void continueWithPreferencePathNavigation() {
        this
                .getPreferencePathNavigatorData()
                .ifPresent(this::showPreferenceScreenAndHighlightPreference);
    }

    private Optional<PreferencePathData> getPreferencePathNavigatorData() {
        return Optional
                .ofNullable(activity.getIntent().getExtras())
                .flatMap(PreferencePathDataConverter::fromBundle);
    }

    private void showPreferenceScreenAndHighlightPreference(final PreferencePathData preferencePathData) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                parent,
                fragmentContainerViewId);
        final SearchPreferenceFragments<C> searchPreferenceFragments =
                createSearchPreferenceFragments.apply(
                        mergedPreferenceScreen ->
                                showPreferenceScreenAndHighlightPreferenceOnce(
                                        preferencePathData,
                                        mergedPreferenceScreen));
        searchPreferenceFragments.showSearchPreferenceFragment();
    }

    private boolean showPreferenceScreenAndHighlightPreferenceAlreadyExecuted = false;

    private void showPreferenceScreenAndHighlightPreferenceOnce(final PreferencePathData preferencePathData,
                                                                final MergedPreferenceScreen<C> mergedPreferenceScreen) {
        if (showPreferenceScreenAndHighlightPreferenceAlreadyExecuted) {
            return;
        }
        showPreferenceScreenAndHighlightPreferenceAlreadyExecuted = true;
        mergedPreferenceScreen
                .searchResultsDisplayer()
                .getSearchResultsFragment()
                .navigatePreferencePathAndHighlightPreference
                .navigatePreferencePathAndHighlightPreference(
                        PreferencePathFactory.createPreferencePath(
                                preferencePathData,
                                mergedPreferenceScreen.treeRepository(),
                                languageCode,
                                actualConfiguration,
                                activity));
    }
}
