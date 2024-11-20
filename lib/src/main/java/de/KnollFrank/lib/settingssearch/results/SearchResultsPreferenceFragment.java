package de.KnollFrank.lib.settingssearch.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.adapter.SearchablePreferenceGroupAdapter;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat implements PropertyChangeListener {

    private SearchResultsDescription searchResultsDescription;
    private final PreferencePathNavigator preferencePathNavigator;
    private final @IdRes int fragmentContainerViewId;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;

    public static SearchResultsPreferenceFragment createInstance(final SearchResultsDisplayer searchResultsDisplayer,
                                                                 final PreferencePathNavigator preferencePathNavigator,
                                                                 final @IdRes int fragmentContainerViewId,
                                                                 final ShowPreferencePathPredicate showPreferencePathPredicate,
                                                                 final PrepareShow prepareShow) {
        final SearchResultsPreferenceFragment searchResultsPreferenceFragment =
                new SearchResultsPreferenceFragment(
                        searchResultsDisplayer.getSearchResultsDescription(),
                        preferencePathNavigator,
                        fragmentContainerViewId,
                        showPreferencePathPredicate,
                        prepareShow);
        searchResultsDisplayer.addPropertyChangeListener(searchResultsPreferenceFragment);
        return searchResultsPreferenceFragment;
    }

    private SearchResultsPreferenceFragment(final SearchResultsDescription searchResultsDescription,
                                            final PreferencePathNavigator preferencePathNavigator,
                                            final @IdRes int fragmentContainerViewId,
                                            final ShowPreferencePathPredicate showPreferencePathPredicate,
                                            final PrepareShow prepareShow) {
        this.searchResultsDescription = searchResultsDescription;
        this.preferencePathNavigator = preferencePathNavigator;
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
    }

    @Override
    public void onCreatePreferences(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        setPreferenceScreen(searchResultsDescription.preferenceScreenWithMap().preferenceScreen());
    }

    @NonNull
    @Override
    public RecyclerView onCreateRecyclerView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, @Nullable final Bundle savedInstanceState) {
        final RecyclerView recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutAnimation(null);
        return recyclerView;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        setSearchResultsDescription((SearchResultsDescription) evt.getNewValue());
    }

    @NonNull
    @Override
    protected Adapter<PreferenceViewHolder> onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        // FK-TODO: die Preferences des preferenceScreen sollen ihren aktuellen Zustand widerspiegeln (z.B. soll der Haken einer CheckBoxPreference gemäß den darunterliegenden Daten gesetzt oder nicht gesetzt sein.)
        return new SearchablePreferenceGroupAdapter(
                preferenceScreen,
                searchResultsDescription.searchableInfoAttribute(),
                searchResultsDescription.preferencePathByPreference(),
                showPreferencePathPredicate,
                preference -> {
                    final _ShowPreferenceScreenAndHighlightPreference showPreferenceScreenAndHighlightPreference =
                            new _ShowPreferenceScreenAndHighlightPreference(
                                    preferencePathNavigator,
                                    null,
                                    fragmentContainerViewId,
                                    searchResultsDescription,
                                    prepareShow,
                                    requireActivity().getSupportFragmentManager());
                    showPreferenceScreenAndHighlightPreference.showPreferenceScreenAndHighlightPreference(preference);
                });
    }

    private void setSearchResultsDescription(final SearchResultsDescription searchResultsDescription) {
        this.searchResultsDescription = searchResultsDescription;
        resetPreferenceScreen(searchResultsDescription.preferenceScreenWithMap().preferenceScreen());
    }

    private void resetPreferenceScreen(final PreferenceScreen preferenceScreen) {
        setPreferenceScreen(null);
        setPreferenceScreen(preferenceScreen);
    }
}
