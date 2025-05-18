package de.KnollFrank.lib.settingssearch.common.graph;

import org.jgrapht.graph.DefaultEdge;

import java.util.Objects;

class StringEdge extends DefaultEdge {

    private final String label;

    public StringEdge(final String label) {
        this.label = Objects.requireNonNull(label, "Edge label cannot be null");
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StringEdge that = (StringEdge) o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return label;
    }
}