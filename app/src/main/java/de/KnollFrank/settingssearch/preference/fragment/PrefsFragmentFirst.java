package de.KnollFrank.settingssearch.preference.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import java.util.stream.Stream;

import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String SUMMARY_OF_SRC_PREFERENCE = "summaryOfSrcPreference";
    public static final String NON_STANDARD_LINK_TO_SECOND_FRAGMENT = "non_standard_link_to_second_fragment";
    private static final String KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER = "keyOfPreferenceWithOnPreferenceClickListener";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createPreferenceConnectedToPreferenceFragmentWithSinglePreference());
        setOnPreferenceClickListeners();
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if (KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER.equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        if (NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey())) {
            show(PrefsFragmentSecond.class.getName(), preference);
            return true;
        }
        return false;
    }

    private @NonNull Preference createPreferenceConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(getContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference from src to dst");
        preference.setKey("keyOfSrcPreference");
        final String summary = "summary of src preference";
        preference.setSummary(summary);
        preference.getExtras().putString(SUMMARY_OF_SRC_PREFERENCE, summary);
        return preference;
    }

    private void setOnPreferenceClickListeners() {
        Stream
                .of(
                        KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER,
                        NON_STANDARD_LINK_TO_SECOND_FRAGMENT)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }

    // adapted from PreferenceFragmentCompat.onPreferenceTreeClick()
    private void show(final String classNameOfFragment2Show, final @NonNull Preference clickedPreference) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        final Bundle args = clickedPreference.getExtras();
        final Fragment fragment =
                fragmentManager.getFragmentFactory().instantiate(
                        requireActivity().getClassLoader(),
                        classNameOfFragment2Show);
        fragment.setArguments(args);
        fragment.setTargetFragment(this, 0);
        fragmentManager.beginTransaction()
                // Attempt to replace this fragment in its root view - developers should
                // implement onPreferenceStartFragment in their activity so that they can
                // customize this behaviour and handle any transitions between fragments
                .replace(((View) requireView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
