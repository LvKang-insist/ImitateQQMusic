package com.admin.core.util;

import com.google.gson.Gson;

public class BeanCopy {
    /**
     * 把modelA对象的属性值赋值给bClass对象的属性。
     *
     * @param modelA
     * @param bClass
     * @param <T>
     * @return
     */
    public static <A, T> T modelAconvertoB(A modelA, Class<T> bClass) {
        try {
            Gson gson = new Gson();
            String gsonA = gson.toJson(modelA);
            return gson.fromJson(gsonA, bClass);
        } catch (Exception e) {
            return null;
        }
    }
}
