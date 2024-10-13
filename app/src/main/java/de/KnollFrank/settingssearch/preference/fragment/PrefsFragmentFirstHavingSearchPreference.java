package de.KnollFrank.settingssearch.preference.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.graph.SearchablePreferenceScreenGraphDAOProvider;
import de.KnollFrank.lib.settingssearch.preference.SearchPreference;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;

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

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return SearchPreferenceFragments
                .builder(
                        new SearchConfiguration(getId(), Optional.empty(), getClass()),
                        getParentFragmentManager(),
                        SearchablePreferenceScreenGraphDAOProvider.Mode.COMPUTE_AND_PERSIST_GRAPH,
                        R.raw.searchable_preference_screen_graph)
                .withSearchableInfoProvider(new ReversedListPreferenceSearchableInfoProvider())
                .withPreferenceDialogAndSearchableInfoProvider(
                        new PreferenceDialogAndSearchableInfoProvider() {

                            @Override
                            public Optional<PreferenceDialogAndSearchableInfoByPreferenceDialogProvider> getPreferenceDialogAndSearchableInfoByPreferenceDialogProvider(final Preference preference, final PreferenceFragmentCompat hostOfPreference) {
                                return preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                        Optional.of(
                                                new PreferenceDialogAndSearchableInfoByPreferenceDialogProvider<>(
                                                        new CustomDialogFragment(),
                                                        CustomDialogFragment::getSearchableInfo)) :
                                        Optional.empty();
                            }
                        })
                .build();
    }

    private SearchPreference createSearchPreference(final SearchPreferenceFragments searchPreferenceFragments) {
        final SearchPreference searchPreference = new SearchPreference(getContext());
        searchPreference.setOrder(-1);
        searchPreferenceFragments.searchConfiguration.queryHint().ifPresent(searchPreference::setQueryHint);
        searchPreference.setOnPreferenceClickListener(
                preference -> {
                    searchPreferenceFragments.showSearchPreferenceFragment();
                    return true;
                });
        return searchPreference;
    }
}
