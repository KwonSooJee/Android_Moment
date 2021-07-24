package org.techtown.moment;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConstants {

    public static String FOLDER_PHOTO="MomentDiaryPhoto";

    public static String DIARY_DATABASE_NAME="MomentDiaryData.db";
    public static String TO_DO_DATABASE_NAME="MomentToDoData.db";

    public static final int MODE_INSERT=111;
    public static final int MODE_MODIFY=112;

    public static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);
    public static SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
    public static SimpleDateFormat dateFormat3=new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    public static SimpleDateFormat dateFormat4=new SimpleDateFormat("MM-dd", Locale.KOREA);
    public static SimpleDateFormat dateFormat5=new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);
    public static SimpleDateFormat dateFormat6=new SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA);
    public static SimpleDateFormat dateFormat7=new SimpleDateFormat("yyyy년 MM월 dd일 HH:MM", Locale.KOREA);
    public static SimpleDateFormat dateFormat8=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    public static SimpleDateFormat dateFormat9=new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

    public static Handler handler= new Handler();


    public static final String TAG="AppConstants";
    public static void println(final String data){
        handler.post(new Runnable(){
            @Override
            public void run(){
                Log.d(TAG,data);
            }
        });
    }
}
