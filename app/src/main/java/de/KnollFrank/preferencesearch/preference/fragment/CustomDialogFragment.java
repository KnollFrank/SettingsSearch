package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.preferencesearch.R;

public class CustomDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog, container, false);
    }

    public static void showInstance(final FragmentManager fragmentManager) {
        final DialogFragment dialogFragment = new CustomDialogFragment();
        dialogFragment.show(fragmentManager, null);
    }
}
