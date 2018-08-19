package com.lechuang.lqsq.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.net.Constant;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class Utils {
    //开启Debug
    public static boolean sDebug = true;
    public static final String sLogTag = "Utils";

    private static final String TAG = "Utils";

    // UTF-8 encoding
    private static final String ENCODING_UTF8 = "UTF-8";

    private static WeakReference<Calendar> calendar;


    /**
     * <p>
     * Get UTF8 bytes from a string
     * </p>
     *
     * @param string String
     * @return UTF8 byte array, or null if failed to get UTF8 byte array
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null)
            return new byte[0];

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * If system doesn't support UTF-8, use another way
			 */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     *
     * @param b byte array
     * @return string in utf-8 encoding, or empty if the byte array is not
     * encoded with UTF-8
     */
    public static String getUTF8String(byte[] b) {
        if (b == null)
            return "";
        return getUTF8String(b, 0, b.length);
    }

    public static String getPrice(double price) {
        return String.format("¥%.2f", price);
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     */
    public static String getUTF8String(byte[] b, int start, int length) {
        if (b == null) {
            return "";
        } else {
            try {
                return new String(b, start, length, ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    /**
     * <p>
     * Parse int value from string
     * </p>
     *
     * @param value string
     * @return int value
     */
    public static int getInt(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim(), 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getPrice1(double price) {
        return String.format("¥%.2f", price);
    }

    public static String getPrice2(double price) {
        return String.format("%.2f", price);
    }

    public static String getPrice3(String price) {
        return String.format("¥%.2f", Double.parseDouble(price));
    }

    public static String getPrice4(String price) {
        return String.format("%.2f", Double.parseDouble(price));
    }

    public static String getPrice5(String price) {
        return String.format("%.1f", Double.parseDouble(price));
    }

    /**
     * <p>
     * Parse float value from string
     * </p>
     *
     * @param value string
     * @return float value
     */
    public static float getFloat(String value) {
        if (value == null)
            return 0f;

        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * <p>
     * Parse long value from string
     * </p>
     *
     * @param value string
     * @return long value
     */
    public static long getLong(String value) {
        if (value == null)
            return 0L;

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static void V(String msg) {
        if (sDebug) {
            Log.v(sLogTag, msg);
        }
    }

    public static void V(String msg, Throwable e) {
        if (sDebug) {
            Log.v(sLogTag, msg, e);
        }
    }

    public static void D(String msg) {
        if (sDebug) {
            Log.d(sLogTag, msg);
        }
    }

    public static void D(String msg, Throwable e) {
        if (sDebug) {
            Log.d(sLogTag, msg, e);
        }
    }

    public static void I(String msg) {
        if (sDebug) {
            Log.i(sLogTag, msg);
        }
    }

    public static void I(String msg, Throwable e) {
        if (sDebug) {
            Log.i(sLogTag, msg, e);
        }
    }

    public static void W(String msg) {
        if (sDebug) {
            Log.w(sLogTag, msg);
        }
    }

    public static void W(String msg, Throwable e) {
        if (sDebug) {
            Log.w(sLogTag, msg, e);
        }
    }

    public static void E(String msg) {
        if (sDebug) {
            Log.e(sLogTag, msg);
        }
    }

    public static void E(String tag, String msg) {
        if (sDebug) {
            Log.e(tag, msg);
        }
    }

    public static void E(String msg, Throwable e) {
        if (sDebug) {
            Log.e(sLogTag, msg, e);
        }
    }

    public static void LogDialog(Context context, String string) {
        if (sDebug) {
            new AlertDialog.Builder(context)
                    .setTitle("API信息")
                    // 设置标题
                    .setMessage(string)
                    // 设置提示消息
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {// 设置确定的按键
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // do something
                                    dialog.cancel();
                                }
                            }).setCancelable(false)// 设置按返回键是否响应返回，这是是不响应
                    .show();// 显示
        }
    }

    public static String formatTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date);
    }

    /**
     * 格式化时间（Format：yyyy-MM-dd HH:mm）
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date(time * 1000));
    }

    public static String formatDate(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .format(new Date(time * 1000));
    }

    public static String formatDate(String time) {
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .format(new Date(Long.valueOf(time) * 1000));
    }

    public static String formatTime(String time) {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                .format(new Date(Long.valueOf(time) * 1000));
    }

    public static String formatTime2(String time) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date(Long.valueOf(time) * 1000));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(today.getTime());
    }

    public static StringBuffer getMonth(long TimeInMillis) {
        StringBuffer strBuff = new StringBuffer(getFormatTime(TimeInMillis)
                .split("-")[1]);
        return strBuff;
    }

    public static String getFormatTime(long TimeInMillis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date(TimeInMillis * 1000));
    }

    /**
     * @param datestr 日期字符串
     * @param day     相对天数，为正数表示之后，为负数表示之前
     * @return 指定日期字符串n天之前或者之后的日期
     */
    @SuppressLint("SimpleDateFormat")
    public static Date getBeforeAfterDate(String datestr, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date olddate = null;
        df.setLenient(false);
        try {
            olddate = new Date(df.parse(datestr).getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new Date(cal.getTimeInMillis());
    }

    /**
     * 返回网络是否是可用的
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(TAG, "couldn't get connectivity manager");
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0, length = info.length; i < length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the network is roaming
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // Log.w(Constants.TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            } else {
            }
        }
        return false;
    }

    /**
     * 文件拷贝工具类
     *
     * @param in  源文件
     * @param dst 目标文件
     * @throws IOException
     */
    public static void copyFile(InputStream in, FileOutputStream dst)
            throws IOException {
        byte[] buffer = new byte[8192];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            dst.write(buffer, 0, len);
        }
        in.close();
        dst.close();
    }


    /**
     * 界面切换动画
     *
     * @return
     */
    public static LayoutAnimationController getLayoutAnimation() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    /**
     * 比较两个文件的签名是否一致
     */
    public static boolean compareFileWithSignature(String path1, String path2) {

        long start = System.currentTimeMillis();
        if (TextUtils.isEmpty(path1) || TextUtils.isEmpty(path2)) {
            return false;
        }

        String signature1 = getFileSignatureMd5(path1);
        String signature2 = getFileSignatureMd5(path2);

        V("compareFileWithSignature total time is "
                + (System.currentTimeMillis() - start));
        if (!TextUtils.isEmpty(signature1) && signature1.equals(signature2)) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用签名MD5
     */
    public static String getFileSignatureMd5(String targetFile) {

        try {
            JarFile jarFile = new JarFile(targetFile);
            // 取RSA公钥
            JarEntry jarEntry = jarFile.getJarEntry("AndroidManifest.xml");

            if (jarEntry != null) {
                InputStream is = jarFile.getInputStream(jarEntry);
                byte[] buffer = new byte[8192];
                while (is.read(buffer) > 0) {
                    // do nothing
                }
                is.close();
                Certificate[] certs = jarEntry == null ? null : jarEntry
                        .getCertificates();
                if (certs != null && certs.length > 0) {
                    String rsaPublicKey = String.valueOf(certs[0]
                            .getPublicKey());
                    return getMD5(rsaPublicKey);
                }
            }
        } catch (IOException e) {
            W("occur IOException when get file signature", e);
        }
        return "";
    }

    /**
     * Get MD5 Code
     */
    public static String getMD5(String text) {
        try {
            byte[] byteArray = text.getBytes("utf8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(byteArray, 0, byteArray.length);
            return convertToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Convert byte array to Hex string
     */
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Check whether the SD card is readable
     */
    public static boolean isSdcardReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the SD card is writable
     */
    public static boolean isSdcardWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * 字符串中截取数字
     */
    public static String getNumbers(String start) {
        boolean isStart = false;
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < start.length(); i++) {
            String one = start.substring(i, i + 1);
            char ar = one.charAt(0);
            // 是数字
            if (ar >= '0' && ar <= '9') {
                str.append(ar);
                isStart = true;
            } else {
                if (isStart) {
                    if (i < start.length() - 1) {
                        str.append("-");
                    }
                }
                isStart = false;
            }
        }
        return str.toString();
    }

    /**
     * 倒计时计算时精度会丢失，造成最后2秒时都显示还剩1秒
     */
    private static long getCountDownTime(long time) {
        return time = time + (time * 2 / 1000);
    }

    final static long CountDownTime = getCountDownTime(60000);
    static long DownTime;

    /**
     * 检测之前的倒计时是否完成
     */
    public static void Detection(final Button button) {
        long difference = DownTime - System.currentTimeMillis();
        if (difference > 1000 && difference < CountDownTime) {
            new CountDownTimer(difference, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (button != null) {
                        button.setBackgroundColor(0X00000000);
                        button.setClickable(false);

                        button.setText(millisUntilFinished / 1000 + "s后重新发送");
                        button.setTextColor(0xff757575);
                    }
                }

                public void onFinish() {
                    if (button != null) {
                        // button.setBackgroundResource(R.drawable.bg_btn_code);
                        button.setTextColor(0xffeb6100);
                        button.setClickable(true);
                        button.setText("获取验证码");
                    }
                }
            }.start();
        }
    }

    public static void wait(final TextView button) {
        DownTime = System.currentTimeMillis() + CountDownTime;
        new CountDownTimer(CountDownTime, 1000) {
            public void onTick(long millisUntilFinished) {
                if (button != null) {
                    // button.setBackgroundColor(0X00000000);
                    button.setClickable(false);
                    button.setText(millisUntilFinished / 1000 + "s后重新获取");
                    // button.setTextColor(0xffffffff);
                }
            }

            public void onFinish() {
                if (button != null) {
                    // button.setBackgroundResource(R.drawable.bg_btn_code);
                    // button.setTextColor(0xffeb6100);
                    button.setClickable(true);
                    button.setText("获取验证码");
                }
            }
        }.start();
    }

    public static String encode(String a) {
        try {
            a = URLEncoder.encode(a, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * 判断在TextView是否为空，同样适用于EditText
     *
     * @param textView
     * @return false
     */
    public static boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().length() == 0;
    }

    // 同样适用于EditText
    public static String getTextFromEditText(TextView textView) {
        if (textView != null && !isEmpty(textView)) {
            return textView.getText().toString().trim();
        } else {
            return "";
        }
    }

    /**
     * 随机获取6位数的验证码
     *
     * @return (int)((Math.random() * 9 + 1) * 100000)
     */
    public static String randomVerifyCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 判断是不是有效合理的身份证号码
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isIdCard(String str) {
        Pattern pattern = Pattern
                .compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是汉字
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{0,}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是合理的手机号码
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isTelNumber(String str) {
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是数字
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是不是有效合理的银行账号
     *
     * @param str
     * @return
     */
    public static boolean isBankAccountNumber(String str) {
        Pattern pattern = Pattern
                .compile("(^\\d{16}$)|(^\\d{18}$)|(^\\d{19}$)");
        return pattern.matcher(str).matches();
    }

    public static boolean isRightPwd(String str) {
        Pattern pattern = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$).{8,15}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取用户配置
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return value
     */
    public static boolean getConfig(Context context, String key,
                                    boolean defaultValue) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean value = sp.getBoolean(key, defaultValue);
        return value;
    }

    /**
     * 保存用户配置
     *
     * @param context
     * @param key
     * @param value
     * @return editor.commit()
     */

    public static boolean saveConfig(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();// 提交
    }

    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 设置子类ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    // 获取manifest文件项目的versionName
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            E("versionName=====" + versionName);
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {

        }
        return versionName;
    }

    public static boolean isPassWord(String password) {
        Pattern p = Pattern.compile("^\\w{6,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);

    }

    /**
     * 把单个或多个view设置成visible
     *
     * @param view
     */
    public static void setVisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成invisible
     *
     * @param view
     */
    public static void setInvisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成gone
     *
     * @param view
     */
    public static void setGone(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.GONE);
        }
    }

    public static String secToTime0(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            // if (minute < 60) {
            // second = time % 60;
            // timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            // } else {
            hour = minute / 60;
            if (hour > 99)
                return "99:59:59";
            minute = minute % 60;
            second = time - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                    + unitFormat(second);
            // }
        }
        return timeStr;
    }

    public static String secToTime1(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            // if (minute < 60) {
            // second = time % 60;
            // timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            // } else {
            hour = minute / 60;
            if (hour > 99)
                return "99:59:59";
            minute = minute % 60;
            second = time - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            // }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 用于缩略图中，比如1.png会变成1_t.png， 这个仅限于非空的、且有效的图片地址，
     * 如果后台返回一个图片地址为http://121.41.54.130/mengshe，这个是要被忽略的
     *
     * @param picurl
     * @return
     */
    public static String dealPicurl(String picurl) {
        // if (!picurl.equals("") &&
        // !picurl.equals("http://121.41.54.130/mengshe")) {
        if (!picurl.equals("") && !picurl.equals("http://app.17mengshe.com")) {
            // 文件名
            String head = picurl.substring(0, picurl.lastIndexOf(".")) + "_t";
            // 后缀名
            String tail = picurl.substring(picurl.lastIndexOf("."), picurl.length());
            return head + tail;
        }
        return picurl;
    }

    /**
     * 时间戳转日期
     *
     * @param seconds
     * @return
     */
    public static String stampToDate(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * ListView/GridView使用ViewHolder一种极简写法，能够省去在每个Adapter中写一个ViewHolder
     *
     * @param convertView
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T getAdapterView(View convertView, int id) {
        SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            convertView.setTag(holder);
        }
        View childView = holder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            holder.put(id, childView);
        }
        return (T) childView;
    }

    //微信
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Uri getImageUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/喜乐淘");
        if (!file.exists()) {
            file.mkdir();
        }
        return Uri.fromFile(new File(file, "head.jpg"));
    }

    /**
     * 裁剪图片
     */
    public static void resizeImage(Uri uri, Activity activity, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    // 复制文本到剪贴板
    public static void copyText2Clipboard(Context context, String text) {
        if (text.trim().equals("")) {
            show(context, "复制的内容不能为空");
        } else {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(text.trim());
            show(context, "已复制到剪贴板");
        }
    }

    //显示提示文本
    public static void show(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    // 设置EditText的焦点，把光标放置在EditText的后面，让其看起来无违和感
    public static void setCursor2RightOfEditText(EditText et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        et.setSelection(et.getText().toString().trim().length());
    }

    // 设置EditText只能输入金额
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 打印所有的json数据（用于Android Studio平台），Android Studio和Eclipse有点不同，
     * Eclipse可以通过把json数据保存到File Explorer，然后导出到桌面；
     * 而Android Studio则需要通过分段打印的方式打印json数据
     *
     * @param msg
     */
    public static void showAllData(String msg) {
        if (msg == null || msg.length() == 0) {
            return;
        }

        int segmentSize = 3 * 1024;
        long length = msg.length();
        E("msg length=======" + length);
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            E("short msg========" + msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                E("long msg=======" + logContent);
            }
            E("long msg=======" + msg);// 打印剩余日志
        }
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    //系统自带的分享文字
    public static void systemShare(Context mContext, String txt) {
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT, txt);
        intent1.setType("text/plain");
        mContext.startActivity(Intent.createChooser(intent1, "分享到:"));
    }


    //判断是否安装了微信
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    //判断是否安装了QQ
    public static boolean isQQAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    //打开微信
    public static void openWeiXin(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    //打开QQ
    public static void openQQ(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.HomeActivity");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    //6.0以上访问sd卡权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    //安装apk
    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file.toString()),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    //调用系统分享分享图片
    public static void shareImgInSystem(Context context, String imgUrl) {
//        String path = Environment.getExternalStorageDirectory() + File.separator+;//sd根目录
        File file = new File(imgUrl);//这里share.jpg是sd卡根目录下的一个图片文件
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }


    //调用系统分享分享多张图片
    public static void shareManyImgInSystem(Context context, File... files) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<>();
        for (File pic : files) {
//            imageUris.add(Uri.fromFile(f));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUris.add(Uri.fromFile(pic));
            }else {
                //修复微信在7.0崩溃的问题
                Uri uri = null;
                try {
                    uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), pic.getName(), null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageUris.add(uri);
            }
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//        intent.putExtra("Kdescription", "wwwwwwwwwwwwwwwwwwww");
        context.startActivity(intent);
    }

    //删除一个文件夹下面所有的文件
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}
