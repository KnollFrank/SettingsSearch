package de.KnollFrank.lib.settingssearch.results.recyclerview;

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

public class SearchResultsFragment extends Fragment implements ItemClickListener {

    private SearchResultsRecyclerViewAdapter adapter;

    public SearchResultsFragment() {
        super(R.layout.searchresults_fragment);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.searchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new SearchResultsRecyclerViewAdapter(getContext());
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public void setData(final List<SearchablePreferencePOJO> data) {
        adapter.setData(data);
    }
}
