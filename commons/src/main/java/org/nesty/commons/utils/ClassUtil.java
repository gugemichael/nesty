package org.nesty.commons.utils;

import java.lang.reflect.Constructor;

/**
 * Author : Michael
 * Date : March 09, 2016
 */
public class ClassUtil {

    public static boolean hasDefaultConstructor(Class<?> clazz) {
       for (Constructor constructor : clazz.getDeclaredConstructors())
           if (!constructor.isVarArgs())
               return true;

        return false;
    }
}
