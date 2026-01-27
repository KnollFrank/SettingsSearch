package de.KnollFrank.lib.settingssearch.db.preference.dao;

import static org.hamcrest.Matchers.is;

import android.os.PersistableBundle;

import com.codepoetics.ambivalence.Either;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeCreator;
import de.KnollFrank.lib.settingssearch.db.preference.db.transformer.SearchablePreferenceScreenTreeTransformer;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.TreeProcessorDescriptionEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.BundleMatchers;

class TreeProcessorDescriptionEntityMatchers {

    public static Matcher<TreeProcessorDescriptionEntity> hasSameContentAs(final TreeProcessorDescriptionEntity expected) {
        return Matchers.allOf(
                hasTreeProcessor(is(expected.treeProcessor())),
                hasParams(BundleMatchers.isEqualTo(expected.params())));
    }

    private static Matcher<TreeProcessorDescriptionEntity> hasTreeProcessor(final Matcher<? super Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>> treeProcessorMatcher) {
        return new FeatureMatcher<TreeProcessorDescriptionEntity, Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>>>(
                treeProcessorMatcher,
                "treeProcessor with",
                "treeProcessor"
        ) {

            @Override
            protected Either<Class<? extends SearchablePreferenceScreenTreeCreator<?>>, Class<? extends SearchablePreferenceScreenTreeTransformer<?>>> featureValueOf(final TreeProcessorDescriptionEntity actual) {
                return actual.treeProcessor();
            }
        };
    }

    private static Matcher<TreeProcessorDescriptionEntity> hasParams(final Matcher<PersistableBundle> paramsMatcher) {
        return new FeatureMatcher<>(
                paramsMatcher,
                "params with",
                "params"
        ) {

            @Override
            protected PersistableBundle featureValueOf(final TreeProcessorDescriptionEntity actual) {
                return actual.params();
            }
        };
    }
}
