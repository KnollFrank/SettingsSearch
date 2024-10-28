package de.KnollFrank.lib.settingssearch.results;

import static de.KnollFrank.lib.settingssearch.fragment.Fragments.showFragment;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import org.threeten.bp.Duration;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePathPredicate;
import de.KnollFrank.lib.settingssearch.results.adapter.SearchablePreferenceGroupAdapter;

public class SearchResultsPreferenceFragment extends PreferenceFragmentCompat {

    private final MergedPreferenceScreen mergedPreferenceScreen;
    private final @IdRes int fragmentContainerViewId;
    private final ShowPreferencePathPredicate showPreferencePathPredicate;
    private final PrepareShow prepareShow;

    public SearchResultsPreferenceFragment(final MergedPreferenceScreen mergedPreferenceScreen,
                                           final @IdRes int fragmentContainerViewId,
                                           final ShowPreferencePathPredicate showPreferencePathPredicate,
                                           final PrepareShow prepareShow) {
        this.mergedPreferenceScreen = mergedPreferenceScreen;
        mergedPreferenceScreen.searchResultsPreferenceScreenHelper.preparePreferenceScreenForSearch();
        this.fragmentContainerViewId = fragmentContainerViewId;
        this.showPreferencePathPredicate = showPreferencePathPredicate;
        this.prepareShow = prepareShow;
    }

    @Override
    public void onCreatePreferences(@Nullable final Bundle savedInstanceState, @Nullable final String rootKey) {
        mergedPreferenceScreen.searchResultsPreferenceScreenHelper.setPreferenceScreen(this);
    }

    @NonNull
    @Override
    protected Adapter<PreferenceViewHolder> onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        // FK-TODO: die Preferences des preferenceScreen sollen ihren aktuellen Zustand widerspiegeln (z.B. soll der Haken einer CheckBoxPreference gemäß den darunterliegenden Daten gesetzt oder nicht gesetzt sein.)
        return new SearchablePreferenceGroupAdapter(
                preferenceScreen,
                mergedPreferenceScreen.searchResultsPreferenceScreenHelper.getSearchableInfoAttribute(),
                mergedPreferenceScreen.searchResultsPreferenceScreenHelper.getPreferencePathByPreference(),
                showPreferencePathPredicate,
                Set.of(),
                this::showPreferenceScreenAndHighlightPreference);
    }

    private void showPreferenceScreenAndHighlightPreference(final Preference preference) {
        showPreferenceScreenAndHighlightPreference(
                mergedPreferenceScreen.getHost(preference),
                preference);
    }

    private void showPreferenceScreenAndHighlightPreference(
            final PreferenceFragmentCompat fragmentOfPreferenceScreen,
            final Preference preference2Highlight) {
        prepareShow.prepareShow(fragmentOfPreferenceScreen);
        showFragment(
                fragmentOfPreferenceScreen,
                _fragmentOfPreferenceScreen -> {
                    if (preference2Highlight.hasKey()) {
                        highlightPreference(_fragmentOfPreferenceScreen, preference2Highlight.getKey());
                    }
                },
                true,
                fragmentContainerViewId,
                requireActivity().getSupportFragmentManager());
    }

    private static void highlightPreference(final PreferenceFragmentCompat preferenceFragment,
                                            final String keyOfPreference2Highlight) {
        preferenceFragment.scrollToPreference(keyOfPreference2Highlight);
        PreferenceHighlighter.highlightPreferenceOfPreferenceFragment(
                keyOfPreference2Highlight,
                preferenceFragment,
                Duration.ofSeconds(1));
    }
}
