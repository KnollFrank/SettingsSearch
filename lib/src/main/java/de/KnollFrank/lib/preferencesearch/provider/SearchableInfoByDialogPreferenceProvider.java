package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.search.provider.HasSearchableInfo;

class SearchableInfoByDialogPreferenceProvider {

    private final Fragments fragments;
    private final FragmentManager fragmentManager;
    private final Map<Class<? extends Preference>, String> tagOfDialogFragmentByPreference;

    public SearchableInfoByDialogPreferenceProvider(final Fragments fragments,
                                                    final FragmentManager fragmentManager,
                                                    final Map<Class<? extends Preference>, String> tagOfDialogFragmentByPreference) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.tagOfDialogFragmentByPreference = tagOfDialogFragmentByPreference;
    }

    public Map<DialogPreference, String> getSearchableInfoByDialogPreference(
            final Collection<PreferenceScreenWithHost> preferenceScreenWithHostList) {
        return Maps.merge(
                preferenceScreenWithHostList
                        .stream()
                        .map(this::getSearchableInfoByDialogPreference)
                        .collect(Collectors.toList()));
    }

    private Map<DialogPreference, String> getSearchableInfoByDialogPreference(
            final PreferenceScreenWithHost preferenceScreenWithHost) {
        return Maps.filterPresentValues(
                getOptionalSearchableInfoByDialogPreference(
                        preferenceScreenWithHost));
    }

    private Map<DialogPreference, Optional<String>> getOptionalSearchableInfoByDialogPreference(
            final PreferenceScreenWithHost preferenceScreenWithHost) {
        final ClickableDialogPreferenceProvider clickableDialogPreferenceProvider =
                ClickableDialogPreferenceProvider.create(
                        preferenceScreenWithHost.host,
                        fragments::instantiateAndInitializeFragment);
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .filter(preference -> preference instanceof DialogPreference)
                .map(preference -> (DialogPreference) preference)
                .filter(dialogPreference -> tagOfDialogFragmentByPreference.containsKey(dialogPreference.getClass()))
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                dialogPreference ->
                                        clickableDialogPreferenceProvider
                                                .asClickableDialogPreference(dialogPreference)
                                                .flatMap(this::getSearchableInfo)));
    }

    private Optional<String> getSearchableInfo(final DialogPreference dialogPreference) {
        final DialogPreferences dialogPreferences =
                new DialogPreferences(
                        fragmentManager,
                        tagOfDialogFragmentByPreference.get(dialogPreference.getClass()));
        return dialogPreferences.getStringFromDialogFragment(dialogPreference, this::getSearchableInfo);
    }

    private Optional<String> getSearchableInfo(final DialogFragment dialogFragment) {
        return dialogFragment instanceof final HasSearchableInfo hasSearchableInfo ?
                Optional.of(hasSearchableInfo.getSearchableInfo()) :
                Optional.empty();
    }
}
