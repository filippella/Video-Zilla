package org.dalol.videozilla.model;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 07:56.
 */
public class Parameter<V> {

    private final V value;

    public Parameter(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }
}
