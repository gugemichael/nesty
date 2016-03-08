package org.nesty.commons.utils;

public class Tuple<K, V> {

    public K first;
    public V second;

    public Tuple(K k) {
        this.first = k;
        this.second = null;
    }

    public Tuple(K k, V v) {
        this.first = k;
        this.second = v;
    }
}
