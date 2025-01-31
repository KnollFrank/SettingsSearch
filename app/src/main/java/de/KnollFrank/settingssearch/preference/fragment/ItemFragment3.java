package de.KnollFrank.settingssearch.preference.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Collectors;

import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.preference.fragment.placeholder.PlaceholderContent3;

public class ItemFragment3 extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public ItemFragment3() {
    }

    @SuppressWarnings("unused")
    public static ItemFragment3 newInstance(final int columnCount) {
        final ItemFragment3 fragment = new ItemFragment3();
        {
            final Bundle args = new Bundle();
            args.putInt(ARG_COLUMN_COUNT, columnCount);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof final RecyclerView recyclerView) {
            final Context context = recyclerView.getContext();
            recyclerView.setLayoutManager(
                    mColumnCount <= 1 ?
                            new LinearLayoutManager(context) :
                            new GridLayoutManager(context, mColumnCount));
            recyclerView.setAdapter(new ItemRecyclerViewAdapter(PlaceholderContent3.ITEMS));
        }
        return view;
    }

    public PreferenceFragmentCompat asPreferenceFragment() {
        return new PreferenceFragment3();
    }

    public static class PreferenceFragment3 extends PreferenceFragmentTemplate {

        public PreferenceFragment3() {
            super(context ->
                    PlaceholderContent3
                            .ITEMS
                            .stream()
                            .map(placeholderItem -> ItemFragment.PreferenceFragment.asPreference(placeholderItem, context))
                            .collect(Collectors.toList()));
        }
    }
}