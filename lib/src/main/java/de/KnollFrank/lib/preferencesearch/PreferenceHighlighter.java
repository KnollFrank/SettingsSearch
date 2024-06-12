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

import org.threeten.bp.Duration;

class PreferenceHighlighter {

    public static void highlightPreferenceOfPreferenceFragment(
            final String keyOfPreference,
            final PreferenceFragmentCompat preferenceFragment,
            final Duration highlightDuration) {
        highlightPreferenceOfPreferenceFragment(
                preferenceFragment.findPreference(keyOfPreference),
                preferenceFragment,
                highlightDuration);
    }

    private static void highlightPreferenceOfPreferenceFragment(
            final Preference preference,
            final PreferenceFragmentCompat preferenceFragment,
            final Duration highlightDuration) {
        new Handler().post(() -> doHighlightPreferenceOfPreferenceFragment(preference, preferenceFragment, highlightDuration));
    }

    private static void doHighlightPreferenceOfPreferenceFragment(
            final Preference preference,
            final PreferenceFragmentCompat preferenceFragment,
            final Duration highlightDuration) {
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
                                        highlightDuration.toMillis());
                            } else {
                                highlightFallback(preference, preferenceFragment, highlightDuration);
                            }
                        },
                        200);
                return;
            }
        }
        highlightFallback(preference, preferenceFragment, highlightDuration);
    }

    private static void highlightFallback(final Preference preference,
                                          final PreferenceFragmentCompat preferenceFragment,
                                          final Duration highlightDuration) {
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
                highlightDuration.toMillis());
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
