package com.xp.app.info;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/11/19 20:05
 */
public class AppUtils {
    static {
        String s = new String();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    @Nullable
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

//    /**
//     * 跳转到应用市场 指定app 界面
//     *
//     * @param pactName
//     * @return
//     */
//    public static boolean startActivityForPackName(String pactName, String appName) {
//        if (TextUtils.isEmpty(appName)) {
//            return startActivityForPackName(pactName);
//        }
//        try {
//            Uri uri = Uri.parse("market://details?id=" + appName);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            if (!TextUtils.isEmpty(pactName)) {
//                intent.setPackage(pactName);
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            AppManager.INSTANCE.getContext().startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            return startActivityForPackName(pactName);
//        }
//    }

//    /**
//     * 跳转到指定app界面
//     *
//     * @param name
//     * @return
//     */
//    public static boolean startActivityForPackName(@Nullable String name) {
//        try {
//            if (!TextUtils.isEmpty(name)) {
//                PackageManager packageManager = AppManager.INSTANCE.getContext().getPackageManager();
//                Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(name);
//                launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                AppManager.INSTANCE.getContext().startActivity(launchIntentForPackage);
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToKt.show("you have not installed this software");
//        }
//        return false;
//    }
//
//    /**
//     * 清单文件中的值
//     *
//     * @return
//     */
//    public static String getChannelName(Context context, String key) {
//        String channelName = "test";
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(),
//                    PackageManager.GET_META_DATA);
//
//            if (applicationInfo != null) {
//                channelName = applicationInfo.metaData.getString(key);
//                if (TextUtils.isEmpty(channelName)) {
//                    int umeng_channel1 = applicationInfo.metaData.getInt(key);
//                    if (umeng_channel1 != 0) {
//                        channelName = umeng_channel1 + "";
//                        return channelName;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return channelName;
//    }
//
//
//    @NotNull
//    public static String getModel() {
//        String model = Build.MODEL;
//        if (model != null) {
//            model = model.trim().replaceAll("\\s*", "");
//        } else {
//            model = "";
//        }
//        return model;
//    }

    /**
     * 获取应用签名
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppSign(Context context, String packageName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (Exception e) {

        }
        return "";
    }

    public static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前操作系统的sdk版本
     *
     * @return String 系统SDK版本
     */
    public static String getSdkCode() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + "";
    }

    /**
     * 获取android版本
     *
     * @return
     */
    @NotNull
    public static String getSystemVersion() {
        String release = Build.VERSION.RELEASE;
        return release;
    }


    /**
     * 应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        try {
            boolean installed = false;
            if (packageName == null) {
                return false;
            }
            List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
            for (ApplicationInfo in : installedApplications) {
                Log.d("12345", "isInstalled: " + in.packageName);
                if (TextUtils.equals(packageName, in.packageName)) {
                    installed = true;
                    break;
                }

            }

            if (!installed) {
                String sysModel = AppUtils.getPhoneFactory(context);
                if ("vivo".equalsIgnoreCase(sysModel) || "oppo".equalsIgnoreCase(sysModel)) {
                    installed = isInstalledViVo(context, packageName);
                }
            }

            return installed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取设备厂商
     *
     * @param context
     * @return
     */
    public static String getPhoneFactory(Context context) {
        try {
            return Build.MANUFACTURER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isInstalledViVo(Context context, String packageName) {
        try {
            if (packageName == null) {
                return false;
            }
            String installerPackageName = context.getPackageManager().getInstallerPackageName(packageName);
            if (!TextUtils.isEmpty(installerPackageName)) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    /**
//     * 添加参数
//     *
//     * @param build
//     * @param key
//     */
//    public static void addParams(MultipartBody.Builder build, String key, String value) {
//        build.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
//                RequestBody.create(null, value));
//    }

    /**
     * 获取App版本码
     *
     * @param context 上下文
     * @return App版本码
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 获取App版本码
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本码
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    /**
//     * 获取设备MAC地址
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
//     *
//     * @return MAC地址
//     */
//    public static String getMacAddress(Context context) {
//        String macAddress = getMacAddressByWifiInfo(context);
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        macAddress = getMacAddressByNetworkInterface();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        macAddress = getMacAddressByFile();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        return "please open wifi";
//    }
//
//    /**
//     * 获取设备MAC地址
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
//     *
//     * @return MAC地址
//     */
//    @SuppressLint("HardwareIds")
//    private static String getMacAddressByWifiInfo(Context context) {
//        try {
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            if (wifi != null) {
//                WifiInfo info = wifi.getConnectionInfo();
//                if (info != null) {
//                    return info.getMacAddress();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "02:00:00:00:00:00";
//    }
//
//    /**
//     * 获取设备MAC地址
//     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
//     *
//     * @return MAC地址
//     */
//    private static String getMacAddressByNetworkInterface() {
//        try {
//            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni : nis) {
//                if (!"wlan0".equalsIgnoreCase(ni.getName())) {
//                    continue;
//                }
//                byte[] macBytes = ni.getHardwareAddress();
//                if (macBytes != null && macBytes.length > 0) {
//                    StringBuilder res1 = new StringBuilder();
//                    for (byte b : macBytes) {
//                        res1.append(String.format("%02x:", b));
//                    }
//                    return res1.deleteCharAt(res1.length() - 1).toString();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "02:00:00:00:00:00";
//    }
//
//    /**
//     * 获取设备MAC地址
//     *
//     * @return MAC地址
//     */
//    private static String getMacAddressByFile() {
//        try {
//            ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
//            if (result.result == 0) {
//                String name = result.successMsg;
//                if (name != null) {
//                    result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
//                    if (result.result == 0) {
//                        if (result.successMsg != null) {
//                            return result.successMsg;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "02:00:00:00:00:00";
//    }
//
//    /**
//     * 获取手机IP地址
//     *
//     * @return String 手机IP地址
//     */
//    public static String getSysLocalIpAddress() {
//        String hostAddress = null;
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        hostAddress = inetAddress.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//            LogUtils.e("AppSysMgr-->>getSysLocalIpAddress", e);
//        }
//        LogUtils.i("AppSysMgr-->>getSysLocalIpAddress", hostAddress);
//        return hostAddress;
//    }
//
//    /**
//     * 获取当前手机连接的网络类型
//     *
//     * @param context 上下文
//     * @return int 网络类型
//     */
//    public static String getNetworkState(Context context) {
//        //获取ConnectivityManager对象
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        //获得当前网络信息
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        String name = "未知";
//        if (networkInfo != null && networkInfo.isAvailable()) {
//            //获取网络类型
//            int currentNetWork = networkInfo.getType();
//            //手机网络类型
//            if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
//                if (networkInfo.getExtraInfo() != null) {
//                    name = networkInfo.getExtraInfo();
////                    if (CMNET.equals(networkInfo.getExtraInfo())) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国移动CMNET网络");
////                    }
////                    if (CMWAP.equals(networkInfo.getExtraInfo())) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国移动CMWAP网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("uniwap")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国联通UNIWAP网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("3gwap")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国联通3GWAP网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("3gnet")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国联通3GNET网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("uninet")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国联通UNINET网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("ctwap")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国电信CTWAP网络");
////                    }
////                    if (networkInfo.getExtraInfo().equals("ctnet")) {
////                        LogUtils.i("AppNetworkMgr", "当前网络为中国电信CTNET网络");
////                    }
//                }
//                //WIFI网络类型
//            } else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
//                LogUtils.i("AppNetworkMgr", "当前网络为WIFI网络");
//                name = "WIFI";
//                return name;
//            }
//        }
//        return name;
//    }
//
//
//    /**
//     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
//     */
//    public static boolean isLocServiceEnable(Context context) {
//        try {
//            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            if (gps || network) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 手机自带的定位
//     */
//    public static void location(Context context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            boolean locationEnabled = locationManager.isLocationEnabled();
//            LogUtils.d("location-Android9.0 位置服务是否启用" + locationEnabled);
//        }
//        List<String> allProviders = locationManager.getAllProviders();
//        for (int i = 0; i < allProviders.size(); i++) {
//            LogUtils.d("location-都有哪些位置提供者：" + allProviders.get(i));
//        }
//        List<String> providers = locationManager.getProviders(true);
//        for (int i = 0; i < providers.size(); i++) {
//            LogUtils.d("location-可用的位置提供者：" + providers.get(i));
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                LogUtils.d("location-没有位置权限：");
//                continue;
//            }
//            Location location = locationManager.getLastKnownLocation(providers.get(i));
//            if (location == null) {
//                LogUtils.d("location-=null:");
//            } else {
//                LogUtils.d("location-accuracy-水平精度:" + location.getAccuracy());
//                LogUtils.d("location-altitude-高度:" + location.getAltitude());
//                LogUtils.d("location-bearing-方位角:" + location.getBearing());
//                LogUtils.d("location-latitude-维度:" + location.getLatitude());
//                LogUtils.d("location-longitude-经度:" + location.getLongitude());
//                LogUtils.d("location-provider-提供者:" + location.getProvider());
//                LogUtils.d("location-time-定位时间:" + new SimpleDateFormat().format(location.getTime()));
//            }
//
//            String s = providers.get(i);
//            locationManager.requestLocationUpdates(s, 1000 * 10, 1, new LocationListener() {
//                @Override
//                public void onLocationChanged(@NonNull Location location) {
//                    if (location == null) {
//                        LogUtils.d("location-" + s + "-=null:");
//                    } else {
//                        LogUtils.d("location-" + s + "-accuracy-水平精度:" + location.getAccuracy());
//                        LogUtils.d("location-" + s + "-altitude-高度:" + location.getAltitude());
//                        LogUtils.d("location-" + s + "-bearing-方位角:" + location.getBearing());
//                        LogUtils.d("location-" + s + "-latitude-维度:" + location.getLatitude());
//                        LogUtils.d("location-" + s + "-longitude-经度:" + location.getLongitude());
//                        LogUtils.d("location-" + s + "-provider-提供者:" + location.getProvider());
//                        LogUtils.d("location-" + s + "-time-定位时间:" + new SimpleDateFormat().format(location.getTime()));
//                    }
//
//                }
//            });
//        }
//
//
//    }
//
//    private boolean isTabletDevice(Context context) {
//        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
//                Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }
}
