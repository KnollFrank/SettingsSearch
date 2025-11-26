package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE;
import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.show;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.Locales;
import de.KnollFrank.lib.settingssearch.db.preference.db.DAOProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenGraph;
import de.KnollFrank.settingssearch.Configuration;
import de.KnollFrank.settingssearch.ConfigurationProvider;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;
import de.KnollFrank.settingssearch.SettingsSearchApplication;

public class PrefsFragmentFifth extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summaryOfSrcPreferenceWithExtras";
    public static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY = "addPreferenceToPreferenceFragmentWithSinglePreferenceKey";
    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summaryOfSrcPreferenceWithoutExtras";
    public static final String KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "keyOfSrcPreferenceWithoutExtras";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summary of src preference with extras";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summary of src preference without extras";
    public static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_TITLE = "add preference to P1";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences5);
        getPreferenceScreen().addPreference(createAddPreferenceToP1CheckBoxPreference(Locales.getCurrentLanguageLocale(getResources())));
        getPreferenceScreen().addPreference(createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference());
        getPreferenceScreen().addPreference(createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference());
        setOnPreferenceClickListeners();
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if (KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(preference.getKey())) {
            show(
                    PreferenceFragmentWithSinglePreference.class.getName(),
                    createArguments4PreferenceWithoutExtras(preference, requireContext()),
                    this);
            return true;
        }
        return false;
    }

    private CheckBoxPreference createAddPreferenceToP1CheckBoxPreference(final Locale locale) {
        final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(requireContext());
        checkBoxPreference.setKey(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY);
        checkBoxPreference.setTitle(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_TITLE);
        checkBoxPreference.setOnPreferenceClickListener(
                preference -> {
                    new SearchDatabaseRootedAtPrefsFragmentFifthAdapter().adaptSearchDatabaseRootedAtPrefsFragmentFifth(
                            getPreferencesDatabase(),
                            getPojoGraph(locale),
                            new Configuration(
                                    checkBoxPreference.isChecked(),
                                    ConfigurationProvider
                                            .getActualConfiguration(requireContext())
                                            .summaryChangingPreference()),
                            SearchDatabaseConfigFactory.createSearchDatabaseConfig(),
                            requireActivity());
                    return true;
                });
        return checkBoxPreference;
    }

    private SearchablePreferenceScreenGraph getPojoGraph(final Locale locale) {
        return getPreferencesDatabase()
                .searchablePreferenceScreenGraphDAO()
                .findGraphById(locale)
                .orElseThrow();
    }

    private DAOProvider getPreferencesDatabase() {
        return SettingsSearchApplication
                .getInstanceFromContext(requireContext())
                .daoProviderManager
                .getDAOProvider();
    }

    public static Bundle createArguments4PreferenceWithoutExtras(final @NonNull Preference preference,
                                                                 final Context context) {
        return createArguments4PreferenceWithoutExtras(preference.getSummary().toString(), context);
    }

    private static Bundle createArguments4PreferenceWithoutExtras(final String summary, final Context context) {
        final Bundle arguments = new Bundle();
        arguments.putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS, summary);
        arguments.putBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE, PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY, false));
        return arguments;
    }

    private Preference createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("preference with extras from src to dst");
        preference.setKey("keyOfSrcPreferenceWithExtras");
        final String summary = SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS;
        preference.setSummary(summary);
        preference.getExtras().putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS, summary);
        return preference;
    }

    private Preference createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference() {
        final Preference preference = new Preference(requireContext());
        preference.setFragment(PreferenceFragmentWithSinglePreference.class.getName());
        preference.setTitle("P1: preference without extras from src to dst");
        preference.setKey(KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        preference.setSummary(SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS);
        return preference;
    }

    private void setOnPreferenceClickListeners() {
        Stream
                .of(KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }
}
