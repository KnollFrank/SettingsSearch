package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.navigation.PreferencePathPointer;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.INavigatePreferencePathAndHighlightPreference;
import de.KnollFrank.lib.settingssearch.search.ui.SearchResultsFragmentUI;

public class SearchResultsFragment extends Fragment {

    public final INavigatePreferencePathAndHighlightPreference navigatePreferencePathAndHighlightPreference;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PreferencePathDisplayer preferencePathDisplayer;
    private final SearchResultsFragmentUI searchResultsFragmentUI;
    private RecyclerView recyclerView;

    public SearchResultsFragment(final INavigatePreferencePathAndHighlightPreference navigatePreferencePathAndHighlightPreference,
                                 final ShowPreferencePathPredicate showPreferencePathPredicate,
                                 final PreferencePathDisplayer preferencePathDisplayer,
                                 final SearchResultsFragmentUI searchResultsFragmentUI) {
        super(searchResultsFragmentUI.getRootViewId());
        this.navigatePreferencePathAndHighlightPreference = navigatePreferencePathAndHighlightPreference;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.preferencePathDisplayer = preferencePathDisplayer;
        this.searchResultsFragmentUI = searchResultsFragmentUI;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = searchResultsFragmentUI.getSearchResultsView(view);
        configure(recyclerView);
    }

    public void setSearchResults(final List<SearchablePreference> searchResults) {
        getSearchResultsRecyclerViewAdapter().setItems(searchResults);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private SearchResultsRecyclerViewAdapter getSearchResultsRecyclerViewAdapter() {
        return (SearchResultsRecyclerViewAdapter) recyclerView.getAdapter();
    }

    private void configure(final RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutAnimation(null);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        layoutManager.getOrientation()));
        final SearchResultsRecyclerViewAdapter searchResultsRecyclerViewAdapter =
                new SearchResultsRecyclerViewAdapter(
                        preference ->
                                navigatePreferencePathAndHighlightPreference.navigatePreferencePathAndHighlightPreference(
                                        PreferencePathPointer.of(
                                                preference.getPreferencePath(),
                                                0)),
                        showPreferencePathPredicate,
                        preferencePathDisplayer);
        recyclerView.setAdapter(searchResultsRecyclerViewAdapter);
    }
}
