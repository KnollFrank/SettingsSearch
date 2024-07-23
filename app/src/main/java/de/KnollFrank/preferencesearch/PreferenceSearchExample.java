package de.KnollFrank.preferencesearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.client.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescription;
import de.KnollFrank.preferencesearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.preferencesearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.preferencesearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: README.md anpassen
// FK-TODO: rename project to SettingsSearch, also rename the preferencesearch part of java packages to settingssearch
public class PreferenceSearchExample extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState == null) {
            show(new PrefsFragmentFirst());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == R.id.search_action) {
            showSearchPreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(FRAGMENT_CONTAINER_VIEW, fragment)
                .commit();
    }

    private void showSearchPreferenceFragment() {
        createSearchPreferenceFragments().showSearchPreferenceFragment();
    }

    private SearchPreferenceFragments createSearchPreferenceFragments() {
        return new SearchPreferenceFragments(
                createSearchConfiguration(PrefsFragmentFirst.class),
                (preference, host) -> true,
                ImmutableList.of(
                        new PreferenceDescription<>(
                                ReversedListPreference.class,
                                new ReversedListPreferenceSearchableInfoProvider())),
                new DefaultFragmentFactory(),
                getSupportFragmentManager(),
                (hostOfPreference, preference) ->
                        preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                Optional.of(new CustomDialogFragment()) :
                                Optional.empty());
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW,
                Optional.empty(),
                rootPreferenceFragment);
    }
}
