package de.KnollFrank.lib.settingssearch.results.recyclerview;

import static de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathView.createPreferencePathView;
import static de.KnollFrank.lib.settingssearch.results.recyclerview.PreferencePathView.getPreferencePathView;
import static de.KnollFrank.lib.settingssearch.results.recyclerview.SearchableInfoView.createSearchableInfoView;
import static de.KnollFrank.lib.settingssearch.results.recyclerview.SearchableInfoView.getSearchableInfoView;
import static de.KnollFrank.lib.settingssearch.results.recyclerview.TextViews.setOptionalTextOnOptionalTextView;
import static de.KnollFrank.lib.settingssearch.results.recyclerview.ViewsAdder.addViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.preference.AndroidResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.PreferencePath;
import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.adapter.ClickListenerSetter;

// FK-TODO: see androidx.preference.PreferenceGroupAdapter
public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<PreferenceViewHolder> {

    private final List<SearchablePreferencePOJO> items = new ArrayList<>();
    private final Consumer<SearchablePreferencePOJO> onPreferenceClickListener;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference;
    private final List<ItemResourceDescriptor> itemResourceDescriptors = new ArrayList<>();

    public SearchResultsRecyclerViewAdapter(final Consumer<SearchablePreferencePOJO> onPreferenceClickListener,
                                            final ShowPreferencePathPredicate showPreferencePathPredicate,
                                            final Map<SearchablePreferencePOJO, PreferencePath> preferencePathByPreference) {
        this.onPreferenceClickListener = onPreferenceClickListener;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.preferencePathByPreference = preferencePathByPreference;
    }

    @Override
    public int getItemViewType(final int position) {
        return getItemViewType(ItemResourceDescriptor.from(getItem(position)));
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onCreateViewHolder()
    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return onCreateViewHolder(parent, itemResourceDescriptors.get(viewType));
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onBindViewHolder()
    @Override
    public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
        onBindViewHolder(holder, getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(final List<SearchablePreferencePOJO> items) {
        this.items.clear();
        this.items.addAll(items);
        // FK-TODO: use DiffUtil like in PreferenceGroupAdapter?
        notifyDataSetChanged();
    }

    private SearchablePreferencePOJO getItem(final int position) {
        return items.get(position);
    }

    private record ItemResourceDescriptor(int layoutResId, int widgetLayoutResId) {

        public static ItemResourceDescriptor from(final SearchablePreferencePOJO searchablePreferencePOJO) {
            return new ItemResourceDescriptor(
                    searchablePreferencePOJO.getLayoutResId(),
                    searchablePreferencePOJO.getWidgetLayoutResId());
        }
    }

    private int getItemViewType(final ItemResourceDescriptor itemResourceDescriptor) {
        if (!itemResourceDescriptors.contains(itemResourceDescriptor)) {
            itemResourceDescriptors.add(itemResourceDescriptor);
        }
        return itemResourceDescriptors.indexOf(itemResourceDescriptor);
    }

    private static PreferenceViewHolder onCreateViewHolder(final @NonNull ViewGroup parent,
                                                           final ItemResourceDescriptor itemResourceDescriptor) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(itemResourceDescriptor.layoutResId(), parent, false);
        setBackground(view, inflater.getContext());
        showWidgetFrame(android.R.id.widget_frame, itemResourceDescriptor.widgetLayoutResId(), view, inflater);
        final PreferenceViewHolder preferenceViewHolder =
                addSearchableInfoViewAndPreferencePathViewIfAbsent(
                        new PreferenceViewHolder(view),
                        inflater.getContext());
        ClickListenerSetter.disableClicksOnSubviews(preferenceViewHolder.itemView);
        return preferenceViewHolder;
    }

    private static void setBackground(final View view, final Context context) {
        if (view.getBackground() == null) {
            ViewCompat.setBackground(view, getBackground(context));
        }
    }

    private static Drawable getBackground(final Context context) {
        try (final TypedArray typedArray = context.obtainStyledAttributes(null, androidx.preference.R.styleable.BackgroundStyle)) {
            return getBackground(typedArray, context);
        }
    }

    private static Drawable getBackground(final TypedArray typedArray, final Context context) {
        final Drawable background = typedArray.getDrawable(androidx.preference.R.styleable.BackgroundStyle_android_selectableItemBackground);
        return background != null ?
                background :
                AppCompatResources.getDrawable(
                        context,
                        android.R.drawable.list_selector_background);
    }

    private static void showWidgetFrame(final @IdRes int widgetFrameId,
                                        final @LayoutRes int widgetLayoutResId,
                                        final View view,
                                        final LayoutInflater inflater) {
        final ViewGroup widgetFrame = view.findViewById(widgetFrameId);
        if (widgetFrame != null) {
            if (widgetLayoutResId != 0) {
                inflater.inflate(widgetLayoutResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(View.GONE);
            }
        }
    }

    private void onBindViewHolder(final PreferenceViewHolder viewHolder, final SearchablePreferencePOJO searchablePreferencePOJO) {
        viewHolder.resetState();
        viewHolder.itemView.setClickable(true);
        viewHolder.itemView.setOnClickListener(view -> onPreferenceClickListener.accept(searchablePreferencePOJO));
        // itemView.setId(mViewId);
        displayTitle(viewHolder, searchablePreferencePOJO);
        displaySummary(viewHolder, searchablePreferencePOJO);
        displaySearchableInfo(viewHolder, searchablePreferencePOJO);
        displayPreferencePath(viewHolder, searchablePreferencePOJO);
        displayIcon(viewHolder, searchablePreferencePOJO, true);
        setEnabledStateOnViews(viewHolder.itemView, true);
    }

    private static void displayTitle(final PreferenceViewHolder holder,
                                     final SearchablePreferencePOJO searchablePreferencePOJO) {
        setOptionalTextOnOptionalTextView(
                holder.findViewById(android.R.id.title),
                searchablePreferencePOJO.getHighlightedTitle());
    }

    private static void displaySummary(final PreferenceViewHolder holder,
                                       final SearchablePreferencePOJO searchablePreferencePOJO) {
        setOptionalTextOnOptionalTextView(
                holder.findViewById(android.R.id.summary),
                searchablePreferencePOJO.getHighlightedSummary());
    }

    private static void displaySearchableInfo(final PreferenceViewHolder holder,
                                              final SearchablePreferencePOJO searchablePreferencePOJO) {
        setOptionalTextOnOptionalTextView(
                getSearchableInfoView(holder),
                searchablePreferencePOJO.getHighlightedSearchableInfo());
    }

    private void displayPreferencePath(final PreferenceViewHolder holder,
                                       final SearchablePreferencePOJO searchablePreferencePOJO) {
        final Optional<PreferencePath> preferencePath = Maps.get(preferencePathByPreference, searchablePreferencePOJO);
        PreferencePathView.displayPreferencePath(
                getPreferencePathView(holder),
                preferencePath,
                showPreferencePath(preferencePath));
    }

    private boolean showPreferencePath(final Optional<PreferencePath> preferencePath) {
        return preferencePath
                .filter(showPreferencePathPredicate::shallShowPreferencePath)
                .isPresent();
    }

    private static void displayIcon(final PreferenceViewHolder holder, final SearchablePreferencePOJO searchablePreferencePOJO, final boolean iconSpaceReserved) {
        final Optional<Drawable> icon = searchablePreferencePOJO.getIcon(holder.itemView.getContext());
        holder
                .<ImageView>findViewById(android.R.id.icon)
                .ifPresent(
                        iconView -> {
                            icon.ifPresent(iconView::setImageDrawable);
                            iconView.setVisibility(getVisibility(icon, iconSpaceReserved));
                        });
        getImageFrame(holder)
                .ifPresent(
                        imageFrame ->
                                imageFrame.setVisibility(
                                        getVisibility(icon, iconSpaceReserved)));
    }

    private static int getVisibility(final Optional<Drawable> icon, final boolean iconSpaceReserved) {
        return icon.isPresent() ?
                View.VISIBLE :
                iconSpaceReserved ?
                        View.INVISIBLE :
                        View.GONE;
    }

    private static Optional<View> getImageFrame(final PreferenceViewHolder holder) {
        return holder
                .findViewById(androidx.preference.R.id.icon_frame)
                .or(() -> holder.findViewById(AndroidResources.ANDROID_R_ICON_FRAME));
    }

    private void setEnabledStateOnViews(final View view, final boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof final ViewGroup viewGroup) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(viewGroup.getChildAt(i), enabled);
            }
        }
    }

    private static PreferenceViewHolder addSearchableInfoViewAndPreferencePathViewIfAbsent(
            final PreferenceViewHolder holder,
            final Context context) {
        return getSearchableInfoView(holder).isPresent() && getPreferencePathView(holder).isPresent() ?
                holder :
                addViews(
                        List.of(
                                createSearchableInfoView("", context),
                                createPreferencePathView(context)),
                        holder,
                        context);
    }
}
