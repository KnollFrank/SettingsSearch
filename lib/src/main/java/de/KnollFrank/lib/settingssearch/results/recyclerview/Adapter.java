package de.KnollFrank.lib.settingssearch.results.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.preference.AndroidResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

// FK-TODO: see androidx.preference.PreferenceGroupAdapter
public class Adapter extends RecyclerView.Adapter<PreferenceViewHolder> {

    private final List<SearchablePreferencePOJO> items = new ArrayList<>();
    private final Consumer<SearchablePreferencePOJO> onPreferenceClickListener;
    private final List<ItemResourceDescriptor> itemResourceDescriptors = new ArrayList<>();

    public Adapter(final Consumer<SearchablePreferencePOJO> onPreferenceClickListener) {
        this.onPreferenceClickListener = onPreferenceClickListener;
    }

    @Override
    public int getItemViewType(final int position) {
        final ItemResourceDescriptor itemResourceDescriptor = ItemResourceDescriptor.from(getItem(position));
        if (!itemResourceDescriptors.contains(itemResourceDescriptor)) {
            itemResourceDescriptors.add(itemResourceDescriptor);
        }
        return itemResourceDescriptors.indexOf(itemResourceDescriptor);
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onCreateViewHolder()
    // FK-TODO: refactor
    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final ItemResourceDescriptor itemResourceDescriptor = itemResourceDescriptors.get(viewType);
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final TypedArray a = parent.getContext().obtainStyledAttributes(null, androidx.preference.R.styleable.BackgroundStyle);
        Drawable background = a.getDrawable(androidx.preference.R.styleable.BackgroundStyle_android_selectableItemBackground);
        if (background == null) {
            background =
                    AppCompatResources.getDrawable(
                            parent.getContext(),
                            android.R.drawable.list_selector_background);
        }
        a.recycle();

        final View view = inflater.inflate(itemResourceDescriptor.layoutResId(), parent, false);
        if (view.getBackground() == null) {
            ViewCompat.setBackground(view, background);
        }

        final ViewGroup widgetFrame = view.findViewById(android.R.id.widget_frame);
        if (widgetFrame != null) {
            if (itemResourceDescriptor.widgetLayoutResId() != 0) {
                inflater.inflate(itemResourceDescriptor.widgetLayoutResId(), widgetFrame);
            } else {
                widgetFrame.setVisibility(View.GONE);
            }
        }

        return new PreferenceViewHolder(view);
    }

    // FK-TODO: adapt from PreferenceGroupAdapter.onBindViewHolder()
    @Override
    public void onBindViewHolder(final PreferenceViewHolder holder, final int position) {
        final SearchablePreferencePOJO searchablePreferencePOJO = getItem(position);
        holder.resetState();
        onBindViewHolder(holder, searchablePreferencePOJO, true, true, true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(final List<SearchablePreferencePOJO> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private SearchablePreferencePOJO getItem(final int position) {
        return items.get(position);
    }

    private static Optional<Drawable> getIcon(final SearchablePreferencePOJO searchablePreferencePOJO,
                                              final Context context) {
        return searchablePreferencePOJO
                .iconResourceIdOrIconPixelData()
                .map(iconResourceIdOrIconPixelData ->
                        iconResourceIdOrIconPixelData.join(
                                iconResourceId -> AppCompatResources.getDrawable(context, iconResourceId),
                                iconPixelData ->
                                        DrawableAndStringConverter.string2Drawable(
                                                iconPixelData,
                                                context.getResources())));
    }

    private record ItemResourceDescriptor(int layoutResId, int widgetLayoutResId) {

        public static ItemResourceDescriptor from(final SearchablePreferencePOJO searchablePreferencePOJO) {
            return new ItemResourceDescriptor(
                    searchablePreferencePOJO.layoutResId(),
                    searchablePreferencePOJO.widgetLayoutResId());
        }
    }

    // FK-TODO: refactor
    private void onBindViewHolder(final PreferenceViewHolder holder,
                                  final SearchablePreferencePOJO searchablePreferencePOJO,
                                  final boolean iconSpaceReserved,
                                  final boolean mAllowDividerAbove,
                                  final boolean mAllowDividerBelow) {
        View itemView = holder.itemView;
        Integer summaryTextColor = null;

        itemView.setClickable(true);
        itemView.setOnClickListener(view -> onPreferenceClickListener.accept(searchablePreferencePOJO));
        // itemView.setId(mViewId);

        final TextView summaryView = (TextView) holder.findViewById(android.R.id.summary);
        if (summaryView != null) {
            final CharSequence summary = searchablePreferencePOJO.summary().orElse("");
            if (!TextUtils.isEmpty(summary)) {
                summaryView.setText(summary);
                summaryView.setVisibility(View.VISIBLE);
                summaryTextColor = summaryView.getCurrentTextColor();
            } else {
                summaryView.setVisibility(View.GONE);
            }
        }

        final TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        if (titleView != null) {
            final CharSequence title = searchablePreferencePOJO.title().orElse("");
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
                titleView.setVisibility(View.VISIBLE);
            } else {
                titleView.setVisibility(View.GONE);
            }
        }

        final Optional<Drawable> icon = Adapter.getIcon(searchablePreferencePOJO, itemView.getContext());
        final ImageView imageView = (ImageView) holder.findViewById(android.R.id.icon);
        if (imageView != null) {
            icon.ifPresentOrElse(
                    drawable -> {
                        imageView.setImageDrawable(drawable);
                        imageView.setVisibility(View.VISIBLE);
                    },
                    () -> {
                        imageView.setVisibility(iconSpaceReserved ? View.INVISIBLE : View.GONE);
                    });
        }

        View imageFrame = holder.findViewById(androidx.preference.R.id.icon_frame);
        if (imageFrame == null) {
            imageFrame = holder.findViewById(AndroidResources.ANDROID_R_ICON_FRAME);
        }
        if (imageFrame != null) {
            if (icon.isPresent()) {
                imageFrame.setVisibility(View.VISIBLE);
            } else {
                imageFrame.setVisibility(iconSpaceReserved ? View.INVISIBLE : View.GONE);
            }
        }

        setEnabledStateOnViews(itemView, true);

        holder.setDividerAllowedAbove(mAllowDividerAbove);
        holder.setDividerAllowedBelow(mAllowDividerBelow);
    }

    private void setEnabledStateOnViews(final View view, final boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof final ViewGroup viewGroup) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(viewGroup.getChildAt(i), enabled);
            }
        }
    }
}
