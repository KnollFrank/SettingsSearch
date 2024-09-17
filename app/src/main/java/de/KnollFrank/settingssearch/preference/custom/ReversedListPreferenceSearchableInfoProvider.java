package de.KnollFrank.settingssearch.preference.custom;

import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;

public class ReversedListPreferenceSearchableInfoProvider implements SearchableInfoProvider<ReversedListPreference> {

    @Override
    public String getSearchableInfo(final ReversedListPreference reversedListPreference) {
        return join(reversedListPreference.getEntries());
    }

    private static String join(final CharSequence[] charSequences) {
        return charSequences == null ?
                "" :
                String.join(", ", charSequences);
    }
}
