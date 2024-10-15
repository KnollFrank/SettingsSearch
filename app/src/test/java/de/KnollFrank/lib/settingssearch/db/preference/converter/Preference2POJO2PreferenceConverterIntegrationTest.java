package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.UIUtils.drawable2Bitmap;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getPreferenceScreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;
import java.util.function.BiConsumer;

import de.KnollFrank.lib.settingssearch.db.SearchablePreferenceTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Preference2POJO2PreferenceConverterIntegrationTest {

    @Test
    public void test_iconOfPreference_survives_convertPreference2POJO2Preference() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final PreferenceScreenWithPreference preferenceScreenWithPreference = createPreferenceScreenWithPreference(activity);
                final Preference preference = preferenceScreenWithPreference.preference();

                // When
                final SearchablePreferencePOJO pojo = convertPreference2POJO(preference);
                final Preference preferenceFromPOJO = convertPOJO2Preference(pojo, preferenceScreenWithPreference.preferenceScreen());

                // Then
                assertThat(equals(preferenceFromPOJO.getIcon(), preference.getIcon()), is(true));
            });
        }
    }

    private record PreferenceScreenWithPreference(PreferenceScreen preferenceScreen,
                                                  Preference preference) {
    }

    private static PreferenceScreenWithPreference createPreferenceScreenWithPreference(final TestActivity activity) {
        final BiConsumer<PreferenceScreen, Context> addPreferences2Screen =
                new BiConsumer<>() {

                    @Override
                    public void accept(final PreferenceScreen screen, final Context context) {
                        screen.addPreference(createPreference(context));
                    }

                    private Preference createPreference(final Context context) {
                        final Preference preference = new Preference(context);
                        final Resources res = activity.getResources();
                        final Drawable icon = res.getDrawable(R.drawable.smiley, null);
                        preference.setIcon(icon);
                        return preference;
                    }
                };
        final PreferenceScreen preferenceScreen =
                getPreferenceScreen(
                        new PreferenceFragmentTemplate(addPreferences2Screen),
                        activity);
        return new PreferenceScreenWithPreference(preferenceScreen, preferenceScreen.getPreference(0));
    }

    private static SearchablePreferencePOJO convertPreference2POJO(final Preference preference) {
        return SearchablePreference2POJOConverter.convert2POJO(asSearchablePreference(preference));
    }

    private static SearchablePreference asSearchablePreference(final Preference preference) {
        final SearchablePreference searchablePreference =
                new SearchablePreference(
                        preference.getContext(),
                        Optional.empty(),
                        Optional.empty());
        SearchablePreferenceTransformer.copyAttributes(preference, searchablePreference);
        return searchablePreference;
    }

    private static Preference convertPOJO2Preference(final SearchablePreferencePOJO pojo,
                                                     final PreferenceScreen preferenceScreen) {
        final PreferenceCategory parent = createPreferenceCategory(preferenceScreen);
        SearchablePreferenceFromPOJOConverter.addConvertedPOJO2Parent(pojo, parent, preferenceScreen.getContext());
        return parent.getPreference(0);
    }

    private static PreferenceCategory createPreferenceCategory(final PreferenceScreen preferenceScreen) {
        final PreferenceCategory parent = new PreferenceCategory(preferenceScreen.getContext());
        preferenceScreen.addPreference(parent);
        return parent;
    }

    private static boolean equals(final Drawable drawable1, final Drawable drawable2) {
        return drawable2Bitmap(drawable1).sameAs(drawable2Bitmap(drawable2));
    }
}
