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

public class SearchPreferenceResult {

    private final String key;
    private final Class<? extends PreferenceFragmentCompat> preferenceFragmentClass;

    public SearchPreferenceResult(final String key,
                                  final Class<? extends PreferenceFragmentCompat> preferenceFragmentClass) {
        this.key = key;
        this.preferenceFragmentClass = preferenceFragmentClass;
    }

    /**
     * Returns the key of the preference pressed
     *
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the file in which the result was found
     *
     * @return The file in which the result was found
     */
    public Class<? extends PreferenceFragmentCompat> getPreferenceFragmentClass() {
        return preferenceFragmentClass;
    }

    /**
     * Highlight the preference that was found
     *
     * @param prefsFragment Fragment that contains the preference
     */
    public void highlight(final PreferenceFragmentCompat prefsFragment) {
        new Handler().post(() -> doHighlight(prefsFragment));
    }

    private void doHighlight(final PreferenceFragmentCompat prefsFragment) {
        final Preference prefResult = prefsFragment.findPreference(getKey());
        if (prefResult == null) {
            Log.e("doHighlight", "Preference not found on given screen");
            return;
        }
        final RecyclerView recyclerView = prefsFragment.getListView();
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter instanceof final PreferenceGroup.PreferencePositionCallback callback) {
            final int position = callback.getPreferenceAdapterPosition(prefResult);
            if (position != RecyclerView.NO_POSITION) {
                recyclerView.scrollToPosition(position);
                recyclerView.postDelayed(
                        () -> {
                            final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                            if (holder != null) {
                                final Drawable oldBackground = holder.itemView.getBackground();
                                final int color = getColorFromAttr(prefsFragment.getContext(), android.R.attr.textColorPrimary);
                                holder.itemView.setBackgroundColor(color & 0xffffff | 0x33000000);
                                new Handler().postDelayed(
                                        () -> holder.itemView.setBackgroundDrawable(oldBackground),
                                        1000);
                                return;
                            }
                            highlightFallback(prefsFragment, prefResult);
                        },
                        200);
                return;
            }
        }
        highlightFallback(prefsFragment, prefResult);
    }

    /**
     * Alternative highlight method if accessing the view did not work
     */
    private void highlightFallback(PreferenceFragmentCompat prefsFragment, final Preference prefResult) {
        final Drawable oldIcon = prefResult.getIcon();
        final boolean oldSpaceReserved = prefResult.isIconSpaceReserved();
        Drawable arrow = AppCompatResources.getDrawable(prefsFragment.getContext(), R.drawable.searchpreference_ic_arrow_right);
        int color = getColorFromAttr(prefsFragment.getContext(), android.R.attr.textColorPrimary);
        arrow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        prefResult.setIcon(arrow);
        prefsFragment.scrollToPreference(prefResult);
        new Handler().postDelayed(() -> {
            prefResult.setIcon(oldIcon);
            prefResult.setIconSpaceReserved(oldSpaceReserved);
        }, 1000);
    }

    private int getColorFromAttr(Context context, int attr) {
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
