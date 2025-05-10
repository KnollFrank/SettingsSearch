package de.KnollFrank.settingssearch.preference.fragment;

import static de.KnollFrank.settingssearch.preference.fragment.PrefsFragmentFirst.KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS;

import android.content.Context;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.common.collect.Iterables;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.converter.Preference2SearchablePreferenceConverter;
import de.KnollFrank.lib.settingssearch.db.preference.dao.SearchablePreferenceDAO;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

public class PreferenceFragmentWithSinglePreference extends PreferenceFragmentCompat {

    public static final String TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS = "title of dst preference coming from src with extras";
    public static final String TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS = "title of dst preference coming from src without extras";
    public static final String ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE = "addPreferenceToPreferenceFragmentWithSinglePreference";
    public static final String SOME_ADDITIONAL_PREFERENCE = "some additional preference";
    public static final String ADDITIONAL_PREFERENCE_KEY = "additionalPreference";

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        final Context context = getPreferenceManager().getContext();
        final PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS)) {
                screen.addPreference(
                        createPreference(
                                "keyOfPreferenceOfConnectedFragment1",
                                TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITH_EXTRAS,
                                arguments.getString(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITH_EXTRAS),
                                context));
            }
            if (arguments.containsKey(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS)) {
                screen.addPreference(
                        createPreference("keyOfPreferenceOfConnectedFragment2",
                                TITLE_OF_DST_PREFERENCE_COMING_FROM_SRC_WITHOUT_EXTRAS,
                                arguments.getString(PrefsFragmentFirst.BUNDLE_KEY_OF_SUMMARY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS),
                                context));
            }
            if (arguments.getBoolean(ADD_PREFERENCE_TO_PREFERENCE_FRAGMENT_WITH_SINGLE_PREFERENCE, false)) {
                screen.addPreference(createAdditionalPreference(context));
            }
        }
        setPreferenceScreen(screen);
    }

    private static Preference createAdditionalPreference(final Context context) {
        final Preference preference = new Preference(context);
        preference.setKey(ADDITIONAL_PREFERENCE_KEY);
        preference.setTitle(SOME_ADDITIONAL_PREFERENCE);
        return preference;
    }

    public SearchablePreference createAdditionalSearchablePreference(
            final SearchablePreferenceDAO searchablePreferenceDAO,
            final Preference2SearchablePreferenceConverter preference2SearchablePreferenceConverter) {
        final Preference preference = createAdditionalPreference(getPreferenceManager().getContext());
        return preference2SearchablePreferenceConverter
                .convert2POJO(
                        preference,
                        // FK-FIXME: -1
                        -1,
                        this,
                        Optional.empty(),
                        // FK-TODO: generalize because too many hard coded values
                        Optional.of(
                                Iterables.getOnlyElement(
                                        searchablePreferenceDAO.findPreferenceByKeyAndHost(
                                                KEY_OF_SRC_PREFERENCE_WITHOUT_EXTRAS,
                                                PrefsFragmentFirst.class))))
                .searchablePreference();
    }

    private Preference createPreference(final String key,
                                        final String title,
                                        final String summaryOfSrcPreference,
                                        final Context context) {
        final Preference preference = new Preference(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary("copied summary: " + summaryOfSrcPreference);
        return preference;
    }
}
