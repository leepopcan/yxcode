package lm.com.gencode;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lm.com.gencode.utils.Constants;
import lm.com.gencode.utils.SerializeUtils;


/**
 * Created by lihaifeng on 16/6/20.
 */
public class PreferencesUtils {

    // default values
    public static final String DEFAULT_STRING = Constants.EMPTY;
    public static final int DEFAULT_INT = Integer.MIN_VALUE;
    public static final long DEFAULT_LONG = Long.MIN_VALUE;
    public static final float DEFAULT_FLOAT = Float.MIN_VALUE;
    public static final boolean DEFAULT_BOOLEAN = Boolean.FALSE;
    public static final Set<String> DEFAULT_STRING_SET = null;

    private static final String PREFS_PATH = "apps-prefs";
    private static final ExecutorService sPool = Executors.newSingleThreadExecutor();
    private static SharedPreferences sPrefs;

    public static void clear(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.clear();
        submit(editor);
    }

    private static SharedPreferences getPrefs(Context context) {
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences(PREFS_PATH, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void submit(final SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT > 9) {
            editor.apply();
        } else {
            sPool.execute(new Runnable() {
                @Override
                public void run() {
                    editor.commit();
                }
            });
        }
    }

    public static boolean containKey(Context context,String key){
        return getPrefs(context).contains(key);
    }

    public static void preLoadPrefs(Context context) {
        getPrefs(context);
    }

    public static void saveString(Context context,String key,String value){
        submit(getPrefs(context).edit().putString(key,value));
    }

    public static String getString(Context context,String key){
        return getPrefs(context).getString(key,DEFAULT_STRING);
    }

    public static void saveInt(Context context,String key,int value){
        submit(getPrefs(context).edit().putInt(key,value));
    }

    public static int getInt(Context context,String key){
        return getPrefs(context).getInt(key,DEFAULT_INT);
    }

    public static void saveLong(Context context,String key,long value){
        submit(getPrefs(context).edit().putLong(key,value));
    }

    public static long getLong(Context context,String key){
        return getPrefs(context).getLong(key,DEFAULT_LONG);
    }

    public static void saveFloat(Context context,String key,float value){
        submit(getPrefs(context).edit().putFloat(key,value));
    }

    public static float getFloat(Context context,String key){
        return getPrefs(context).getFloat(key,DEFAULT_FLOAT);
    }


    public static void saveBoolean(Context context,String key,boolean value){
        submit(getPrefs(context).edit().putBoolean(key,value));
    }

    public static boolean getBoolean(Context context,String key){
        return getPrefs(context).getBoolean(key,DEFAULT_BOOLEAN);
    }

    public static void saveStringSet(Context context,String key,Set<String> value){
        submit(getPrefs(context).edit().putStringSet(key,value));
    }

    public static Set<String> getStringSet(Context context,String key){
        return getPrefs(context).getStringSet(key,DEFAULT_STRING_SET);
    }

    public static void saveSerialize(Context context, String key, Serializable serializable) throws IOException{
        if(null == serializable){
            return;
        }
        String s = SerializeUtils.serialize(serializable);
        if(TextUtils.isEmpty(s)){
            return;
        }
        saveString(context,key,s);
    }

    public static Object getSerializable(Context context,String key) throws ClassNotFoundException,IOException{
        String str = getString(context,key);
        if(TextUtils.isEmpty(str)){
            return null;
        }
        return SerializeUtils.deSerialization(str);
    }


    public static boolean remove(Context context,String key){
        if(containKey(context,key)){
            submit(getPrefs(context).edit().remove(key));
            return true;
        }
        return false;
    }

}
