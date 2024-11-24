package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.common.converter.DrawableAndBitmapConverter.drawable2Bitmap;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceFragmentTestFactory.createSomePreferenceFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.test.core.app.ActivityScenario;

import com.codepoetics.ambivalence.Either;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.SearchableInfoAndDialogInfoProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.settingssearch.R;
import de.KnollFrank.settingssearch.test.TestActivity;

@RunWith(RobolectricTestRunner.class)
public class Preference2POJO2PreferenceConverterIntegrationTest {

    @Test
    public void test_iconOfPreference_survives_convertPreference2POJO() {
        try (final ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                final Drawable icon = activity.getResources().getDrawable(R.drawable.smiley, null);
                final Preference preference = createPreferenceWithIcon(activity, icon);

                // When
                final SearchablePreferencePOJO pojo =
                        convertPreference2POJO(
                                preference,
                                createSomePreferenceFragment(activity));

                // Then
                final Drawable pojoIcon = pojo.getIcon(activity).orElseThrow();
                assertThat(equals(pojoIcon, preference.getIcon()), is(true));
            });
        }
    }

    private static Preference createPreferenceWithIcon(final Context context, final Drawable icon) {
        final Preference preference = new Preference(context);
        preference.setIcon(icon);
        return preference;
    }

    private static SearchablePreferencePOJO convertPreference2POJO(final Preference preference,
                                                                   final PreferenceFragmentCompat hostOfPreference) {
        final Preference2SearchablePreferencePOJOConverter preference2SearchablePreferencePOJOConverter =
                new Preference2SearchablePreferencePOJOConverter(
                        (_preference, _hostOfPreference) ->
                                preference.equals(_preference) ?
                                        Optional.of(Either.ofRight(preference.getIcon())) :
                                        Optional.empty(),
                        new SearchableInfoAndDialogInfoProvider(
                                _preference -> Optional.empty(),
                                (_preference, _hostOfPreference) -> Optional.empty()),
                        new IdGenerator());
        return preference2SearchablePreferencePOJOConverter
                .convert2POJO(preference, hostOfPreference)
                .searchablePreferencePOJO();
    }

    private static boolean equals(final Drawable drawable1, final Drawable drawable2) {
        return drawable2Bitmap(drawable1).sameAs(drawable2Bitmap(drawable2));
    }
}
