package com.lechuang.lqsq.manage;

import android.widget.Toast;

import com.lechuang.lqsq.MyApplication;


/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/6/30
 * 时间：17-37
 */

public class ToastManager {
    private static volatile ToastManager instance;
    private Toast toast;

    private ToastManager() {
    }

    public static ToastManager getInstance() {
        if (instance == null) {
            synchronized (ToastManager.class) {
                if (instance == null) {
                    instance = new ToastManager();
                }
            }
        }
        return instance;
    }

    /**
     * 显示一个时间较长的toast信息
     *
     * @param id
     */
    public void showLongToast(int id) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), id, Toast.LENGTH_LONG);
        } else {
            toast.setText(id);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 显示一个时间较长的toast信息
     *
     * @param msg
     */
    public void showLongToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 显示一个时间较短的toast信息
     *
     * @param id
     */
    public void showShortToast(int id) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    /**
     * 显示一个时间较短的toast信息
     *
     * @param msg
     */
    public void showShortToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
