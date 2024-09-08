package de.KnollFrank.lib.settingssearch.results.adapter;

import static de.KnollFrank.lib.settingssearch.results.adapter.ClickListenerSetter.setOnClickListener;
import static de.KnollFrank.lib.settingssearch.results.adapter.PreferencePathView.createPreferencePathView;
import static de.KnollFrank.lib.settingssearch.results.adapter.SearchableInfoView.createSearchableInfoView;
import static de.KnollFrank.lib.settingssearch.results.adapter.SearchableInfoView.displaySearchableInfo;
import static de.KnollFrank.lib.settingssearch.results.adapter.ViewsAdder.addSearchableInfoViewAndPreferencePathView;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoGetter;

public class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter {

    private final SearchableInfoGetter searchableInfoGetter;
    private final Map<Preference, PreferencePath> preferencePathByPreference;
    private final ShowPreferencePath showPreferencePath;
    private final Set<PreferenceCategory> isNonClickable;
    private final Consumer<Preference> onPreferenceClickListener;

    public SearchablePreferenceGroupAdapter(final PreferenceGroup preferenceGroup,
                                            final SearchableInfoGetter searchableInfoGetter,
                                            final Map<Preference, PreferencePath> preferencePathByPreference,
                                            final ShowPreferencePath showPreferencePath,
                                            final Set<PreferenceCategory> isNonClickable,
                                            final Consumer<Preference> onPreferenceClickListener) {
        super(preferenceGroup);
        this.searchableInfoGetter = searchableInfoGetter;
        this.preferencePathByPreference = preferencePathByPreference;
        this.showPreferencePath = showPreferencePath;
        this.isNonClickable = isNonClickable;
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return addSearchableInfoViewAndPreferencePathView(
                createSearchableInfoView("", parent.getContext()),
                createPreferencePathView(parent.getContext()),
                super.onCreateViewHolder(parent, viewType),
                parent.getContext());
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

    private void displayPreferencePath(final Optional<PreferencePath> preferencePath, final PreferenceViewHolder holder) {
        PreferencePathView.displayPreferencePath(
                holder,
                preferencePath,
                showPreferencePath(preferencePath));
    }

    private boolean showPreferencePath(final Optional<PreferencePath> preferencePath) {
        return preferencePath.filter(showPreferencePath::show).isPresent();
    }

    private Optional<View.OnClickListener> getOnClickListener(final Preference preference) {
        return isNonClickable.contains(preference) ?
                Optional.empty() :
                Optional.of(v -> onPreferenceClickListener.accept(preference));
    }
}
