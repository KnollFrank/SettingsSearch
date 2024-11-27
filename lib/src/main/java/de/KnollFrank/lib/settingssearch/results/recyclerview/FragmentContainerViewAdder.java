package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentContainerView;

public class FragmentContainerViewAdder {

    // FK-TODO: refactor
    public static FragmentContainerView addInvisibleFragmentContainerViewWithIdToParent(final ViewGroup parent,
                                                                                        final @IdRes int id) {
        final FragmentContainerView fragmentContainerView = parent.findViewById(id);
        if (fragmentContainerView == null) {
            final FragmentContainerView _fragmentContainerView = new FragmentContainerView(parent.getContext());
            _fragmentContainerView.setId(id);
            _fragmentContainerView.setVisibility(View.INVISIBLE);
            parent.addView(_fragmentContainerView);
            return _fragmentContainerView;
        }
        return fragmentContainerView;
    }
}
