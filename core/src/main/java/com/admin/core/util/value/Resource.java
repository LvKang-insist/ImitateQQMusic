package com.admin.core.util.value;

import com.admin.core.app.Latte;

/**
 * @author Lv
 * Created at 2019/7/9
 */
public class Resource {
    public static String getString(int id){
        return Latte.getApplication().getResources().getString(id);
    }
    public static Integer getInteger(int id){
        return Latte.getApplication().getResources().getInteger(id);
    }
}
