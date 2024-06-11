package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;

// FK-TODO: refactor
class PreferenceHighlighter {

    public static void highlightPreferenceOfPreferenceFragment(
            final String keyOfPreference,
            final PreferenceFragmentCompat preferenceFragment) {
        new Handler().post(() -> doHighlightPreferenceOfPreferenceFragment(keyOfPreference, preferenceFragment, 1000));
    }

    private static void doHighlightPreferenceOfPreferenceFragment(
            final String keyOfPreference,
            final PreferenceFragmentCompat preferenceFragment,
            final int highlightDurationMillis) {
        doHighlightPreferenceOfPreferenceFragment(
                preferenceFragment.findPreference(keyOfPreference),
                preferenceFragment,
                highlightDurationMillis);
    }

    private static void doHighlightPreferenceOfPreferenceFragment(
            final Preference preference,
            final PreferenceFragmentCompat preferenceFragment,
            final int highlightDurationMillis) {
        if (preference == null) {
            Log.e("doHighlight", "Preference not found on given screen");
            return;
        }
        final RecyclerView recyclerView = preferenceFragment.getListView();
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter instanceof final PreferenceGroup.PreferencePositionCallback callback) {
            final int position = callback.getPreferenceAdapterPosition(preference);
            if (position != RecyclerView.NO_POSITION) {
                recyclerView.scrollToPosition(position);
                recyclerView.postDelayed(
                        () -> {
                            final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                            if (holder != null) {
                                final Drawable oldBackground = holder.itemView.getBackground();
                                final int color = getColorFromAttr(preferenceFragment.getContext(), android.R.attr.textColorPrimary);
                                holder.itemView.setBackgroundColor(color & 0xffffff | 0x33000000);
                                new Handler().postDelayed(
                                        () -> holder.itemView.setBackgroundDrawable(oldBackground),
                                        highlightDurationMillis);
                            } else {
                                highlightFallback(preference, preferenceFragment, highlightDurationMillis);
                            }
                        },
                        200);
                return;
            }
        }
        highlightFallback(preference, preferenceFragment, highlightDurationMillis);
    }

    private static void highlightFallback(final Preference preference,
                                          final PreferenceFragmentCompat preferenceFragment,
                                          final int highlightDurationMillis) {
        final Drawable oldIcon = preference.getIcon();
        final boolean oldSpaceReserved = preference.isIconSpaceReserved();
        final Drawable arrow = AppCompatResources.getDrawable(preferenceFragment.getContext(), R.drawable.searchpreference_ic_arrow_right);
        final int color = getColorFromAttr(preferenceFragment.getContext(), android.R.attr.textColorPrimary);
        arrow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        preference.setIcon(arrow);
        preferenceFragment.scrollToPreference(preference);
        new Handler().postDelayed(
                () -> {
                    preference.setIcon(oldIcon);
                    preference.setIconSpaceReserved(oldSpaceReserved);
                },
                highlightDurationMillis);
    }

    private static int getColorFromAttr(final Context context, final int attr) {
        final TypedValue typedValue = getTypedValue(context, attr);
        final TypedArray typedArray =
                context.obtainStyledAttributes(
                        typedValue.data,
                        new int[]{android.R.attr.textColorPrimary});
        final int color = typedArray.getColor(0, 0xff3F51B5);
        typedArray.recycle();
        return color;
    }

    private static TypedValue getTypedValue(final Context context, final int attr) {
        final TypedValue typedValue = new TypedValue();
        context
                .getTheme()
                .resolveAttribute(attr, typedValue, true);
        return typedValue;
    }
}
