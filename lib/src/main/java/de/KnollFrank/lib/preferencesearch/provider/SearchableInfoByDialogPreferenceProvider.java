package de.KnollFrank.lib.preferencesearch.provider;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;

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
        final PreferenceFragmentCompat preferenceFragment =
                instantiateAndInitialize(preferenceScreenWithHost.host);
        return Preferences
                .getAllChildren(preferenceScreenWithHost.preferenceScreen)
                .stream()
                .filter(preference -> preference instanceof DialogPreference)
                .map(preference -> (DialogPreference) preference)
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                dialogPreference -> getOptionalSearchableInfo(dialogPreference, preferenceFragment)));
    }

    private PreferenceFragmentCompat instantiateAndInitialize(final Class<? extends PreferenceFragmentCompat> classOfPreferenceFragment) {
        final PreferenceFragmentCompat preferenceFragment =
                (PreferenceFragmentCompat) fragments.instantiateAndInitializeFragment(classOfPreferenceFragment.getName());
        preferenceFragment.onStart();
        return preferenceFragment;
    }

    private Optional<String> getOptionalSearchableInfo(final DialogPreference dialogPreference,
                                                       final PreferenceFragmentCompat preferenceFragment) {
        return this
                .findDialogPreference(preferenceFragment, Optional.ofNullable(dialogPreference.getKey()))
                .flatMap(this::getSearchableInfo);
    }

    private Optional<DialogPreference> findDialogPreference(final PreferenceFragmentCompat preferenceFragment,
                                                            final Optional<String> key) {
        return key.map(preferenceFragment::findPreference);
    }

    private Optional<String> getSearchableInfo(final DialogPreference dialogPreference) {
        // prevent NullPointerException
        if (dialogPreference instanceof DropDownPreference) {
            return Optional.empty();
        }
        return getStringFromDialogFragment(dialogPreference, this::getSearchableInfo);
    }

    private Optional<String> getStringFromDialogFragment(
            final DialogPreference dialogPreference,
            final Function<DialogFragment, Optional<? extends String>> getString) {
        // FK-TODO: refactor
        final Optional<DialogFragment> dialogFragment = getDialog(dialogPreference);
        final Optional<String> searchableInfo = dialogFragment.flatMap(getString);
        dialogFragment.ifPresent(DialogFragment::dismiss);
        return searchableInfo;
    }

    private Optional<DialogFragment> getDialog(final DialogPreference dialogPreference) {
        dialogPreference.performClick();
        fragmentManager.executePendingTransactions();
        return fragmentManager
                .getFragments()
                .stream()
                .filter(fragment -> fragment instanceof DialogFragment)
                .map(fragment -> (DialogFragment) fragment)
                .findFirst();
        // FK-TODO: das ist doch immer ein echtes DialogFragment und nie ein Optional.empty(), oder?
    }

    private Optional<String> getSearchableInfo(final DialogFragment dialogFragment) {
        return dialogFragment instanceof final SearchableInfoProvider2 searchableInfoProvider ?
                Optional.of(searchableInfoProvider.getSearchableInfo()) :
                Optional.empty();
    }
}
