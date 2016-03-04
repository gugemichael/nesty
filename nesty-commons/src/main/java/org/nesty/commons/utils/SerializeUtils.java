package org.nesty.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * [Author] Michael
 * [Date] March 04, 2016
 */
public class SerializeUtils {

    private static final Gson gson = new GsonBuilder().create();

    public static byte[] format(Object stuff) {
        return gson.toJson(stuff).getBytes();
    }

}
