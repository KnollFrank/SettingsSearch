package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentContainerView;

public class FragmentContainerViewAdder {

    public static void addInvisibleFragmentContainerViewWithIdToParent(final ViewGroup parent,
                                                                       final @IdRes int id) {
        if (parent.findViewById(id) == null) {
            parent.addView(createInvisibleFragmentContainerView(id, parent.getContext()));
        }
    }

    private static FragmentContainerView createInvisibleFragmentContainerView(final @IdRes int id,
                                                                              final Context context) {
        final FragmentContainerView fragmentContainerView = new FragmentContainerView(context);
        fragmentContainerView.setId(id);
        fragmentContainerView.setVisibility(View.INVISIBLE);
        return fragmentContainerView;
    }
}
