package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class SearchResultsFragment extends Fragment {

    private RecyclerView recyclerView;

    public SearchResultsFragment() {
        super(R.layout.searchresults_fragment);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.searchResults);
        configureRecyclerView(recyclerView, view.getContext());
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private Adapter getAdapter() {
        return (Adapter) recyclerView.getAdapter();
    }

    public void setData(final List<SearchablePreferencePOJO> data) {
        getAdapter().setData(data);
    }

    private void configureRecyclerView(final RecyclerView recyclerView, final Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutAnimation(null);
        final Adapter adapter =
                new Adapter(
                        searchablePreferencePOJO ->
                                Toast
                                        .makeText(
                                                getContext(),
                                                "You clicked " + searchablePreferencePOJO, Toast.LENGTH_SHORT)
                                        .show(),
                        getContext());
        recyclerView.setAdapter(adapter);
    }
}
