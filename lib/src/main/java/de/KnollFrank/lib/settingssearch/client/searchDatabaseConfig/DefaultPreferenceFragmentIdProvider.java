package de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig;

import androidx.preference.PreferenceFragmentCompat;

import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGenerator;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;

public class DefaultPreferenceFragmentIdProvider implements PreferenceFragmentIdProvider {

    private final IdGenerator idGenerator = IdGeneratorFactory.createIdGeneratorStartingAt(1);

    @Override
    public String getId(final PreferenceFragmentCompat preferenceFragment) {
        return String.valueOf(idGenerator.nextId());
    }
}
