package yxcode.com.cn.yxdecoder.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHaifeng on 2016/5/26 0026.
 */
public class GsonUtils {

    public static <T> T castObj(Object obj, Class<T> clazz) {
        return GsonUtils.getObj(GsonUtils.toJson(obj), clazz);
    }

    public static <T> List<T> castList(Object obj, Type typeOfT) {
        return GsonUtils.getList(GsonUtils.toJson(obj), typeOfT);
    }


    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> List<T> getList(String jsonStr, Type type) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(jsonStr, type);
        return list;
    }

    public static <T> ArrayList<T> getArrayList(String jsonStr, Type typeOfT) {
        Gson gson = new Gson();

        ArrayList<T> list = gson.fromJson(jsonStr, typeOfT);
        return list;
    }

    public static <T> T getObj(String jsonStr, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, clazz);
    }

}
