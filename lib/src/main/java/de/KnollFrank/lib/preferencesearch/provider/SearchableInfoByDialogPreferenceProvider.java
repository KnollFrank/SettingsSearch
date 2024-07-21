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

    // FK-TODO: refactor
    private Optional<String> getSearchableInfo(final DialogPreference dialogPreference) {
        // prevent NullPointerException
        if (dialogPreference instanceof DropDownPreference) {
            return Optional.empty();
        }
        dialogPreference.performClick();
        fragmentManager.executePendingTransactions();
        final Optional<SearchableInfoProvider2> searchableInfoProvider =
                fragmentManager
                        .getFragments()
                        .stream()
                        .filter(fragment -> fragment instanceof SearchableInfoProvider2)
                        .map(fragment -> (SearchableInfoProvider2) fragment)
                        .findFirst();
        if (searchableInfoProvider.isPresent()) {
            final String searchableInfo = searchableInfoProvider.get().getSearchableInfo();
            ((DialogFragment) searchableInfoProvider.get()).dismiss();
            return Optional.of(searchableInfo);
        } else {
            final Optional<DialogFragment> dialogFragment =
                    fragmentManager
                            .getFragments()
                            .stream()
                            .filter(fragment -> fragment instanceof DialogFragment)
                            .map(fragment -> (DialogFragment) fragment)
                            .findFirst();
            dialogFragment.ifPresent(DialogFragment::dismiss);
            return Optional.empty();
        }
    }
}
