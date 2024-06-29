package de.KnollFrank.lib.preferencesearch.results;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import de.KnollFrank.lib.preferencesearch.R;
import de.KnollFrank.lib.preferencesearch.common.Attributes;

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
                                final @ColorInt int color = Attributes.getColorFromAttr(preferenceFragment.getContext(), android.R.attr.textColorPrimary);
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
        final @ColorInt int color = Attributes.getColorFromAttr(preferenceFragment.getContext(), android.R.attr.textColorPrimary);
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
}
