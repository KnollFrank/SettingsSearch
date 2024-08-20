package de.KnollFrank.lib.settingssearch.fragment.factory;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.Optional;

class Arguments {

    private final String fragmentClassName;
    private final Optional<String> keyOfPreference;
    private final Optional<PreferenceFragmentCompat> hostOfPreference;

    public Arguments(final String fragmentClassName,
                     final Optional<String> keyOfPreference,
                     final Optional<PreferenceFragmentCompat> hostOfPreference) {
        this.fragmentClassName = fragmentClassName;
        this.keyOfPreference = keyOfPreference;
        this.hostOfPreference = hostOfPreference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Arguments arguments = (Arguments) o;
        return Objects.equals(fragmentClassName, arguments.fragmentClassName) && Objects.equals(keyOfPreference, arguments.keyOfPreference) && Objects.equals(hostOfPreference, arguments.hostOfPreference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fragmentClassName, keyOfPreference, hostOfPreference);
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "fragmentClassName='" + fragmentClassName + '\'' +
                ", keyOfPreference=" + keyOfPreference +
                ", hostOfPreference=" + hostOfPreference +
                '}';
    }
}
