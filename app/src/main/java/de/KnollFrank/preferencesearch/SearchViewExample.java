package de.KnollFrank.preferencesearch;

import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.preferencesearch.BaseSearchPreferenceFragment;
import de.KnollFrank.lib.preferencesearch.Navigation;
import de.KnollFrank.lib.preferencesearch.PreferenceFragments;
import de.KnollFrank.lib.preferencesearch.SearchConfiguration;
import de.KnollFrank.lib.preferencesearch.SearchPreferenceFragments;
import de.KnollFrank.lib.preferencesearch.SearchPreferenceResult;
import de.KnollFrank.lib.preferencesearch.SearchPreferenceResultListener;
import de.KnollFrank.lib.preferencesearch.common.UIUtils;

public class SearchViewExample extends AppCompatActivity implements SearchPreferenceResultListener {

    @IdRes
    private static final int FRAGMENT_CONTAINER_VIEW = R.id.fragmentContainerView;

    @IdRes
    private int dummyFragmentContainerViewId = View.NO_ID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setContentView(R.layout.multiple_preference_screens_example);
        if (savedInstanceState == null) {
            Navigation.show(
                    new PrefsFragment(),
                    false,
                    getSupportFragmentManager(),
                    FRAGMENT_CONTAINER_VIEW);
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
            final SearchConfiguration searchConfiguration = new SearchConfiguration();
            configure(searchConfiguration, new PrefsFragment());
            final SearchPreferenceFragments searchPreferenceFragments = new SearchPreferenceFragments(searchConfiguration);
            searchPreferenceFragments.showSearchPreferenceFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResultClicked(@NonNull final SearchPreferenceResult result) {
        Navigation.showPreferenceScreenAndHighlightPreference(
                result.getPreferenceFragmentClass().getName(),
                result.getKey(),
                this,
                FRAGMENT_CONTAINER_VIEW);
    }

    public static class PrefsFragment extends BaseSearchPreferenceFragment {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            addPreferencesFromResource(R.xml.preferences_multiple_screens);
        }
    }

    private void _setContentView(final @LayoutRes int resource) {
        final Pair<View, Integer> contentViewAndDummyFragmentContainerViewId =
                UIUtils.createContentViewAndDummyFragmentContainerViewId(
                        resource,
                        this);
        dummyFragmentContainerViewId = contentViewAndDummyFragmentContainerViewId.second;
        setContentView(contentViewAndDummyFragmentContainerViewId.first);
    }

    private void configure(final SearchConfiguration searchConfiguration,
                           final PreferenceFragmentCompat root) {
        searchConfiguration.setActivity(this);
        searchConfiguration.setFragmentContainerViewId(FRAGMENT_CONTAINER_VIEW);
        searchConfiguration.setDummyFragmentContainerViewId(dummyFragmentContainerViewId);
        searchConfiguration.setPreferenceFragments(
                PreferenceFragments.getPreferenceFragments(
                        root,
                        this,
                        this.getSupportFragmentManager(),
                        dummyFragmentContainerViewId));
        searchConfiguration.setBreadcrumbsEnabled(false);
    }
}
