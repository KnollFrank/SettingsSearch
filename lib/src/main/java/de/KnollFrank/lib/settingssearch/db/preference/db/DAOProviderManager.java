package de.KnollFrank.lib.settingssearch.db.preference.db;

import android.os.PersistableBundle;

import androidx.fragment.app.FragmentActivity;

import java.util.Optional;

public class DAOProviderManager {

    private Optional<DAOProvider> daoProvider = Optional.empty();
    private static final Object LOCK = new Object();

    public void initDAOProvider(final PreferencesDatabaseConfig preferencesDatabaseConfig,
                                final PersistableBundle configuration,
                                final FragmentActivity activityContext) {
        if (daoProvider.isEmpty()) {
            synchronized (LOCK) {
                if (daoProvider.isEmpty()) {
                    daoProvider = Optional.of(DAOProviderFactory.createDAOProvider(preferencesDatabaseConfig, configuration, activityContext));
                }
            }
        }
    }

    public DAOProvider getDAOProvider() {
        return daoProvider.orElseThrow(() -> new IllegalStateException("DAOProviderManager is not initialized. Call init() first."));
    }
}
