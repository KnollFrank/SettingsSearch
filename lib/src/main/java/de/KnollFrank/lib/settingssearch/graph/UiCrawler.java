package de.KnollFrank.lib.settingssearch.graph;

import android.app.Activity;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.BiMap;
import com.google.common.graph.ImmutableValueGraph;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.ActivityDescription;
import de.KnollFrank.lib.settingssearch.PreferenceScreenOfHostOfActivity;
import de.KnollFrank.lib.settingssearch.common.converter.BundleConverter;
import de.KnollFrank.lib.settingssearch.common.graph.Tree;
import de.KnollFrank.lib.settingssearch.common.uicontroller.Fragments;
import de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenToSearchablePreferenceScreenConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

// FK-TODO: refactor
public final class UiCrawler {

    public interface UiNavigator {

        void click(final Preference preference);

        void goBack();

        void waitUntilIdle() throws InterruptedException;
    }

    private final PreferenceScreenToSearchablePreferenceScreenConverter converter;
    private final UiNavigator uiNavigator;
    private final Predicate<SearchablePreference> isSubScreenPredicate;

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
                        new CrawlerListener(),
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
        final PreferenceFragmentCompat currentFragment =
                Fragments
                        .findEitherVisiblePreferenceFragmentOnCurrentActivityOrError()
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
                        preference ->
                                this
                                        .navigate(preference)
                                        .filter(targetFragment -> targetFragment != currentFragment)
                                        .map(
                                                targetFragment ->
                                                        converter
                                                                .convertPreferenceScreen(
                                                                        createPreferenceScreenOfHostOfActivity(targetFragment),
                                                                        screen.id() + "/" + searchablePreference.getKey())
                                                                .searchablePreferenceScreen()));
    }

    private Optional<PreferenceFragmentCompat> navigate(final Preference preference) {
        clickAndWaitUntilIdle(preference);
        return Fragments
                .findEitherVisiblePreferenceFragmentOnCurrentActivityOrError()
                .join(
                        Optional::of,
                        errorMessage -> {
                            goBackAndWaitUntilIdle();
                            return Optional.empty();
                        });
    }

    private void clickAndWaitUntilIdle(final Preference preference) {
        uiNavigator.click(preference);
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
                                               final PreferenceFragmentCompat currentFragment) {
        return Optional.ofNullable(
                this
                        .getPojoEntityMap(screen, currentFragment)
                        .get(searchablePreference));
    }

    private BiMap<SearchablePreference, Preference> getPojoEntityMap(final SearchablePreferenceScreen screen,
                                                                     final PreferenceFragmentCompat currentFragment) {
        return converter
                .convertPreferenceScreen(
                        createPreferenceScreenOfHostOfActivity(currentFragment),
                        screen.id())
                .pojoEntityMap();
    }

    private SearchablePreferenceScreen getRootSearchablePreferenceScreen() {
        return converter
                .convertPreferenceScreen(
                        createPreferenceScreenOfHostOfActivity(getRootPreferenceFragment()),
                        "root")
                .searchablePreferenceScreen();
    }

    private static PreferenceFragmentCompat getRootPreferenceFragment() {
        return Fragments
                .findEitherVisiblePreferenceFragmentOnCurrentActivityOrError()
                .join(
                        Function.identity(),
                        errorMessage -> {
                            throw new IllegalStateException(errorMessage);
                        });
    }

    private class CrawlerListener extends TreeBuilderListeners.EmptyTreeBuilderListener<SearchablePreferenceScreen, SearchablePreference> {

        @Override
        public void onFinishBuildSubtree(final SearchablePreferenceScreen subtreeRoot, final boolean isRootOfTree) {
            if (isRootOfTree) {
                return;
            }
            goBackAndWaitUntilIdle();
        }
    }

    private static boolean isLikelySubScreen(final SearchablePreference searchablePreference) {
        return searchablePreference.getFragment().isPresent() ||
                searchablePreference.getClassNameOfReferencedActivity().isPresent();
    }

    private static PreferenceScreenOfHostOfActivity createPreferenceScreenOfHostOfActivity(final PreferenceFragmentCompat preferenceFragment) {
        final Activity activity = preferenceFragment.requireActivity();
        return new PreferenceScreenOfHostOfActivity(
                preferenceFragment.getPreferenceScreen(),
                preferenceFragment,
                new ActivityDescription(
                        activity.getClass(),
                        BundleConverter.toPersistableBundle(
                                Optional
                                        .ofNullable(activity.getIntent().getExtras())
                                        .orElse(new Bundle()))));
    }
}