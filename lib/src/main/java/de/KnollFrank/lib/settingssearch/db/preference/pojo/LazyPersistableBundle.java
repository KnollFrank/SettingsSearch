package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import android.os.PersistableBundle;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.PersistableBundleConverter;

public class LazyPersistableBundle {

    private final String xmlString;
    private Optional<PersistableBundle> bundleCache = Optional.empty();

    public LazyPersistableBundle(final String xmlString) {
        this.xmlString = xmlString;
    }

    public PersistableBundle get() {
        if (bundleCache.isEmpty()) {
            bundleCache = Optional.of(_get());
        }
        return bundleCache.orElseThrow();
    }

    public String getXmlString() {
        return xmlString;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final LazyPersistableBundle that = (LazyPersistableBundle) o;
        return Objects.equals(this.xmlString, that.xmlString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xmlString);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LazyPersistableBundle.class.getSimpleName() + "[", "]")
                .add("xmlString='" + xmlString + "'")
                .toString();
    }

    private PersistableBundle _get() {
        return new PersistableBundleConverter().convertBackward(xmlString);
    }
}
