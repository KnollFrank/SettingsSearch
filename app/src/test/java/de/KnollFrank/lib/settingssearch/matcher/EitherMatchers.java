package de.KnollFrank.lib.settingssearch.matcher;

import com.codepoetics.ambivalence.Either;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class EitherMatchers {

    private EitherMatchers() {
    }

    public static <L, R> Matcher<Either<L, R>> isLeft() {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(final Either<L, R> item) {
                return item.isLeft();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("an Either that isLeft");
            }

            @Override
            protected void describeMismatchSafely(final Either<L, R> item, final Description mismatchDescription) {
                mismatchDescription.appendText("was an Either that wasRight");
            }
        };
    }

    public static <L, R> Matcher<Either<L, R>> isRight() {
        return new TypeSafeMatcher<>() {

            @Override
            protected boolean matchesSafely(final Either<L, R> item) {
                return item.isRight();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("an Either that isRight");
            }

            @Override
            protected void describeMismatchSafely(final Either<L, R> item, final Description mismatchDescription) {
                mismatchDescription.appendText("was an Either that wasLeft");
            }
        };
    }
}
