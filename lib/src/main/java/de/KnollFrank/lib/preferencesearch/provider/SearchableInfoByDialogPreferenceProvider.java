package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.DropDownPreference;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceScreenWithHost;
import de.KnollFrank.lib.preferencesearch.common.Maps;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.fragment.Fragments;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoProvider2;

class SearchableInfoByDialogPreferenceProvider {

    private final Fragments fragments;
    private final FragmentManager fragmentManager;

    public SearchableInfoByDialogPreferenceProvider(final Fragments fragments,
                                                    final FragmentManager fragmentManager) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
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
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                dialogPreference ->
                                        clickableDialogPreferenceProvider
                                                .getClickableDialogPreference(dialogPreference)
                                                .flatMap(this::getSearchableInfo)));
    }

    private Optional<String> getSearchableInfo(final DialogPreference dialogPreference) {
        return getStringFromDialogFragment(dialogPreference, this::getSearchableInfo);
    }

    // FK-TODO: diese Dialog-Methoden in eine neue Klasse verschieben
    private Optional<String> getStringFromDialogFragment(
            final DialogPreference dialogPreference,
            final Function<DialogFragment, Optional<? extends String>> getString) {
        // FK-TODO: refactor
        final Optional<DialogFragment> dialogFragment = getDialogFragment(dialogPreference);
        final Optional<String> searchableInfo = dialogFragment.flatMap(getString);
        dialogFragment.ifPresent(DialogFragment::dismiss);
        return searchableInfo;
    }

    private Optional<DialogFragment> getDialogFragment(final DialogPreference dialogPreference) {
        // prevent NullPointerException
        if (dialogPreference instanceof DropDownPreference) {
            return Optional.empty();
        }
        dialogPreference.performClick();
        fragmentManager.executePendingTransactions();
        return fragmentManager
                .getFragments()
                .stream()
                .filter(fragment -> fragment instanceof DialogFragment)
                .map(fragment -> (DialogFragment) fragment)
                .findFirst();
    }

    private Optional<String> getSearchableInfo(final DialogFragment dialogFragment) {
        return dialogFragment instanceof final SearchableInfoProvider2 searchableInfoProvider ?
                Optional.of(searchableInfoProvider.getSearchableInfo()) :
                Optional.empty();
    }
}
