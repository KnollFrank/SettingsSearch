package de.KnollFrank.lib.settingssearch.fragment.navigation;

import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.results.recyclerview.FragmentContainerViewAdder;

public class ContinueWithPreferencePathNavigation {

    private final FragmentActivity activity;
    private final ViewGroup parent;
    private final @IdRes int fragmentContainerViewId;
    private final Function<Consumer<MergedPreferenceScreen>, SearchPreferenceFragments> createSearchPreferenceFragments;
    private final Locale locale;

    private ContinueWithPreferencePathNavigation(final FragmentActivity activity,
                                                 final ViewGroup parent,
                                                 final @IdRes int fragmentContainerViewId,
                                                 final Function<Consumer<MergedPreferenceScreen>, SearchPreferenceFragments> createSearchPreferenceFragments,
                                                 final Locale locale) {
        this.activity = activity;
        this.parent = parent;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.createSearchPreferenceFragments = createSearchPreferenceFragments;
        this.locale = locale;
    }

    public static void continueWithPreferencePathNavigation(final FragmentActivity activity,
                                                            final ViewGroup parent,
                                                            final @IdRes int fragmentContainerViewId,
                                                            final Function<Consumer<MergedPreferenceScreen>, SearchPreferenceFragments> createSearchPreferenceFragments,
                                                            final Locale locale) {
        final var continueWithPreferencePathNavigation =
                new ContinueWithPreferencePathNavigation(
                        activity,
                        parent,
                        fragmentContainerViewId,
                        createSearchPreferenceFragments,
                        locale);
        continueWithPreferencePathNavigation.continueWithPreferencePathNavigation();
    }

    private void continueWithPreferencePathNavigation() {
        this
                .getPreferencePathNavigatorData()
                .ifPresent(this::showPreferenceScreenAndHighlightPreference);
    }

    private Optional<PreferencePathNavigatorData> getPreferencePathNavigatorData() {
        return Optional
                .ofNullable(activity.getIntent().getExtras())
                .flatMap(PreferencePathNavigatorDataConverter::fromBundle);
    }

    private void showPreferenceScreenAndHighlightPreference(final PreferencePathNavigatorData preferencePathNavigatorData) {
        FragmentContainerViewAdder.addInvisibleFragmentContainerViewWithIdToParent(
                parent,
                fragmentContainerViewId);
        final SearchPreferenceFragments searchPreferenceFragments =
                createSearchPreferenceFragments.apply(
                        mergedPreferenceScreen ->
                                showPreferenceScreenAndHighlightPreferenceOnce(
                                        preferencePathNavigatorData,
                                        mergedPreferenceScreen));
        searchPreferenceFragments.showSearchPreferenceFragment();
    }

    private boolean showPreferenceScreenAndHighlightPreferenceAlreadyExecuted = false;

    private void showPreferenceScreenAndHighlightPreferenceOnce(final PreferencePathNavigatorData preferencePathNavigatorData,
                                                                final MergedPreferenceScreen mergedPreferenceScreen) {
        if (showPreferenceScreenAndHighlightPreferenceAlreadyExecuted) {
            return;
        }
        showPreferenceScreenAndHighlightPreferenceAlreadyExecuted = true;
        mergedPreferenceScreen
                .searchResultsDisplayer()
                .getSearchResultsFragment()
                .navigatePreferencePathAndHighlightPreference
                .navigatePreferencePathAndHighlightPreference(
                        PreferencePathPointerFactory.createPreferencePathPointer(
                                preferencePathNavigatorData,
                                mergedPreferenceScreen.searchablePreferenceScreenGraphDAO(),
                                locale));
    }
}
