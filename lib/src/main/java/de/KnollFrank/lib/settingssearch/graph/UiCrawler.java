package de.KnollFrank.lib.settingssearch.graph;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;

import com.google.common.collect.BiMap;
import com.google.common.graph.ImmutableValueGraph;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.common.converter.BundleConverter;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.uicontroller.Fragments;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: refactor
public final class UiCrawler {

    public static final String DISCOVERY_DUMMY = "de.KnollFrank.lib.settingssearch.graph.DISCOVERY_DUMMY";

    public interface UiNavigator {

        void click(final SearchablePreference searchablePreference);

        void goBack();

        void waitUntilIdle() throws InterruptedException;

        Optional<PreferencesOfFragment> extractPreferences();
    }

    private final PreferenceScreenToSearchablePreferenceScreenConverter converter;
    private final UiNavigator uiNavigator;
    private final Predicate<SearchablePreference> isSubScreenPredicate;
    private final CrawlerListener crawlerListener = new CrawlerListener();

    public UiCrawler(final PreferenceScreenToSearchablePreferenceScreenConverter converter,
                     final UiNavigator uiNavigator,
                     final Predicate<SearchablePreference> isSubScreenPredicate) {
        this.converter = converter;
        this.uiNavigator = uiNavigator;
        this.isSubScreenPredicate = isSubScreenPredicate;
    }

    @SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
    public Tree<SearchablePreferenceScreen, SearchablePreference, ImmutableValueGraph<SearchablePreferenceScreen, SearchablePreference>> crawl() {
        final TreeBuilder<SearchablePreferenceScreen, SearchablePreference> treeBuilder =
                new TreeBuilder<>(
                        crawlerListener,
                        this::createEdgeSuppliersHavingSource);
        return treeBuilder.buildTreeWithRoot(getRootSearchablePreferenceScreen());
    }

    private List<EdgeSupplier<SearchablePreferenceScreen, SearchablePreference>> createEdgeSuppliersHavingSource(final SearchablePreferenceScreen screen) {
        return screen
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .filter(this::isSubScreen)
                .map(searchablePreference -> EdgeSupplierFactory.createEdgeSupplier(screen, searchablePreference, () -> getTargetNode(screen, searchablePreference)))
                .collect(Collectors.toList());
    }

    private boolean isSubScreen(final SearchablePreference searchablePreference) {
        return isLikelySubScreen(searchablePreference) || isSubScreenPredicate.test(searchablePreference);
    }

    private Optional<SearchablePreferenceScreen> getTargetNode(final SearchablePreferenceScreen screen,
                                                               final SearchablePreference searchablePreference) {
        final Fragment currentFragment =
                Fragments
                        .findEitherVisibleFragmentOnCurrentActivityOrError()
                        .join(
                                Function.identity(),
                                errorMessage -> {
                                    throw new IllegalStateException(
                                            String.format(
                                                    "%s screen: %s searchablePreference: %s",
                                                    errorMessage,
                                                    screen,
                                                    searchablePreference));
                                });
        return this
                .getPreference(searchablePreference, screen, currentFragment)
                .flatMap(
                        preference -> {
                            if (isAlreadyInPath(preference)) {
                                return Optional.empty();
                            }
                            clickAndWaitUntilIdle(searchablePreference);
                            return Fragments
                                    .findEitherVisibleFragmentOnCurrentActivityOrError()
                                    .join(
                                            targetFragment -> {
                                                if (targetFragment == currentFragment) {
                                                    return Optional.empty();
                                                }
                                                final ActivityDescription targetActivity = getActivityDescription(targetFragment);
                                                final CrawlerState targetState = new CrawlerState((Class<? extends Fragment>) targetFragment.getClass(), targetActivity);
                                                if (crawlerListener.isStateInPath(targetState)) {
                                                    goBackAndWaitUntilIdle();
                                                    return Optional.empty();
                                                }

                                                final Optional<PreferencesOfFragment> childPreferencesOfFragment = uiNavigator.extractPreferences();
                                                if (childPreferencesOfFragment.isEmpty() || childPreferencesOfFragment.get().preferences().isEmpty()) {
                                                    goBackAndWaitUntilIdle();
                                                    clearDiscoveryDummy(searchablePreference);
                                                    return Optional.empty();
                                                }
                                                updateSearchablePreferenceWithDiscovery(searchablePreference, targetFragment);
                                                return Optional.of(
                                                        converter
                                                                .convertPreferenceScreen(
                                                                        childPreferencesOfFragment.get().preferences(),
                                                                        targetFragment,
                                                                        targetActivity,
                                                                        childPreferencesOfFragment.get().title(),
                                                                        childPreferencesOfFragment.get().summary(),
                                                                        screen.id() + "/" + searchablePreference.getKey())
                                                                .searchablePreferenceScreen());
                                            },
                                            errorMessage -> {
                                                goBackAndWaitUntilIdle();
                                                return Optional.empty();
                                            });
                        })
                .or(() -> {
                    clearDiscoveryDummy(searchablePreference);
                    return Optional.empty();
                });
    }

    private boolean isAlreadyInPath(final Preference preference) {
        final String fragmentClassName = preference.getFragment();
        if (fragmentClassName != null) {
            try {
                final Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) Class.forName(fragmentClassName);
                final ActivityDescription activityDescription = getActivityDescriptionFromPreference(preference);
                return crawlerListener.isStateInPath(new CrawlerState(fragmentClass, activityDescription));
            } catch (final Exception ignored) {
            }
        }
        return false;
    }

    private static ActivityDescription getActivityDescriptionFromPreference(final Preference preference) {
        final android.app.Activity activity = (android.app.Activity) preference.getContext();
        return new ActivityDescription(
                activity.getClass(),
                BundleConverter.toPersistableBundle(preference.getExtras()));
    }

    private static void updateSearchablePreferenceWithDiscovery(final SearchablePreference searchablePreference, final Fragment targetFragment) {
        if (searchablePreference.getFragment().equals(Optional.of(DISCOVERY_DUMMY))) {
            searchablePreference.setFragment(Optional.of(targetFragment.getClass().getName()));
            searchablePreference.setClassNameOfReferencedActivity(Optional.of(targetFragment.requireActivity().getClass().getName()));
        }
    }

    private static void clearDiscoveryDummy(final SearchablePreference searchablePreference) {
        if (searchablePreference.getFragment().equals(Optional.of(DISCOVERY_DUMMY))) {
            searchablePreference.setFragment(Optional.empty());
        }
    }

    private static ActivityDescription getActivityDescription(final Fragment fragment) {
        return new ActivityDescription(
                fragment.requireActivity().getClass(),
                BundleConverter.toPersistableBundle(
                        Optional
                                .ofNullable(fragment.requireActivity().getIntent().getExtras())
                                .orElse(new android.os.Bundle())));
    }

    private void clickAndWaitUntilIdle(final SearchablePreference searchablePreference) {
        uiNavigator.click(searchablePreference);
        try {
            uiNavigator.waitUntilIdle();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private void goBackAndWaitUntilIdle() {
        uiNavigator.goBack();
        try {
            uiNavigator.waitUntilIdle();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Optional<Preference> getPreference(final SearchablePreference searchablePreference,
                                               final SearchablePreferenceScreen screen,
                                               final Fragment currentFragment) {
        return Optional.ofNullable(
                this
                        .getPojoEntityMap(screen, currentFragment)
                        .get(searchablePreference));
    }

    private BiMap<SearchablePreference, Preference> getPojoEntityMap(final SearchablePreferenceScreen screen,
                                                                     final Fragment currentFragment) {
        final Optional<PreferencesOfFragment> preferencesOfFragment = uiNavigator.extractPreferences();
        return converter
                .convertPreferenceScreen(
                        preferencesOfFragment.map(PreferencesOfFragment::preferences).orElse(List.of()),
                        currentFragment,
                        getActivityDescription(currentFragment),
                        preferencesOfFragment.flatMap(PreferencesOfFragment::title),
                        preferencesOfFragment.flatMap(PreferencesOfFragment::summary),
                        screen.id())
                .pojoEntityMap();
    }

    private SearchablePreferenceScreen getRootSearchablePreferenceScreen() {
        final Fragment rootFragment = getRootPreferenceFragment();
        final Optional<PreferencesOfFragment> preferencesOfFragment = uiNavigator.extractPreferences();
        return converter
                .convertPreferenceScreen(
                        preferencesOfFragment.map(PreferencesOfFragment::preferences).orElse(List.of()),
                        rootFragment,
                        getActivityDescription(rootFragment),
                        preferencesOfFragment.flatMap(PreferencesOfFragment::title),
                        preferencesOfFragment.flatMap(PreferencesOfFragment::summary),
                        "root")
                .searchablePreferenceScreen();
    }

    private static Fragment getRootPreferenceFragment() {
        return Fragments
                .findEitherVisibleFragmentOnCurrentActivityOrError()
                .join(
                        Function.identity(),
                        errorMessage -> {
                            throw new IllegalStateException(errorMessage);
                        });
    }

    private record CrawlerState(Class<? extends Fragment> fragmentClass, ActivityDescription activityDescription) {
    }

    private class CrawlerListener extends TreeBuilderListeners.EmptyTreeBuilderListener<SearchablePreferenceScreen, SearchablePreference> {

        private final java.util.Deque<CrawlerState> ancestry = new java.util.ArrayDeque<>();

        @Override
        public void onStartBuildSubtree(final SearchablePreferenceScreen subtreeRoot, final boolean isRootOfTree) {
            ancestry.push(new CrawlerState((Class<? extends Fragment>) subtreeRoot.host().fragment(), subtreeRoot.host().activityOfFragment()));
        }

        @Override
        public void onFinishBuildSubtree(final SearchablePreferenceScreen subtreeRoot, final boolean isRootOfTree) {
            ancestry.pop();
            if (isRootOfTree) {
                return;
            }
            goBackAndWaitUntilIdle();
        }

        public boolean isStateInPath(final CrawlerState state) {
            return ancestry.contains(state);
        }
    }

    private static boolean isLikelySubScreen(final SearchablePreference searchablePreference) {
        return searchablePreference.getFragment().isPresent() ||
                searchablePreference.getClassNameOfReferencedActivity().isPresent() ||
                searchablePreference.getFragment().equals(Optional.of(DISCOVERY_DUMMY));
    }
}
