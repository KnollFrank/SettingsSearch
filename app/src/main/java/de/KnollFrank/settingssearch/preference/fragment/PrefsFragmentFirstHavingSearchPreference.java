package de.KnollFrank.settingssearch.preference.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.preference.SearchPreference;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;

public class PrefsFragmentFirstHavingSearchPreference extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String SUMMARY_OF_SRC_PREFERENCE = "summaryOfSrcPreference";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createPreferenceConnectedToPreferenceFragmentWithSinglePreference());
        findPreference("keyOfPreferenceWithOnPreferenceClickListener").setOnPreferenceClickListener(this);
        getPreferenceScreen().addPreference(createSearchPreference());
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
        if ("keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        return false;
    }

    private Preference createPreferenceConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(getContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference from src to dst");
        preference.setKey("keyOfSrcPreference");
        final String summary = "summary of src preference";
        preference.setSummary(summary);
        preference.getExtras().putString(SUMMARY_OF_SRC_PREFERENCE, summary);
        return preference;
    }

    private SearchPreference createSearchPreference() {
        final SearchPreference searchPreference = new SearchPreference(getContext());
        searchPreference.setOrder(-1);
        searchPreference.setOnPreferenceClickListener(
                new OnPreferenceClickListener() {

                    private final SearchPreferenceFragments searchPreferenceFragments = createSearchPreferenceFragments();

                    @Override
                    public boolean onPreferenceClick(@NonNull final Preference preference) {
                        searchPreferenceFragments.showSearchPreferenceFragment();
                        return true;
                    }
                });
        return searchPreference;
    }

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return SearchPreferenceFragments
                .builder(
                        new SearchConfiguration(getId(), Optional.empty(), getClass()),
                        getParentFragmentManager())
                .withPreferenceDescriptions(
                        ImmutableList.of(
                                new PreferenceDescription<>(
                                        ReversedListPreference.class,
                                        new ReversedListPreferenceSearchableInfoProvider())))
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final PreferenceFragmentCompat hostOfPreference, final Preference preference) {
                                return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                        Optional.of(
                                                new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider(
                                                        new CustomDialogFragment(),
                                                        customDialogFragment -> ((CustomDialogFragment) customDialogFragment).getSearchableInfo())) :
                                        Optional.empty();
                            }
                        })
                .build();
    }
}
