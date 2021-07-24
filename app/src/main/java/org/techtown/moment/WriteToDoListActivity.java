package org.techtown.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class WriteToDoListActivity extends AppCompatActivity {

    private static final String TAG="WriteToDo";

    EditText nameEditText, contentsEditText,editTextTime;
    EditText editTextMax;
    Button saveBtn;

    Context context;
    OnRequestListener onRequestListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_do_list);
        context=this;

        if (context instanceof OnRequestListener) {
            onRequestListener = (OnRequestListener) context;
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        contentsEditText=findViewById(R.id.contentEditText);
        nameEditText=findViewById(R.id.nameEditText);
        editTextMax=findViewById(R.id.editTextMax);
        editTextTime=findViewById(R.id.editTextTime);

        saveBtn=findViewById(R.id.writeToDoBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveList();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void saveList(){
        String name=nameEditText.getText().toString();
        String contents=contentsEditText.getText().toString();
        String finishTime=editTextTime.getText().toString();

        String maxStr=editTextMax.getText().toString().trim();

        int max=Integer.parseInt(maxStr);


        String sql = "Insert into " + ToDoDatabase.TABLE_TO_DO_LIST +
                " ('FINISH_DATE', 'CONTENTS', 'SUBJECT', 'MAX') values(" +
                "'"+ finishTime + "', " +
                "'"+ contents + "', " +
                "'"+ name + "', " +
                "'"+ max + "');";


        ToDoDatabase database=ToDoDatabase.getInstance(context);
        if(database!=null){
            Log.d(TAG,"여기까진 성공");
        }
        database.exeSQL(sql);
        this.finish();
    }
}