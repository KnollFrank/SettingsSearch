package de.KnollFrank.preferencesearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.client.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.preferencesearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialog;
import de.KnollFrank.lib.preferencesearch.search.provider.PreferenceDescription;
import de.KnollFrank.preferencesearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreference;
import de.KnollFrank.preferencesearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.preferencesearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.preferencesearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: README.md anpassen
// FK-TODO: rename project to SettingsSearch, also rename the preferencesearch part of java packages to settingssearch
// FK-FIXME: type in search field "in a" => crash
//           java.lang.IllegalStateException: Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.
//                                                                                                     ViewHolder 1:PreferenceViewHolder{73d5ea1 position=4 id=6, oldPos=-1, pLpos:-1 not recyclable(1)}
//                                                                                                     View Holder 2:PreferenceViewHolder{b834bb4 position=2 id=6, oldPos=-1, pLpos:-1} androidx.recyclerview.widget.RecyclerView{da9791d VFED.V... ......ID 0,0-1080,862 #7f080173 app:id/recycler_view}, adapter:de.KnollFrank.lib.preferencesearch.results.adapter.SearchablePreferenceGroupAdapter@20368e1, layout:androidx.recyclerview.widget.LinearLayoutManager@d7d9792, context:de.KnollFrank.preferencesearch.PreferenceSearchExample@e249b2
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
                preference -> !(preference instanceof PreferenceGroup),
                ImmutableList.of(
                        new PreferenceDescription<>(
                                ReversedListPreference.class,
                                new ReversedListPreferenceSearchableInfoProvider())),
                new DefaultFragmentFactory(),
                getSupportFragmentManager(),
                (hostOfPreference, preference) ->
                        preference instanceof CustomDialogPreference || "keyOfPreferenceWithOnPreferenceClickListener".equals(preference.getKey()) ?
                                Optional.of(
                                        new PreferenceDialogAndSearchableInfoByPreferenceDialog(
                                                new CustomDialogFragment(),
                                                customDialogFragment -> ((CustomDialogFragment) customDialogFragment).getSearchableInfo())) :
                                Optional.empty());
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW,
                Optional.empty(),
                rootPreferenceFragment);
    }
}
