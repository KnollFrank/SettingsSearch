package de.KnollFrank.settingssearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.client.SearchConfiguration;
import de.KnollFrank.lib.settingssearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoByPreferenceDialogProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreference;
import de.KnollFrank.settingssearch.preference.custom.ReversedListPreferenceSearchableInfoProvider;
import de.KnollFrank.settingssearch.preference.fragment.CustomDialogFragment;
import de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst;

// FK-TODO: README.md anpassen
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
                new DefaultFragmentFactory(),
                ImmutableList.of(
                        new PreferenceDescription<>(
                                ReversedListPreference.class,
                                new ReversedListPreferenceSearchableInfoProvider())),
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
                },
                new IsPreferenceSearchable() {

                    @Override
                    public boolean isPreferenceOfHostSearchable(final Preference preference, final PreferenceFragmentCompat host) {
                        return true;
                    }
                },
                new ShowPreferencePath() {

                    @Override
                    public boolean show(final PreferencePath preferencePath) {
                        return preferencePath
                                .getPreference()
                                .map(preference -> !(preference instanceof PreferenceGroup))
                                .orElse(false);
                    }
                },
                getSupportFragmentManager());
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW,
                Optional.empty(),
                rootPreferenceFragment);
    }
}