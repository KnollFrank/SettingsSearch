package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.lib.settingssearch.client.CreateSearchDatabaseTaskProvider.FRAGMENT_CONTAINER_VIEW_ID;
import static de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference.ADDITIONAL_PREFERENCE_KEY;
import static de.KnollFrank.settingssearch.preference.fragment.PreferenceFragmentWithSinglePreference.ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.common.collect.Iterables;

import java.util.Optional;
import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.SearchDatabaseConfig;
import de.KnollFrank.lib.settingssearch.db.preference.converter.IdGeneratorFactory;
import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverterFactory;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.fragment.FragmentInitializerFactory;
import de.KnollFrank.lib.settingssearch.fragment.InstantiateAndInitializeFragmentFactory;
import de.KnollFrank.lib.settingssearch.fragment.PreferenceDialogsFactory;
import de.KnollFrank.settingssearch.PreferenceSearchExample;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.SearchDatabaseConfigFactory;
import de.KnollFrank.settingssearch.SettingsActivity;
import de.KnollFrank.settingssearch.SettingsActivity3;
import de.KnollFrank.settingssearch.preference.custom.CustomDialogPreference;

public class PrefsFragmentFirst extends PreferenceFragmentCompat implements OnPreferenceClickListener {

    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summaryOfSrcPreferenceWithExtras";
    public static final String BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summaryOfSrcPreferenceWithoutExtras";
    public static final String KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "keyOfSrcPreferenceWithoutExtras";
    public static final String NON_STANDARD_LINK_TO_SECOND_FRAGMENT = "non_standard_link_to_second_fragment";
    public static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY = "addPreferenceToPreferenceFragmentWithSinglePreferenceKey";
    private static final String KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER = "keyOfPreferenceWithOnPreferenceClickListener";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS = "summary of src preference with extras";
    public static final String SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS = "summary of src preference without extras";
    public static final String SUMMARY_CHANGING_PREFERENCE_KEY = "summaryChangingPreference";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.preferences_multiple_screens);
        getPreferenceScreen().addPreference(createAddPreferenceToP1CheckBoxPreference());
        getPreferenceScreen().addPreference(createPreferenceWithoutExtrasConnectedToPreferenceFragmentWithSinglePreference());
        getPreferenceScreen().addPreference(createPreferenceWithExtrasConnectedToPreferenceFragmentWithSinglePreference());
        getPreferenceScreen().findPreference(NON_STANDARD_LINK_TO_SECOND_FRAGMENT).setIcon(R.drawable.face);
        getPreferenceScreen().findPreference("preferenceWithIntent").setIntent(createIntent(SettingsActivity.class, createExtrasForSettingsActivity()));
        getPreferenceScreen().findPreference("preferenceWithIntent3").setIntent(new Intent(getContext(), SettingsActivity3.class));
        configureSummaryChangingPreference();
        setOnPreferenceClickListeners();
    }

    private CheckBoxPreference createAddPreferenceToP1CheckBoxPreference() {
        final CheckBoxPreference checkBoxPreference = new CheckBoxPreference(requireContext());
        checkBoxPreference.setKey(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE_KEY);
        checkBoxPreference.setTitle("add preference to P1");
        checkBoxPreference.setOnPreferenceChangeListener(
                new OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull final Preference preference, final Object checked) {
                        if ((boolean) checked) {
                            addPreferenceToP1();
                        } else {
                            removePreferenceFromP1();
                        }
                        return true;
                    }

                    private void addPreferenceToP1() {
                        final SearchablePreferenceDAO searchablePreferenceDAO = getSearchablePreferenceDAO();
                        final SearchDatabaseConfig searchDatabaseConfig = SearchDatabaseConfigFactory.createSearchDatabaseConfig();
                        searchablePreferenceDAO.persist(
                                InstantiateAndInitializeFragmentFactory
                                        .createInstantiateAndInitializeFragment(
                                                searchDatabaseConfig.fragmentFactory,
                                                FragmentInitializerFactory.createFragmentInitializer(requireActivity(), FRAGMENT_CONTAINER_VIEW_ID),
                                                requireContext())
                                        .instantiateAndInitializeFragment(
                                                PreferenceFragmentWithSinglePreference.class,
                                                // FK-FIXME: use real src
                                                Optional.empty())
                                        .createAdditionalSearchablePreference(
                                                searchablePreferenceDAO,
                                                Preference2SearchablePreferenceConverterFactory.createPreference2SearchablePreferenceConverter(
                                                        searchDatabaseConfig,
                                                        PreferenceDialogsFactory.createPreferenceDialogs(requireActivity(), FRAGMENT_CONTAINER_VIEW_ID),
                                                        IdGeneratorFactory.createIdGeneratorStartingAt(
                                                                searchablePreferenceDAO
                                                                        .getMaxId()
                                                                        .map(maxId -> maxId + 1)
                                                                        .orElse(0)))));
                    }

                    private void removePreferenceFromP1() {
                        final SearchablePreferenceDAO searchablePreferenceDAO = getSearchablePreferenceDAO();
                        searchablePreferenceDAO.remove(
                                Iterables.getOnlyElement(
                                        searchablePreferenceDAO
                                                .findPreferenceByKeyAndHost(
                                                        ADDITIONAL_PREFERENCE_KEY,
                                                        PreferenceFragmentWithSinglePreference.class)));
                    }
                });
        return checkBoxPreference;
    }

    private void configureSummaryChangingPreference() {
        final SwitchPreference summaryChangingPreference = getPreferenceScreen().findPreference(SUMMARY_CHANGING_PREFERENCE_KEY);
        summaryChangingPreference.setSummary(getSummary(summaryChangingPreference.isChecked()));
        summaryChangingPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(@NonNull final Preference preference, final Object checked) {
                        setSummary(preference, getSummary((boolean) checked));
                        return true;
                    }

                    private void setSummary(final Preference preference, final String summary) {
                        preference.setSummary(summary);
                        final SearchablePreferenceDAO searchablePreferenceDAO = getSearchablePreferenceDAO();
                        final SearchablePreference searchablePreference =
                                Iterables.getOnlyElement(
                                        searchablePreferenceDAO
                                                .findPreferenceByKeyAndHost(
                                                        preference.getKey(),
                                                        PrefsFragmentFirst.this.getClass()));
                        searchablePreference.setSummary(Optional.of(summary));
                        searchablePreferenceDAO.update(searchablePreference);
                    }
                });
    }

    private SearchablePreferenceDAO getSearchablePreferenceDAO() {
        return ((PreferenceSearchExample) requireActivity())
                .getSearchablePreferenceDAO()
                .orElseThrow();
    }

    public static String getSummary(final boolean checked) {
        return checked ?
                "summaryChangingPreference is ON" :
                "summaryChangingPreference is OFF";
    }

    private Intent createIntent(final Class<? extends Activity> activityClass, final Bundle extras) {
        final Intent intent = new Intent(getContext(), activityClass);
        intent.putExtras(extras);
        return intent;
    }

    public static Bundle createExtrasForSettingsActivity() {
        final Bundle bundle = new Bundle();
        bundle.putString(SettingsActivity.SETTINGS_ACTIVITY_MANDATORY_DUMMY_KEY, "some mandatory dummy value");
        return bundle;
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        if (preference instanceof CustomDialogPreference) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull final Preference preference) {
        if (KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER.equals(preference.getKey())) {
            CustomDialogFragment.showInstance(getParentFragmentManager());
            return true;
        }
        if (NON_STANDARD_LINK_TO_SECOND_FRAGMENT.equals(preference.getKey())) {
            show(PrefsFragmentSecond.class.getName(), preference.getExtras());
            return true;
        }
        if (KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS.equals(preference.getKey())) {
            show(
                    PreferenceFragmentWithSinglePreference.class.getName(),
                    createArguments4PreferenceWithoutExtras(preference, requireContext()));
            return true;
        }
        return false;
    }

    public static Bundle createArguments4PreferenceWithoutExtras(final @NonNull Preference preference, final Context context) {
        final Bundle arguments = new Bundle();
        arguments.putString(BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS, preference.getSummary().toString());
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
                .of(
                        KEY_OF_PREFERENCE_WITH_ON_PREFERENCE_CLICK_LISTENER,
                        NON_STANDARD_LINK_TO_SECOND_FRAGMENT,
                        KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)
                .<Preference>map(this::findPreference)
                .forEach(preference -> preference.setOnPreferenceClickListener(this));
    }

    // adapted from PreferenceFragmentCompat.onPreferenceTreeClick()
    private void show(final String classNameOfFragment2Show, final Bundle arguments) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        final Fragment fragment =
                fragmentManager.getFragmentFactory().instantiate(
                        requireActivity().getClassLoader(),
                        classNameOfFragment2Show);
        fragment.setArguments(arguments);
        fragment.setTargetFragment(this, 0);
        fragmentManager.beginTransaction()
                // Attempt to replace this fragment in its root view - developers should
                // implement onPreferenceStartFragment in their activity so that they can
                // customize this behaviour and handle any transitions between fragments
                .replace(((View) requireView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
