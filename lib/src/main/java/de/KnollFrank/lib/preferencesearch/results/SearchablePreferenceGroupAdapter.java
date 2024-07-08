package de.KnollFrank.lib.preferencesearch.results;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceViewHolder;

import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoGetter;

class SearchablePreferenceGroupAdapter extends PreferenceGroupAdapter {

    public static final int SEARCHABLE_INFO_VIEW_ID = View.generateViewId();

    private final SearchableInfoGetter searchableInfoGetter;
    private final Consumer<Preference> onPreferenceClickListener;

    public SearchablePreferenceGroupAdapter(final PreferenceGroup preferenceGroup,
                                            final SearchableInfoGetter searchableInfoGetter,
                                            final Consumer<Preference> onPreferenceClickListener) {
        super(preferenceGroup);
        this.searchableInfoGetter = searchableInfoGetter;
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final PreferenceViewHolder preferenceViewHolder = super.onCreateViewHolder(parent, viewType);
        addSearchableInfoView(parent, preferenceViewHolder);
        return preferenceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Preference preference = getItem(position);
        displaySearchableInfo(holder, preference);
        UIUtils.setOnClickListener(
                holder.itemView,
                v -> onPreferenceClickListener.accept(preference));
    }

    private void addSearchableInfoView(final ViewGroup parent, final PreferenceViewHolder preferenceViewHolder) {
        addSecondViewBelowFirstView(
                preferenceViewHolder.findViewById(android.R.id.summary),
                createSearchableInfoView("", parent.getContext()), parent.getContext());
    }

    private void addSecondViewBelowFirstView(final View firstView, final View secondView, final Context context) {
        final LinearLayout container = createContainer(context, firstView.getLayoutParams());
        replaceView(firstView, container);
        firstView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
        container.addView(firstView);
        container.addView(secondView);
    }

    private void replaceView(final View oldView, final View newView) {
        final ViewGroup oldViewParent = (ViewGroup) oldView.getParent();
        final int indexOfOldView = oldViewParent.indexOfChild(oldView);
        oldViewParent.removeViewAt(indexOfOldView);
        oldViewParent.addView(newView, indexOfOldView);
    }

    // FK-TODO: move UI code to new class
    private static LinearLayout createContainer(final Context context, final LayoutParams layoutParams) {
        final LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.VERTICAL);
        return container;
    }

    private static TextView createSearchableInfoView(final String text, final Context context) {
        final TextView searchableInfoView = new TextView(context);
        searchableInfoView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
        searchableInfoView.setText(text);
        searchableInfoView.setId(SEARCHABLE_INFO_VIEW_ID);
        searchableInfoView.setVisibility(View.GONE);
        return searchableInfoView;
    }

    private static TextView getSearchableInfoView(final PreferenceViewHolder holder) {
        return (TextView) holder.findViewById(SEARCHABLE_INFO_VIEW_ID);
    }

    private void displaySearchableInfo(final PreferenceViewHolder holder, final Preference preference) {
        final TextView searchableInfoView = getSearchableInfoView(holder);
        final Optional<CharSequence> searchableInfo = searchableInfoGetter.getSearchableInfo(preference);
        if (searchableInfo.isPresent()) {
            searchableInfoView.setText(searchableInfo.get());
            searchableInfoView.setVisibility(View.VISIBLE);
        } else {
            searchableInfoView.setVisibility(View.GONE);
        }
    }
}
