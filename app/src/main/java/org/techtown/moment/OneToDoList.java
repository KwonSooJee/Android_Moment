package org.techtown.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class OneToDoList extends AppCompatActivity {
    private static final String TAG="OneToDoList";

    TextView startView,finishView,contentsView,nameView;
    SeekBar seekBar;
    ToDoDatabase database;
    Button saveBtn, completeBtn;
    Context context;
    int max;
    int now;

    int _id;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.one_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sql ="delete from "+database.TABLE_TO_DO_LIST+ " where _id ='" + _id + "'";

        if(database!=null) {
            switch (item.getItemId()) {
                case R.id.deleteMenu:
                    database.exeSQL(sql);
                    finish();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_do_list);

        context = this;
        startView=findViewById(R.id.startDate);
        finishView=findViewById(R.id.finishDate);
        contentsView=findViewById(R.id.toDoTextView);
        nameView=findViewById(R.id.SubjectToDo);
        seekBar=findViewById(R.id.seekBar2);
        saveBtn=findViewById(R.id.ToDoButtonSave);
        completeBtn=findViewById(R.id.ToDoButtonComplete);

        _id=getIntent().getIntExtra("ToID",-1);

        if(_id==-1){
            Toast.makeText(context,"잘못된 접근입니다.",Toast.LENGTH_LONG).show();
            finish();
        }

        loadDate();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now=seekBar.getProgress();
                String sql ="update "+database.TABLE_TO_DO_LIST+ " set NOW =" + now + " where _id = '"+_id+"'";
                database.exeSQL(sql);
                finish();

                if(seekBar.getProgress()==max) {
                    sql = "delete from " + database.TABLE_TO_DO_LIST + " where _id ='" + _id + "'";
                    database.exeSQL(sql);
                    Toast.makeText(context, "완료, 잘했어요!", Toast.LENGTH_LONG).show();
                    finish();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    public void loadDate(){
        database=ToDoDatabase.getInstance(this);

        String sql = "select _id, FINISH_DATE, CONTENTS, SUBJECT, MAX, NOW, START_DATE from " + ToDoDatabase.TABLE_TO_DO_LIST + " order by START_DATE desc";

        Log.d(TAG, "sql : "+sql);

        Log.d(TAG,"_id : "+_id );
        if(database!=null) {
            Cursor outCursor = database.rawQuery(sql);


            do{
                outCursor.moveToNext();
            }while(_id!=outCursor.getInt(0));

            Log.d("OneDayToDo", "cursor " + outCursor);

            String finishDate=outCursor.getString(1);
            String contents = outCursor.getString(2);
            String subject = outCursor.getString(3);
            max = outCursor.getInt(4);
            now =outCursor.getInt(5);
            String createDate = outCursor.getString(6);
            String createDateStr = null;
            String finishDateStr=null;

            if (createDate != null && createDate.length() > 10) {
                try {
                    Date inDate = AppConstants.dateFormat8.parse(createDate);
                    createDateStr = AppConstants.dateFormat3.format(inDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                createDateStr = "";
            }

            if (finishDate != null && finishDate.length() > 7) {
                try {
                    Date inDate = AppConstants.dateFormat9.parse(finishDate);
                    finishDateStr = AppConstants.dateFormat3.format(inDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                finishDateStr = "";
            }

            Log.d(TAG,finishDate+", "+finishDateStr);

            startView.setText(createDateStr);
            nameView.setText(subject);
            contentsView.setText(contents);
            finishView.setText(finishDateStr);
            seekBar.setMax(max);
            seekBar.setProgress(now);


            outCursor.close();
        }

    }

}