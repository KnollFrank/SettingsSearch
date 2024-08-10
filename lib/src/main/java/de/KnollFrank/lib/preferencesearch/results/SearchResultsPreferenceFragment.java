package de.KnollFrank.lib.preferencesearch.results;

import static de.KnollFrank.lib.preferencesearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.preferencesearch.results.PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.threeten.bp.Duration;

import java.util.function.Predicate;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.results.adapter.SearchablePreferenceGroupAdapter;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoGetter;

// FK-TODO: die PreferenceCategory im Suchergebnis, die den Namen eines PreferenceScreens anzeigt, soll nicht anklickbar sein.
public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private MergedPreferenceScreen mergedPreferenceScreen;
    private @IdRes int fragmentContainerViewId;
    private Predicate<Preference> showPreferencePathForPreference;
    private SearchableInfoGetter searchableInfoGetter;

    public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId,
                                                              final SearchableInfoGetter searchableInfoGetter,
                                                              final Predicate<Preference> showPreferencePathForPreference,
                                                              final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchResultsPreferenceFragment searchResultsPreferenceFragment = Factory.newInstance(fragmentContainerViewId);
        searchResultsPreferenceFragment.setMergedPreferenceScreen(mergedPreferenceScreen);
        searchResultsPreferenceFragment.setSearchableInfoGetter(searchableInfoGetter);
        searchResultsPreferenceFragment.setShowPreferencePathForPreference(showPreferencePathForPreference);
        return searchResultsPreferenceFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        new Factory().setInstanceVariables();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        setPreferenceScreen(mergedPreferenceScreen.preferenceScreen);
    }

    @NonNull
    @Override
    protected Adapter onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        // FK-TODO: die Preferences des preferenceScreen sollen ihren aktuellen Zustand widerspiegeln (z.B. soll der Haken einer CheckBoxPreference gemäß den darunterliegenden Daten gesetzt oder nicht gesetzt sein.)
        return new SearchablePreferenceGroupAdapter(
                preferenceScreen,
                searchableInfoGetter,
                mergedPreferenceScreen.preferencePathByPreference,
                showPreferencePathForPreference,
                this::showPreferenceScreenAndHighlightPreference);
    }

    private void setMergedPreferenceScreen(final MergedPreferenceScreen mergedPreferenceScreen) {
        preparePreferenceScreenForSearch(mergedPreferenceScreen.preferenceScreen);
        this.mergedPreferenceScreen = mergedPreferenceScreen;
    }

    private void setSearchableInfoGetter(final SearchableInfoGetter searchableInfoGetter) {
        this.searchableInfoGetter = searchableInfoGetter;
    }

    private void setShowPreferencePathForPreference(final Predicate<Preference> showPreferencePathForPreference) {
        this.showPreferencePathForPreference = showPreferencePathForPreference;
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        if (preference instanceof PreferenceGroup) {
            return;
        }
        this
                .mergedPreferenceScreen
                .findHost(preference)
                .ifPresent(host -> showPreferenceScreenAndHighlightPreference(host, preference));
    }

    private void showPreferenceScreenAndHighlightPreference(
            final PreferenceFragmentCompat fragmentOfPreferenceScreen,
            final Preference preference2Highlight) {
        showFragment(
                fragmentOfPreferenceScreen,
                _fragmentOfPreferenceScreen -> highlightPreference(_fragmentOfPreferenceScreen, preference2Highlight.getKey()),
                true,
                fragmentContainerViewId,
                requireActivity().getSupportFragmentManager());
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
        // FK-TODO: was soll passieren, falls die Preference keinen key hat, also keyOfPreference2Highlight == null ist?
        preferenceFragment.scrollToPreference(keyOfPreference2Highlight);
        PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                keyOfPreference2Highlight,
                preferenceFragment,
                Duration.ofSeconds(1));
    }

    private class Factory {

        private static final String FRAGMENT_CONTAINER_VIEW_ID = "fragmentContainerViewId";

        public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId) {
            final SearchResultsPreferenceFragment fragment = new SearchResultsPreferenceFragment();
            fragment.setArguments(createArguments(fragmentContainerViewId));
            return fragment;
        }

        public void setInstanceVariables() {
            fragmentContainerViewId = requireArguments().getInt(FRAGMENT_CONTAINER_VIEW_ID);
        }

        private static Bundle createArguments(final @IdRes int fragmentContainerViewId) {
            final Bundle bundle = new Bundle();
            bundle.putInt(FRAGMENT_CONTAINER_VIEW_ID, fragmentContainerViewId);
            return bundle;
        }
    }
}
