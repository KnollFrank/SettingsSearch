package de.KnollFrank.preferencesearch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.client.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.client.SearchPreferenceFragments;
import de.KnollFrank.lib.preferencesearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.preferencesearch.fragment.navigation.Commit;
import de.KnollFrank.lib.preferencesearch.fragment.navigation.Navigation;

// FK-TODO: README.md anpassen
public class SearchViewExample extends AppCompatActivity {

    private static final @IdRes int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState == null) {
            Navigation.show(
                    new PrefsFragmentFirst(),
                    false,
                    getSupportFragmentManager(),
                    FRAGMENT_CONTAINER_VIEW,
                    Commit.COMMIT_ASYNC);
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
            final SearchPreferenceFragments searchPreferenceFragments =
                    new SearchPreferenceFragments(
                            createSearchConfiguration(PrefsFragmentFirst.class),
                            getSupportFragmentManager(),
                            new DefaultFragmentFactory());
            searchPreferenceFragments.showSearchPreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private SearchConfiguration createSearchConfiguration(final Class<? extends PreferenceFragmentCompat> rootPreferenceFragment) {
        return new SearchConfiguration(
                FRAGMENT_CONTAINER_VIEW,
                Optional.empty(),
                rootPreferenceFragment);
    }
}
