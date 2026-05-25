package de.KnollFrank.lib.settingssearch.results;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.util.OptionalInt;

import de.KnollFrank.lib.settingssearch.R;
import de.KnollFrank.lib.settingssearch.common.Attributes;
import de.KnollFrank.lib.settingssearch.common.StructuredPreferenceKey;

public class PreferenceHighlighter implements SettingHighlighter {

    @Override
    public void highlightSetting(final Fragment settingsFragment, final Setting setting) {
        final Duration duration = Duration.ofSeconds(1);
        final String key = setting.getKey();
        if (StructuredPreferenceKey.isStructuredKey(key)) {
            highlightGraphicalItem(settingsFragment, key, duration);
        } else if (settingsFragment instanceof final PreferenceFragmentCompat preferenceFragment) {
            highlightPreferenceOfPreferenceFragment(key, preferenceFragment, duration);
        }
    }

    private void highlightGraphicalItem(final Fragment fragment, final String key, final Duration duration) {
        final OptionalInt index = StructuredPreferenceKey.getIndex(key);
        if (index.isPresent()) {
            final RecyclerView recyclerView = findRecyclerView(fragment.getView());
            if (recyclerView != null) {
                recyclerView.post(() -> ViewAtPositionHighlighter.highlightViewAtPosition(recyclerView, index.getAsInt(), duration, () -> {
                }));
            }
        }
    }

    private static RecyclerView findRecyclerView(final View view) {
        // FK-TODO: DRY, da dieses Muster immer wieder auftritt
        if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        } else if (view instanceof final ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                final RecyclerView found = findRecyclerView(viewGroup.getChildAt(i));
                if (found != null) {
                    return found;
                }
            }
        }
        // FK-TODO: return Optional<RecyclerView>
        return null;
    }

    private static void highlightPreferenceOfPreferenceFragment(final String keyOfPreference,
                                                                final PreferenceFragmentCompat preferenceFragment,
                                                                final Duration highlightDuration) {
        // FK-TODO: use Optional
        final Preference preference = preferenceFragment.findPreference(keyOfPreference);
        if (preference != null) {
            highlightPreferenceOfPreferenceFragment(preference, preferenceFragment, highlightDuration);
        }
    }

    private static void highlightPreferenceOfPreferenceFragment(final Preference preference,
                                                                final PreferenceFragmentCompat preferenceFragment,
                                                                final Duration highlightDuration) {
        // FK-TODO: use new Handler(Looper.getMainLooper())?
        new Handler().post(() -> doHighlightPreferenceOfPreferenceFragment(preference, preferenceFragment, highlightDuration));
    }

    private static void doHighlightPreferenceOfPreferenceFragment(final Preference preference,
                                                                  final PreferenceFragmentCompat preferenceFragment,
                                                                  final Duration highlightDuration) {
        PreferenceHighlighter
                .getPreferenceAdapterPosition(
                        preference,
                        preferenceFragment.getListView().getAdapter())
                .ifPresentOrElse(
                        preferenceAdapterPosition -> highlightPreference(preference, preferenceFragment, highlightDuration, preferenceAdapterPosition),
                        () -> highlightFallback(preference, preferenceFragment, highlightDuration));
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
        ViewAtPositionHighlighter.highlightViewAtPosition(
                preferenceFragment.getListView(),
                position,
                highlightDuration,
                () -> highlightFallback(preference, preferenceFragment, highlightDuration));
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
        // FK-TODO: use new Handler(Looper.getMainLooper())?
        new Handler().postDelayed(
                () -> {
                    preference.setIcon(oldIcon);
                    preference.setIconSpaceReserved(oldSpaceReserved);
                },
                highlightDuration.toMillis());
    }
}
