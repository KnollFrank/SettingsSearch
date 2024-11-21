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
import java.util.Map;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.results.IShowPreferenceScreenAndHighlightPreference;

public class SearchResultsFragment extends Fragment {

    private final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference;
    private final IShowPreferenceScreenAndHighlightPreference showPreferenceScreenAndHighlightPreference;
    private RecyclerView recyclerView;

    public SearchResultsFragment(final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference,
                                 final IShowPreferenceScreenAndHighlightPreference showPreferenceScreenAndHighlightPreference) {
        super(R.layout.searchresults_fragment);
        this.preferencePathByPreference = preferencePathByPreference;
        this.showPreferenceScreenAndHighlightPreference = showPreferenceScreenAndHighlightPreference;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.searchResults);
        configure(recyclerView);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Adapter getAdapter() {
        return (Adapter) recyclerView.getAdapter();
    }

    public void setSearchResults(final List<SearchablePreferencePOJO> searchResults) {
        getAdapter().setItems(searchResults);
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
        final Adapter adapter =
                new Adapter(
                        showPreferenceScreenAndHighlightPreference::showPreferenceScreenAndHighlightPreference,
                        // FK-TODO: use predicate defined by user
                        preferencePath -> true,
                        preferencePathByPreference);
        recyclerView.setAdapter(adapter);
    }
}
