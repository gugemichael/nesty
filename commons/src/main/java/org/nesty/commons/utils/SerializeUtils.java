package org.nesty.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.nesty.commons.exception.SerialFormatException;

/**
 * [Author] Michael
 * [Date] March 04, 2016
 */
public class SerializeUtils {

    private static final Gson gson = new GsonBuilder().create();

    public static byte[] encode(Object stuff) {
        return gson.toJson(stuff).getBytes();
    }

    public static Object decode(String stuff, Class<?> clazz) throws SerialFormatException {
        try {
            return gson.fromJson(stuff, clazz);
        } catch (JsonSyntaxException e) {
            throw new SerialFormatException(String.format("%s decode exception", stuff));
        }
    }
}
