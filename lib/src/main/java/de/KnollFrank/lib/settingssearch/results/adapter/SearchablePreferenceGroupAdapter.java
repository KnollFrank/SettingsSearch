package de.KnollFrank.lib.settingssearch.results.adapter;

import static de.KnollFrank.lib.settingssearch.results.adapter.ClickListenerSetter.setOnClickListener;
import static de.KnollFrank.lib.settingssearch.results.adapter.PreferencePathView.createPreferencePathView;
import static de.KnollFrank.lib.settingssearch.results.adapter.PreferencePathView.hasPreferencePathView;
import static de.KnollFrank.lib.settingssearch.results.adapter.SearchableInfoView.createSearchableInfoView;
import static de.KnollFrank.lib.settingssearch.results.adapter.SearchableInfoView.displaySearchableInfo;
import static de.KnollFrank.lib.settingssearch.results.adapter.SearchableInfoView.hasSearchableInfoView;
import static de.KnollFrank.lib.settingssearch.results.adapter.ViewsAdder.addViews;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDescription;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoGetter;

public class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter implements PropertyChangeListener {

    private SearchableInfoGetter searchableInfoGetter;
    private Map<Preference, PreferencePath> preferencePathByPreference;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final Set<PreferenceCategory> nonClickablePreferences;
    private final Consumer<Preference> onPreferenceClickListener;

    public SearchablePreferenceGroupAdapter(final PreferenceGroup preferenceGroup,
                                            final SearchableInfoGetter searchableInfoGetter,
                                            final Map<Preference, PreferencePath> preferencePathByPreference,
                                            final ShowPreferencePathPredicate showPreferencePathPredicate,
                                            final Set<PreferenceCategory> nonClickablePreferences,
                                            final Consumer<Preference> onPreferenceClickListener) {
        super(preferenceGroup);
        this.searchableInfoGetter = searchableInfoGetter;
        this.preferencePathByPreference = preferencePathByPreference;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.nonClickablePreferences = nonClickablePreferences;
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final PreferenceViewHolder holder = super.onCreateViewHolder(parent, viewType);
        return addSearchableInfoViewAndPreferencePathViewIfAbsent(holder, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Preference preference = getItem(position);
        displaySearchableInfo(holder, searchableInfoGetter.getSearchableInfo(preference));
        displayPreferencePath(
                Maps.get(preferencePathByPreference, preference),
                holder);
        setOnClickListener(
                holder.itemView,
                getOnClickListener(preference));
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final SearchResultsDescription searchResultsDescription = (SearchResultsDescription) evt.getNewValue();
        this.searchableInfoGetter = searchResultsDescription.searchableInfoAttribute();
        this.preferencePathByPreference = searchResultsDescription.preferencePathByPreference();
        Log.i(this.getClass().getName(), "new Info");
    }

    private static PreferenceViewHolder addSearchableInfoViewAndPreferencePathViewIfAbsent(
            final PreferenceViewHolder holder,
            final Context context) {
        return hasSearchableInfoView(holder) && hasPreferencePathView(holder) ?
                holder :
                addViews(
                        List.of(
                                createSearchableInfoView("", context),
                                createPreferencePathView(context)),
                        holder,
                        context);
    }

    private void displayPreferencePath(final Optional<PreferencePath> preferencePath, final PreferenceViewHolder holder) {
        PreferencePathView.displayPreferencePath(
                holder,
                preferencePath,
                showPreferencePath(preferencePath));
    }

    private boolean showPreferencePath(final Optional<PreferencePath> preferencePath) {
        return preferencePath.filter(showPreferencePathPredicate::shallShowPreferencePath).isPresent();
    }

    private Optional<View.OnClickListener> getOnClickListener(final Preference preference) {
        return nonClickablePreferences.contains(preference) ?
                Optional.empty() :
                Optional.of(v -> onPreferenceClickListener.accept(preference));
    }
}
