package de.KnollFrank.lib.preferencesearch;

import android.content.Context;
import android.content.res.Resources;
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
public class PreferenceHighlighter {

    public static void highlightPreferenceOfPreferenceFragment(
            final String keyOfPreference,
            final PreferenceFragmentCompat preferenceFragment) {
        new Handler().post(() -> doHighlightPreferenceOfPreferenceFragment(keyOfPreference, preferenceFragment));
    }

    private static void doHighlightPreferenceOfPreferenceFragment(
            final String keyOfPreference,
            final PreferenceFragmentCompat preferenceFragment) {
        final Preference preference = preferenceFragment.findPreference(keyOfPreference);
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
                                        1000);
                                return;
                            }
                            highlightFallback(preferenceFragment, preference);
                        },
                        200);
                return;
            }
        }
        highlightFallback(preferenceFragment, preference);
    }

    /**
     * Alternative highlight method if accessing the view did not work
     */
    private static void highlightFallback(final PreferenceFragmentCompat preferenceFragment,
                                          final Preference preference) {
        final Drawable oldIcon = preference.getIcon();
        final boolean oldSpaceReserved = preference.isIconSpaceReserved();
        Drawable arrow = AppCompatResources.getDrawable(preferenceFragment.getContext(), R.drawable.searchpreference_ic_arrow_right);
        int color = getColorFromAttr(preferenceFragment.getContext(), android.R.attr.textColorPrimary);
        arrow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        preference.setIcon(arrow);
        preferenceFragment.scrollToPreference(preference);
        new Handler().postDelayed(() -> {
            preference.setIcon(oldIcon);
            preference.setIconSpaceReserved(oldSpaceReserved);
        }, 1000);
    }

    private static int getColorFromAttr(final Context context, final int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        TypedArray arr = context.obtainStyledAttributes(typedValue.data, new int[]{
                android.R.attr.textColorPrimary});
        int color = arr.getColor(0, 0xff3F51B5);
        arr.recycle();
        return color;
    }
}
