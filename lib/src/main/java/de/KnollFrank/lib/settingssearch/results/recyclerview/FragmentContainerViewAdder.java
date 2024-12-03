package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentContainerView;

public class FragmentContainerViewAdder {

    public static void addInvisibleFragmentContainerViewWithIdToParent(final ViewGroup parent,
                                                                       final @IdRes int id) {
        if (parent.findViewById(id) == null) {
            parent.addView(createInvisibleFragmentContainerView(parent, id));
        }
    }

    private static FragmentContainerView createInvisibleFragmentContainerView(final ViewGroup parent,
                                                                              final int id) {
        final FragmentContainerView fragmentContainerView = new FragmentContainerView(parent.getContext());
        fragmentContainerView.setId(id);
        fragmentContainerView.setVisibility(View.INVISIBLE);
        return fragmentContainerView;
    }
}
