package de.KnollFrank.lib.settingssearch.results;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Duration;

import java.util.Objects;
import java.util.OptionalInt;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.Attributes;

public class PreferenceHighlighter implements SettingHighlighter {

    @Override
    public void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        highlightPreferenceOfPreferenceFragment(
                setting.getKey(),
                (PreferenceFragmentCompat) settingsFragment,
                Duration.ofSeconds(1));
    }

    private static void highlightPreferenceOfPreferenceFragment(final String keyOfPreference,
                                                                final PreferenceFragmentCompat preferenceFragment,
                                                                final Duration highlightDuration) {
        highlightPreferenceOfPreferenceFragment(
                Objects.<Preference>requireNonNull(preferenceFragment.findPreference(keyOfPreference)),
                preferenceFragment,
                highlightDuration);
    }

    private static void highlightPreferenceOfPreferenceFragment(final Preference preference,
                                                                final PreferenceFragmentCompat preferenceFragment,
                                                                final Duration highlightDuration) {
        new Handler().post(() -> doHighlightPreferenceOfPreferenceFragment(preference, preferenceFragment, highlightDuration));
    }

    private static void doHighlightPreferenceOfPreferenceFragment(final Preference preference,
                                                                  final PreferenceFragmentCompat preferenceFragment,
                                                                  final Duration highlightDuration) {
        final OptionalInt preferenceAdapterPosition =
                getPreferenceAdapterPosition(
                        preference,
                        preferenceFragment.getListView().getAdapter());
        if (preferenceAdapterPosition.isPresent()) {
            highlightPreference(
                    preference,
                    preferenceFragment,
                    highlightDuration,
                    preferenceAdapterPosition.getAsInt());
        } else {
            highlightFallback(preference, preferenceFragment, highlightDuration);
        }
    }

    private static OptionalInt getPreferenceAdapterPosition(final Preference preference,
                                                            final RecyclerView.Adapter<?> adapter) {
        if (adapter instanceof final PreferenceGroup.PreferencePositionCallback preferencePosition) {
            final int position = preferencePosition.getPreferenceAdapterPosition(preference);
            if (position != RecyclerView.NO_POSITION) {
                return OptionalInt.of(position);
            }
        }
        return OptionalInt.empty();
    }

    private static void highlightPreference(final Preference preference,
                                            final PreferenceFragmentCompat preferenceFragment,
                                            final Duration highlightDuration,
                                            final int position) {
        final RecyclerView recyclerView = preferenceFragment.getListView();
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
