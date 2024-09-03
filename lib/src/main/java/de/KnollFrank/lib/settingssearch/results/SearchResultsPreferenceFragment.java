package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;
import static de.KnollFrank.lib.settingssearch.results.PreferenceScreenForSearchPreparer.preparePreferenceScreenForSearch;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.results.adapter.SearchablePreferenceGroupAdapter;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoGetter;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private MergedPreferenceScreen mergedPreferenceScreen;
    private @IdRes int fragmentContainerViewId;
    private ShowPreferencePath showPreferencePath;
    private SearchableInfoGetter searchableInfoGetter;

    public static SearchResultsPreferenceFragment newInstance(final @IdRes int fragmentContainerViewId,
                                                              final SearchableInfoGetter searchableInfoGetter,
                                                              final ShowPreferencePath showPreferencePath,
                                                              final MergedPreferenceScreen mergedPreferenceScreen) {
        final SearchResultsPreferenceFragment fragment = Factory.newInstance(fragmentContainerViewId);
        fragment.setMergedPreferenceScreen(mergedPreferenceScreen);
        fragment.setSearchableInfoGetter(searchableInfoGetter);
        fragment.setShowPreferencePathPredicate(showPreferencePath);
        return fragment;
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
                showPreferencePath,
                mergedPreferenceScreen.isNonClickable,
                this::showPreferenceScreenAndHighlightPreference);
    }

    private void setMergedPreferenceScreen(final MergedPreferenceScreen mergedPreferenceScreen) {
        preparePreferenceScreenForSearch(mergedPreferenceScreen.preferenceScreen);
        this.mergedPreferenceScreen = mergedPreferenceScreen;
    }

    private void setSearchableInfoGetter(final SearchableInfoGetter searchableInfoGetter) {
        this.searchableInfoGetter = searchableInfoGetter;
    }

    public void setShowPreferencePathPredicate(final ShowPreferencePath showPreferencePath) {
        this.showPreferencePath = showPreferencePath;
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
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
                _fragmentOfPreferenceScreen -> {
                    if (preference2Highlight.hasKey()) {
                        highlightPreference(_fragmentOfPreferenceScreen, preference2Highlight.getKey());
                    }
                },
                true,
                fragmentContainerViewId,
                requireActivity().getSupportFragmentManager());
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
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
