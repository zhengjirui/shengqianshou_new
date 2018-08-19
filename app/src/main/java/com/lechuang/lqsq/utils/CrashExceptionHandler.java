package com.lechuang.lqsq.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YST on 2016/6/8.
 */
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = CrashExceptionHandler.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
    private Context mApplicationContext;
    private File mCrashInfoFolder;
    private CrashExceptionRemoteReport mCrashExceptionRemoteReport;

    public CrashExceptionHandler(Context context, File crashInfoFolder) {
        this.mApplicationContext = context.getApplicationContext();
        this.mCrashInfoFolder = crashInfoFolder;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        this.handleException(ex);

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }
    }

    public void configRemoteReport(CrashExceptionRemoteReport crashExceptionRemoteReport) {
        this.mCrashExceptionRemoteReport = crashExceptionRemoteReport;
    }

    private void handleException(Throwable ex) {
        /*if (ex != null && BuildConfig.DEBUG) {
            saveCrashInfoToFile(ex);
        }
        Intent intent = new Intent(mApplicationContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(mApplicationContext.getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //退出程序
        AlarmManager mgr = (AlarmManager) mApplicationContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());*/


    }

    private void saveCrashInfoToFile(Throwable ex) {
        Writer writer = null;
        PrintWriter printWriter = null;
        FileOutputStream fos = null;
        try {
            if (this.mCrashInfoFolder == null) {
                return;
            }

            if (!this.mCrashInfoFolder.exists()) {
                this.mCrashInfoFolder.mkdirs();
            }

            if (this.mCrashInfoFolder.exists()) {
                String e = DATE_FORMAT.format(new Date());
                String crashLogFileName = e + ".log";
                StringBuffer sb = new StringBuffer();
                File crashLogFile = new File(this.mCrashInfoFolder, crashLogFileName);
                sb.append("------------Crash Environment Info------------\n");
                sb.append("------------Manufacture: " + SystemUtils.getDeviceManufacture() + "------------" + "\n");
                sb.append("------------DeviceName: " + SystemUtils.getDeviceName() + "------------" + "\n");
                sb.append("------------SystemVersion: " + SystemUtils.getSystemVersion() + "------------" + "\n");
                sb.append("------------DeviceIMEI: " + SystemUtils.getIMEI(this.mApplicationContext) + "------------" + "\n");
                sb.append("------------AppVersion: " + SystemUtils.getVersionName(this.mApplicationContext) + "------------" + "\n");
                sb.append("------------CrashTime:" + DATE_FORMAT.format(new Date(System.currentTimeMillis())) + "\n\n");
                sb.append("------------Crash Environment Info------------\n");
                sb.append("\n");
                writer = new StringWriter();
                printWriter = new PrintWriter(writer);
                ex.printStackTrace(printWriter);
                Throwable cause = ex.getCause();
                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = cause.getCause();
                }
                printWriter.close();
                sb.append(writer.toString());
                fos = new FileOutputStream(crashLogFile);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } else {
                Log.e(TAG, "crash info folder create failure!!!");
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void sendCrashInfoToServer(Throwable ex) {
        if (this.mCrashExceptionRemoteReport != null) {
            this.mCrashExceptionRemoteReport.onCrash(ex);
        }

    }

    public interface CrashExceptionRemoteReport {
        void onCrash(Throwable var1);
    }
}
