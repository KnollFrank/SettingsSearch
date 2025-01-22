package de.KnollFrank.settingssearch.preference.fragment;

import android.content.Intent;
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
import de.KnollFrank.settingssearch.SettingsActivity;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summaryOfSrcPreferenceWithExtras";
    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summaryOfSrcPreferenceWithoutExtras";
    public static final String KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "keyOfSrcPreferenceWithoutExtras";
    public static final String NON_STANDARD_LINK_TO_SECOND_FRAGMENT = "non_standard_link_to_second_fragment";
    private static final String KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER = "keyOfPreferenceWithOnPreferenceClickListener";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summary of src preference with extras";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summary of src preference without extras";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference1());
        getPreferenceScreen().addPreference(createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference());
        getPreferenceScreen().findPreference(NON_STANDARD_LINK_TO_SECOND_FRAGMENT).setIcon(R.drawable.face);
        getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(new Intent(getContext(), SettingsActivity.class));
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
            show(PrefsFragmentSecond.class.getName(), preference.getExtras());
            return true;
        }
        if (KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(preference.getKey())) {
            show(
                    PreferenceFragmentWithSinglePreference.class.getName(),
                    createArguments4PreferenceWithoutExtras(preference));
            return true;
        }
        return false;
    }

    public static Bundle createArguments4PreferenceWithoutExtras(final @NonNull Preference preference) {
        final Bundle arguments = new Bundle();
        arguments.putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS, preference.getSummary().toString());
        return arguments;
    }

    private @NonNull Preference createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference1() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference with extras from src to dst");
        preference.setKey("keyOfSrcPreferenceWithExtras");
        final String summary = SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
        preference.setSummary(summary);
        preference.getExtras().putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS, summary);
        return preference;
    }

    private @NonNull Preference createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference without extras from src to dst");
        preference.setKey(KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        preference.setSummary(SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        return preference;
    }

    private void setOnPreferenceClickListeners() {
        Stream
                .of(
                        KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER,
                        NON_STANDARD_LINK_TO_SECOND_FRAGMENT,
                        KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }

    // adapted from PreferenceFragmentCompat.onPreferenceTreeClick()
    private void show(final String classNameOfFragment2Show, final Bundle arguments) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        final Fragment fragment =
                fragmentManager.getFragmentFactory().instantiate(
                        requireActivity().getClassLoader(),
                        classNameOfFragment2Show);
        fragment.setArguments(arguments);
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
