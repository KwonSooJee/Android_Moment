package org.techtown.moment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDatabase {
    private static final String TAG="DiaryDatabase";

    private static DiaryDatabase database;

    public static String TABLE_DIARY ="DIARY";
    public static int DATABASE_VERSION=1;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;


    private DiaryDatabase(Context context){
        this.context=context;
    }



    public static DiaryDatabase getInstance(Context context){
        if(database==null){
            database=new DiaryDatabase(context);
        }

        return database;
    }


    public boolean open(){
        println("opening database ["+AppConstants.DIARY_DATABASE_NAME+"]");
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();


        return true;
    }

    public void close(){
        println("closing database ["+AppConstants.DIARY_DATABASE_NAME+"]");
        db.close();

        database=null;
    }

    public Cursor rawQuery(String SQL){
        println("\nexecuteQuery called\n");

        Cursor cursor=null;

        try{
            open();
            cursor=db.rawQuery(SQL,null);
            println("cursor count :"+cursor.getCount());
            close();

        }catch (Exception e){
            Log.e(TAG, "Exception in executeQuery", e);
        }

        return cursor;
    }

    public boolean exeSQL(String SQL){
        println("\nexecuteQuery called\n");

        try{
            open();
            db.execSQL(SQL);
            close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }



    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, AppConstants.DIARY_DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String DROP_SQL="drop table if exists "+ TABLE_DIARY;


            try {
                db.execSQL(DROP_SQL);
            }catch (Exception e){
                Log.e(TAG, "Exception in DROP_SQL", e);
            }

            String CREATE_SQL = "create table " + TABLE_DIARY + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  ADDRESS TEXT DEFAULT '', "
                    + "  CONTENTS TEXT DEFAULT '', "
                    + "  PICTURE TEXT DEFAULT '', "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "  MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";

            try {
                db.execSQL(CREATE_SQL);
            }catch (Exception e){
                Log.e(TAG, "Exception in CREATE_SQL", e);
            }

            String CREATE_INDEX_SQL="create index "+ TABLE_DIARY +"_IDX on "
                    + TABLE_DIARY +"("+"CREATE_DATE"+")";

            try{
                db.execSQL(CREATE_INDEX_SQL);
            }catch (Exception e){
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", e);
            }

        }

        public void onOpen(SQLiteDatabase db){
            println("opened database ["+AppConstants.DIARY_DATABASE_NAME+"]");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version "+oldVersion+" to "+newVersion);

            String sql = "DROP TABLE if exists "+TABLE_DIARY;

            db.execSQL(sql);
            onCreate(db);
        }


    }

    private void println(String msg){
        Log.d(TAG,msg);
    }

}
