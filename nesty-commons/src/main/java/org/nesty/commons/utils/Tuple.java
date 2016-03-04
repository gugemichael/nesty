package org.nesty.commons.utils;

/**
 * [Author] Michael
 * [Date] March 04, 2016
 */
public class Tuple <K, V>{

    public K first;
    public V second;

    public Tuple(K k, V v) {
        this.first = k;
        this.second = v;
    }
}
