package de.KnollFrank.lib.settingssearch.preference;

import static de.KnollFrank.lib.settingssearch.results.adapter.ClickListenerSetter.setOnClickListener;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.R;

public class SearchPreference extends Preference {

    private Optional<String> queryHint = Optional.empty();

    public SearchPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.searchpreference_preference);
    }

    public SearchPreference(@NonNull final Context context) {
        super(context);
        setLayoutResource(R.layout.searchpreference_preference);
    }

    public void setQueryHint(final String queryHint) {
        this.queryHint = Optional.ofNullable(queryHint);
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder) {
        setOnClickListener(
                holder.itemView,
                Optional.of(view -> onPreferenceClick()));
        configure((SearchView) holder.findViewById(R.id.searchView));
    }

    private void configure(final SearchView searchView) {
        searchView.setFocusable(false);
        searchView.setClickable(false);
        searchView.setOnQueryTextFocusChangeListener((view, hasFocus) -> onPreferenceClick());
        searchView.setInputType(InputType.TYPE_NULL);
        queryHint.ifPresent(searchView::setQueryHint);
    }

    private void onPreferenceClick() {
        getOnPreferenceClickListener().onPreferenceClick(this);
    }
}
