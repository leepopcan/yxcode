package yxcode.com.cn.yxdecoder.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;


/**
 * Created by lihaifeng on 16/11/27.
 * Desc :
 */

public class DeviceUtils {

    private static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";

    public static String getDeviceId(Context context) {
        if(PreferencesUtils.containKey(context,KEY_DEVICE_ID)){
            return PreferencesUtils.getString(context,KEY_DEVICE_ID);
        }

        String deviceId = Constants.EMPTY;
        try {
            deviceId = getLocalMac(context).replace(":", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(deviceId)) {
            try {
                deviceId = getAndroidId(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(deviceId)) {
            UUID uuid = UUID.randomUUID();
            deviceId = uuid.toString().replace("-", "");
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getIMIEStatus(context);
        }


        if (!TextUtils.isEmpty(deviceId)) {
            PreferencesUtils.saveString(context,KEY_DEVICE_ID,deviceId);
        }

        return deviceId;
    }

    // IMEI码
    private static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    // Android Id
    private static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }
}
