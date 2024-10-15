package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static de.KnollFrank.lib.settingssearch.db.preference.converter.PreferenceScreenWithHostClass2POJOConverterTest.getPreferenceScreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Size;

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
        return drawableToBitmap(drawable1).sameAs(drawableToBitmap(drawable2));
    }

    // adapted from https://stackoverflow.com/a/10600736
    public static Bitmap drawableToBitmap(final Drawable drawable) {
        if (drawable instanceof final BitmapDrawable bitmapDrawable) {
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        final Size size = getIntrinsicSize(drawable).orElse(new Size(1, 1));
        final Bitmap bitmap = Bitmap.createBitmap(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static Optional<Size> getIntrinsicSize(final Drawable drawable) {
        return drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0 ?
                Optional.of(new Size(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())) :
                Optional.empty();
    }
}
