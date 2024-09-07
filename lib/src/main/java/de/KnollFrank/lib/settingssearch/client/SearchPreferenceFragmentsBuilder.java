package de.KnollFrank.lib.settingssearch.client;

import androidx.fragment.app.FragmentManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.fragment.DefaultFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.FragmentFactory;
import de.KnollFrank.lib.settingssearch.provider.IsPreferenceSearchable;
import de.KnollFrank.lib.settingssearch.provider.PreferenceDialogAndSearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.provider.PreferenceScreenGraphAvailableListener;
import de.KnollFrank.lib.settingssearch.provider.PrepareShow;
import de.KnollFrank.lib.settingssearch.provider.ShowPreferencePath;
import de.KnollFrank.lib.settingssearch.search.provider.PreferenceDescription;

public class SearchPreferenceFragmentsBuilder {

    private final SearchConfiguration searchConfiguration;
    private final FragmentManager fragmentManager;
    private FragmentFactory fragmentFactory = new DefaultFragmentFactory();
    private List<PreferenceDescription> preferenceDescriptions = Collections.emptyList();
    private PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider = (hostOfPreference, preference) -> Optional.empty();
    private IsPreferenceSearchable isPreferenceSearchable = (preference, host) -> true;
    private PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener = preferenceScreenGraph -> {
    };
    private ShowPreferencePath showPreferencePath = preferencePath -> preferencePath.getPreference().isPresent();
    private PrepareShow prepareShow = preferenceFragment -> {
    };

    protected SearchPreferenceFragmentsBuilder(final SearchConfiguration searchConfiguration,
                                               final FragmentManager fragmentManager) {
        this.searchConfiguration = searchConfiguration;
        this.fragmentManager = fragmentManager;
    }

    public SearchPreferenceFragmentsBuilder withFragmentFactory(final FragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceDescriptions(final List<PreferenceDescription> preferenceDescriptions) {
        this.preferenceDescriptions = preferenceDescriptions;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceDialogAndSearchableInfoProvider(final PreferenceDialogAndSearchableInfoProvider preferenceDialogAndSearchableInfoProvider) {
        this.preferenceDialogAndSearchableInfoProvider = preferenceDialogAndSearchableInfoProvider;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withIsPreferenceSearchable(final IsPreferenceSearchable isPreferenceSearchable) {
        this.isPreferenceSearchable = isPreferenceSearchable;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPreferenceScreenGraphAvailableListener(final PreferenceScreenGraphAvailableListener preferenceScreenGraphAvailableListener) {
        this.preferenceScreenGraphAvailableListener = preferenceScreenGraphAvailableListener;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withShowPreferencePath(final ShowPreferencePath showPreferencePath) {
        this.showPreferencePath = showPreferencePath;
        return this;
    }

    public SearchPreferenceFragmentsBuilder withPrepareShow(final PrepareShow prepareShow) {
        this.prepareShow = prepareShow;
        return this;
    }

    public SearchPreferenceFragments build() {
        return new SearchPreferenceFragments(
                searchConfiguration,
                fragmentFactory,
                preferenceDescriptions,
                preferenceDialogAndSearchableInfoProvider,
                isPreferenceSearchable,
                preferenceScreenGraphAvailableListener,
                showPreferencePath,
                prepareShow,
                fragmentManager);
    }
}