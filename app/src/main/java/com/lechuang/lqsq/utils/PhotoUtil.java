package com.lechuang.lqsq.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import com.lechuang.lqsq.net.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtil {
    /*
    * 倒计时
    * */
    public static Handler handler;
    public static Thread t;
    private static int time = Constant.TIME;//s

    public static boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static Bitmap toSmall(Bitmap bitmap, String path) {
        int scale = 1;
        int width_tmp = bitmap.getWidth();
        int height_tmp = bitmap.getHeight();

        while (true) {
            if (width_tmp / 2 < 100 || height_tmp / 2 < 100)
                break;

            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);

            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                        fileOutputStream);
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
        try {
            return BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {

        }

        return null;
    }

    //倒计时
    public static void getCode(final TextView tv) {
        handler = new Handler();
        time = Constant.TIME;
        t = new Thread() {
            @Override
            public void run() {
                super.run();
                time--;
                if (time <= 0) {
                    tv.setEnabled(true);
                    time = Constant.TIME;
                    tv.setText("重新获取");
                    return;
                }
                tv.setText(time + "s重新获取");
                if (handler != null)
                    handler.postDelayed(this, 1000);
            }
        };
    }

    //立即回收
    public static void closeCode() {
        if (handler != null) {
            handler = null;
            t = null;
            time = Constant.TIME;
        }
    }

    /**
     * @author li
     * 邮箱：961567115@qq.com
     * @time 2017/9/20  15:58
     * @describe 7.0，传送文件地址处理
     */
    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, Constant.package_fileProvider, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
