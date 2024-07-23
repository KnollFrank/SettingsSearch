package de.KnollFrank.preferencesearch.preference.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import de.KnollFrank.preferencesearch.R;

public class CustomDialogFragment extends DialogFragment {

    public static void showInstance(final FragmentManager fragmentManager) {
        new CustomDialogFragment().show(fragmentManager, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog, container, false);
    }

    public String getSearchableInfo() {
        final TextView textView = getView().findViewById(R.id.textView);
        return textView.getText().toString();
    }
}
