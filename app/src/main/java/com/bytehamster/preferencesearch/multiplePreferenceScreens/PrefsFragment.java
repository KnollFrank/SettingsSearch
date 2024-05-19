package com.bytehamster.preferencesearch.multiplePreferenceScreens;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bytehamster.lib.preferencesearch.SearchConfiguration;
import com.bytehamster.lib.preferencesearch.SearchPreference;
import com.bytehamster.lib.preferencesearch.SearchPreferenceResult;
import com.bytehamster.preferencesearch.R;

public class PrefsFragment extends PreferenceFragmentCompat {

    private SearchPreference searchPreference;

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);

        this.searchPreference = findPreference("searchPreference");
        final SearchConfiguration config = searchPreference.getSearchConfiguration();
        config.setActivity((AppCompatActivity) getActivity());
        config.setFragmentContainerViewId(android.R.id.content);

        config.index(R.xml.preferences_multiple_screens).addBreadcrumb("Main file");
        config.index(R.xml.preferences2).addBreadcrumb("Second file");
        config.setBreadcrumbsEnabled(true);
        config.setHistoryEnabled(true);
        config.setFuzzySearchEnabled(true);
    }

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    public void onSearchResultClicked(final SearchPreferenceResult result) {
        if (result.getResourceFile() == R.xml.preferences_multiple_screens) {
            searchPreference.setVisible(false); // Do not allow to click search multiple times
            scrollToPreference(result.getKey());
            findPreference(result.getKey()).setTitle("RESULT: " + findPreference(result.getKey()).getTitle());
            result.highlight(this);
        } else if (result.getResourceFile() == R.xml.preferences2) {
            // globalSettings.performClick();
            final Fragment fragment4Preferences2 =
                    instantiateFragment(
                            findPreference("global_settings").getFragment(),
                            createBundle(result.getKey()));
            show(fragment4Preferences2);
        }
    }

    private static Bundle createBundle(final String keyOfPreference2Highlight) {
        final Bundle arguments = new Bundle();
        arguments.putString(MultiplePreferenceScreensExample.KEY_OF_PREFERENCE_2_HIGHLIGHT, keyOfPreference2Highlight);
        return arguments;
    }

    private Fragment instantiateFragment(final String fragmentClassName, final Bundle arguments) {
        final Fragment fragment = requireActivity()
                .getSupportFragmentManager()
                .getFragmentFactory()
                .instantiate(
                        requireActivity().getClassLoader(),
                        fragmentClassName);
        fragment.setArguments(arguments);
        return fragment;
    }

    private void show(final Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(((View) this.getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
