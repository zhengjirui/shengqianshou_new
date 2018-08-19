package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.lechuang.lqsq.R;
import com.lechuang.lqsq.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author yrj
 * @date 2017/10/9
 * @E-mail 1422947831@qq.com
 * @desc 版本更新的dialog
 */

public class VersionUpdateDialog extends Dialog {
    private TextView textView;
    //版本描述
    private String desc;
    private Context context;
    private ProgressDialog pBar;
    private SharedPreferences sp;
    private String url;

    public VersionUpdateDialog(Context context, String text) {
        super(context, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_update);
        this.context = context;
        this.desc = text;

        init();

        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }


    private void init() {
        textView = (TextView) findViewById(R.id.tv_desc);
        textView.setText(desc);
        //点击更新
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(url);//在下面的代码段
                } else {
                    Toast.makeText(context, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        findViewById(R.id.tv_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void downFile(final String downUrl) {
        pBar = new ProgressDialog(context);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");//http://app.qihaotuan.com/app/downLoad
        pBar.setMessage("请稍候...");
        pBar.setCancelable(false);//设置弹出框不可退出
        pBar.setProgress(0);
        pBar.show();
        new Thread() {
            public void run() {
                try {
                    String sdpath;
                    File sd = Environment.getExternalStorageDirectory();
                    boolean can_write = sd.canWrite();
                    Log.e("tag", "can_write:" + can_write);
                    // 判断SD卡是否存在，并且是否具有读写权限
                    sdpath = Environment.getExternalStorageDirectory() + "/lqsq/";
                    /*if (Environment.getExternalStorageState()
                            .equals(Environment.MEDIA_MOUNTED)) {
                        sdpath = Environment.getExternalStorageDirectory() + "/";
                    } else {
                        sdpath = Environment.getDataDirectory() + "/";
                    }*/

                    URL url = new URL(downUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5 * 1000);// 设置超时时间
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "GBK,utf-8;q=0.7,*;q=0.3");
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    pBar.setMax(length);  //设置进度条的总长度
                    FileOutputStream fos = null;
                    if (is != null) {
                        File file = new File(sdpath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File file1 = new File(file, context.getResources().getString(R.string.app_name) + ".apk");
                        if (!file1.exists()) {
                            file1.createNewFile();
                        }
                        fos = new FileOutputStream(file1);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            pBar.setProgress(process); //这里就是关键的实时更新进度了！
                        }
                    }
                    fos.flush();
                    if (fos != null) {
                        fos.close();
                    }
                    Thread.sleep(1000);
                    down();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void down() {
        pBar.cancel();
        update();
    }

    //安装文件，一般固定写法
    private void update() {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().commit();
        File file = new File(Environment.getExternalStorageDirectory() + "/lqsq/", context.getResources().getString(R.string.app_name) + ".apk");
        Utils.installApk(context, file);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
