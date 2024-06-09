package de.KnollFrank.lib.preferencesearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// FK-TODO: extend PreferenceFragmentCompat instead of BaseSearchPreferenceFragment?
public class SearchResultsPreferenceFragment extends BaseSearchPreferenceFragment {

    private List<PreferenceWithHost> preferenceWithHostList = Collections.emptyList();
    private Consumer<PreferenceWithHost> onPreferenceClickListener;

    public void setPreferenceWithHostList(final List<PreferenceWithHost> preferenceWithHostList) {
        final List<Preference> preferences = getPreferences(preferenceWithHostList);
        PreferencesRemover.removePreferencesFromTheirParents(preferences);
        // FK-TODO: replace with new instance method of SearchResultsPreferenceFragment
        this.onPreferenceClickListener =
                preferenceWithHost ->
                        ((SearchPreferenceResultListener) getActivity())
                                .onSearchResultClicked(getSearchPreferenceResult(preferenceWithHost));
        PreferencePreparer.preparePreferences(getPreferences(preferenceWithHostList));
        setPreferencesOnOptionalPreferenceScreen(preferences);
        this.preferenceWithHostList = preferenceWithHostList;
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final PreferenceScreen preferenceScreen = createPreferenceScreen();
        PreferencesSetter.addPreferences2PreferenceScreen(
                getPreferences(this.preferenceWithHostList),
                preferenceScreen);
        setPreferenceScreen(preferenceScreen);
    }

    @NonNull
    @Override
    protected Adapter onCreateAdapter(@NonNull final PreferenceScreen preferenceScreen) {
        // FK-TODO: refactor by extracting sub class?
        return new PreferenceGroupAdapter(preferenceScreen) {
            @Override
            public void onBindViewHolder(@NonNull final PreferenceViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.setOnClickListener(
                        v -> onPreferenceClickListener.accept(getPreferenceWithHost(position)));
            }

            private PreferenceWithHost getPreferenceWithHost(final int position) {
                return getPreferenceWithHost(getItem(position));
            }

            private PreferenceWithHost getPreferenceWithHost(final Preference preference) {
                return preferenceWithHostList
                        .stream()
                        .filter(preferenceWithHost -> preferenceWithHost.preference.equals(preference))
                        .findFirst()
                        .get();
            }
        };
    }

    private List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
    }

    private static SearchPreferenceResult getSearchPreferenceResult(final PreferenceWithHost preferenceWithHost) {
        return new SearchPreferenceResult(
                preferenceWithHost.preference.getKey(),
                preferenceWithHost.host
        );
    }

    private static class PreferencesRemover {

        public static void removePreferencesFromTheirParents(final Collection<Preference> preferences) {
            preferences.forEach(PreferencesRemover::removePreferenceFromItsParent);
        }

        private static void removePreferenceFromItsParent(final Preference preference) {
            final PreferenceGroup parent = preference.getParent();
            if (parent != null) {
                parent.removePreference(preference);
            }
        }
    }

    private static class PreferencePreparer {

        public static void preparePreferences(final List<Preference> preferences) {
            preferences.forEach(PreferencePreparer::preparePreference);
        }

        private static void preparePreference(final Preference preference) {
            preference.setEnabled(false);
            preference.setShouldDisableView(false);
        }
    }

    private PreferenceScreen createPreferenceScreen() {
        return getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext());
    }

    private void setPreferencesOnOptionalPreferenceScreen(final List<Preference> preferences) {
        this
                .getOptionalPreferenceScreen()
                .ifPresent(
                        preferenceScreen ->
                                PreferencesSetter.setPreferencesOnPreferenceScreen(
                                        preferences,
                                        preferenceScreen));
    }

    private Optional<PreferenceScreen> getOptionalPreferenceScreen() {
        return Optional
                .ofNullable(getPreferenceManager())
                .map(PreferenceManager::getPreferenceScreen);
    }

    private static class PreferencesSetter {

        public static void setPreferencesOnPreferenceScreen(final List<Preference> preferences,
                                                            final PreferenceScreen preferenceScreen) {
            preferenceScreen.removeAll();
            addPreferences2PreferenceScreen(preferences, preferenceScreen);
        }

        public static void addPreferences2PreferenceScreen(final List<Preference> preferences,
                                                           final PreferenceScreen preferenceScreen) {
            preferences.forEach(preferenceScreen::addPreference);
        }
    }
}
