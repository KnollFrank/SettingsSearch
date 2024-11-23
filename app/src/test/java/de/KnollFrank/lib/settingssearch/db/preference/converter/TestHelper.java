package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.codepoetics.ambivalence.Either;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.common.converter.DrawableAndStringConverter;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;

public class TestHelper {

    private static void assertEquals(final SearchablePreference actual, final SearchablePreferencePOJO expected) {
        assertThat(asIconResourceIdOrIconPixelData(Optional.ofNullable(actual.getIcon())), is(expected.iconResourceIdOrIconPixelData()));
        assertThat(actual.getLayoutResource(), is(expected.layoutResId()));
        assertThat(Optional.ofNullable(actual.getSummary()), is(expected.summary()));
        assertThat(Optional.ofNullable(actual.getTitle()), is(expected.title()));
        assertThat(actual.getWidgetLayoutResource(), is(expected.widgetLayoutResId()));
        assertThat(Optional.ofNullable(actual.getFragment()), is(expected.fragment()));
        assertThat(actual.isVisible(), is(expected.visible()));
        assertThat(actual.getSearchableInfo(), is(expected.searchableInfo()));
        assertThat(equalBundles(actual.getExtras(), expected.extras()), is(true));
        assertEquals(
                SearchablePreferenceCaster.cast(Preferences.getImmediateChildren(actual)),
                expected.children());
    }

    private static Optional<Either<Integer, String>> asIconResourceIdOrIconPixelData(final Optional<Drawable> icon) {
        return DrawableAndStringConverter
                .drawable2String(icon)
                .map(Either::ofRight);
    }

    private static void assertEquals(final List<SearchablePreference> actuals, final List<SearchablePreferencePOJO> expecteds) {
        assertThat(actuals.size(), is(expecteds.size()));
        for (int i = 0; i < actuals.size(); i++) {
            assertEquals(actuals.get(i), expecteds.get(i));
        }
    }

    // adapted from https://stackoverflow.com/a/13238729
    public static boolean equalBundles(final Bundle one, final Bundle two) {
        if (one.size() != two.size())
            return false;

        Set<String> setOne = new HashSet<>(one.keySet());
        setOne.addAll(two.keySet());
        Object valueOne;
        Object valueTwo;

        for (String key : setOne) {
            if (!one.containsKey(key) || !two.containsKey(key))
                return false;

            valueOne = one.get(key);
            valueTwo = two.get(key);
            if (valueOne instanceof Bundle && valueTwo instanceof Bundle &&
                    !equalBundles((Bundle) valueOne, (Bundle) valueTwo)) {
                return false;
            } else if (valueOne == null) {
                if (valueTwo != null)
                    return false;
            } else if (!valueOne.equals(valueTwo))
                return false;
        }

        return true;
    }
}