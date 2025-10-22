package de.KnollFrank.settingssearch.preference.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.preference.SearchPreference;
import de.KnollFrank.settingssearch.ConfigurationProvider;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.SearchPreferenceFragmentsFactory;
import de.KnollFrank.settingssearch.SettingsSearchApplication;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirstHavingSearchPreference extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String SUMMARY_OF_SRC_PREFERENCE = "summaryOfSrcPreference";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createPreferenceConnectedToPreferenceFragmentWithSinglePreference());
        findPreference("keyOfPreferenceWithOnPreferenceClickListener").setOnPreferenceClickListener(this);
        getPreferenceScreen().addPreference(createSearchPreference(createSearchPreferenceFragments()));
    }

    @Override
    public void onDisplayPreferenceDialog(@NonNull final Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if ("keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        return false;
    }

    private Preference createPreferenceConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference from src to dst");
        preference.setKey("keyOfSrcPreference");
        final String summary = "summary of src preference";
        preference.setSummary(summary);
        preference.getExtras().putString(SUMMARY_OF_SRC_PREFERENCE, summary);
        return preference;
    }

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return SearchPreferenceFragmentsFactory.createSearchPreferenceFragments(
                getId(),
                requireActivity(),
                Optional::empty,
                mergedPreferenceScreen -> {
                },
                SettingsSearchApplication
                        .getInstanceFromContext(requireContext())
                        .daoProviderManager
                        .getDAOProvider(),
                ConfigurationProvider.getConfiguration(requireContext()));
    }

    private SearchPreference createSearchPreference(final SearchPreferenceFragments searchPreferenceFragments) {
        final SearchPreference searchPreference = new SearchPreference(requireContext());
        searchPreference.setOrder(-1);
        searchPreferenceFragments.searchConfig.queryHint.ifPresent(searchPreference::setQueryHint);
        searchPreference.setOnPreferenceClickListener(
                preference -> {
                    searchPreferenceFragments.showSearchPreferenceFragment();
                    return true;
                });
        return searchPreference;
    }
}
