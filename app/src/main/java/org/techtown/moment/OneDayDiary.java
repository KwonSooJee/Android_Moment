package org.techtown.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class OneDayDiary extends AppCompatActivity {

    TextView dateView,contentsView,addressView;
    ImageView imageView;
    DiaryDatabase database;


    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_diary);

        dateView=findViewById(R.id.onediaryDateView);
        contentsView=findViewById(R.id.oneDiaryTextView);
        addressView=findViewById(R.id.onediaryAddressView);
        imageView=findViewById(R.id.oneDiaryImageView);

        _id=getIntent().getIntExtra("ID",-1);

        if(_id==-1){
            Toast.makeText(this,"잘못된 접근입니다.",Toast.LENGTH_LONG).show();
            finish();
        }

        loadDate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.one_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sql ="delete from "+database.TABLE_DIARY+ " where _id ='" + _id + "'";

        if(database!=null) {
            switch (item.getItemId()) {
                case R.id.deleteMenu:
                    database.exeSQL(sql);
                    finish();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void loadDate(){
        database=DiaryDatabase.getInstance(this);

        String sql="select _id, ADDRESS, CONTENTS, PICTURE, CREATE_DATE, MODIFY_DATE from "+DiaryDatabase.TABLE_DIARY +" order by CREATE_DATE desc";

        if(database!=null) {
            Cursor outCursor = database.rawQuery(sql);

            do{
                outCursor.moveToNext();
            }while(_id!=outCursor.getInt(0));

            Log.d("OneDayDiary", "cursor " + outCursor);
            String address = outCursor.getString(1);
            String contents = outCursor.getString(2);
            String picture = outCursor.getString(3);
            String dateStr = outCursor.getString(4);
            String createDateStr = null;

            if (dateStr != null && dateStr.length() > 10) {
                try {
                    Date inDate = AppConstants.dateFormat8.parse(dateStr);
                    createDateStr = AppConstants.dateFormat2.format(inDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                createDateStr = "";
            }

            dateView.setText(createDateStr);

            if (address != null && address != "") {
                addressView.setVisibility(View.VISIBLE);
                addressView.setText(address);
            }

            contentsView.setText(contents);

            Bitmap bitmap = loadPicture(picture);

            if (bitmap != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }
            outCursor.close();
        }

    }


    public Bitmap loadPicture(String picturePath){


        Bitmap bitmap=null;

        try{
            bitmap= BitmapFactory.decodeFile(picturePath);
        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }
}