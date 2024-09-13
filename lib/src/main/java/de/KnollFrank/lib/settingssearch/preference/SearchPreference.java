package de.KnollFrank.lib.settingssearch.preference;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import de.KnollFrank.lib.settingssearch.R;

public class SearchPreference extends Preference {

    public SearchPreference(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.searchpreference_preference);
    }

    public SearchPreference(@NonNull final Context context) {
        super(context);
        setLayoutResource(R.layout.searchpreference_preference);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        final EditText searchText = (EditText) holder.findViewById(R.id.search);
        searchText.setFocusable(false);
        searchText.setInputType(InputType.TYPE_NULL);
        searchText.setOnClickListener(getOnClickListener());
        searchText.setHint("Search");
        holder.findViewById(R.id.search_card).setOnClickListener(getOnClickListener());
        holder.itemView.setOnClickListener(getOnClickListener());
        holder.itemView.setBackgroundColor(0x0);
    }

    private View.OnClickListener getOnClickListener() {
        return view -> getOnPreferenceClickListener().onPreferenceClick(SearchPreference.this);
    }
}
