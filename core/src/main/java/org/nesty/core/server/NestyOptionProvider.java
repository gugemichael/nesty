package org.nesty.core.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A vector of options of Nesty
 *
 * Author Michael on 04/13/2016.
 */
public class NestyOptionProvider {

    private Map<NestyOptions<?>, Object> options = new ConcurrentHashMap<>();

    public <T> NestyOptionProvider option(NestyOptions<T> option, T value) {
        this.options.put(option, value);
        return this;
    }

    /**
     * return specified option name's value if this option is setted. or named option
     * default value.
     *
     * @param option named option already defined
     * @return setted value or default value
     */
    @SuppressWarnings("unchecked")
    public <T> T option(NestyOptions<T> option) {
        T value;
        if ((value = (T) options.get(option)) != null)
            return value;
        else
            return option.defaultValue();
    }
}
