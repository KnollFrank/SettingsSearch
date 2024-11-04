package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.threeten.bp.Duration;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.adapter.SearchablePreferenceGroupAdapter;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private final SearchResultsDisplayer searchResultsDisplayer;
    private final PreferencePathNavigator preferencePathNavigator;
    private final @IdRes int fragmentContainerViewId;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;

    public SearchResultsPreferenceFragment(final SearchResultsDisplayer searchResultsDisplayer,
                                           final PreferencePathNavigator preferencePathNavigator,
                                           final @IdRes int fragmentContainerViewId,
                                           final ShowPreferencePathPredicate showPreferencePathPredicate,
                                           final PrepareShow prepareShow) {
        this.searchResultsDisplayer = searchResultsDisplayer;
        this.preferencePathNavigator = preferencePathNavigator;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
    }

    @Override
    public void onCreatePreferences(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        setPreferenceScreen(searchResultsDisplayer.getSearchResultsDescription().preferenceScreenWithMap().preferenceScreen());
    }

    @NonNull
    @Override
    public RecyclerView onCreateRecyclerView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, @Nullable final Bundle savedInstanceState) {
        final RecyclerView recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutAnimation(null);
        return recyclerView;
    }

    @NonNull
    @Override
    protected Adapter<PreferenceViewHolder> onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        // FK-TODO: die Preferences des preferenceScreen sollen ihren aktuellen Zustand widerspiegeln (z.B. soll der Haken einer CheckBoxPreference gemäß den darunterliegenden Daten gesetzt oder nicht gesetzt sein.)
        final SearchablePreferenceGroupAdapter searchablePreferenceGroupAdapter =
                new SearchablePreferenceGroupAdapter(
                        preferenceScreen,
                        searchResultsDisplayer.getSearchResultsDescription().searchableInfoAttribute(),
                        searchResultsDisplayer.getSearchResultsDescription().preferencePathByPreference(),
                        showPreferencePathPredicate,
                        Set.of(),
                        this::showPreferenceScreenAndHighlightPreference);
        searchResultsDisplayer.addPropertyChangeListener(searchablePreferenceGroupAdapter);
        return searchablePreferenceGroupAdapter;
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        // FK-TODO: refactor
        final SearchablePreferencePOJO searchablePreferencePOJO = searchResultsDisplayer.getSearchResultsDescription().preferenceScreenWithMap().pojoEntityMap().inverse().get(preference);
        showPreferenceScreenAndHighlightPreference(
                preferencePathNavigator.navigatePreferencePath(searchResultsDisplayer.getSearchResultsDescription().preferencePathByPreference().get(preference)),
                searchablePreferencePOJO.key());
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
}
