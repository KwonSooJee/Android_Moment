package org.techtown.moment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class ToDoAllFragment extends Fragment {

    private static final String TAG = "ToDoAllFragment";

    ToDoDataAdapter adapter;
    RecyclerView toDoAllListView;

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ToDoList = inflater.inflate(R.layout.fragment_to_do_all, container, false);

        init((ViewGroup) ToDoList);

        loadToDoListData();

        return ToDoList;
    }

    private void init(ViewGroup toDoAllView) {
        toDoAllListView = toDoAllView.findViewById(R.id.toDoAllListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        toDoAllListView.setLayoutManager(layoutManager);

        adapter = new ToDoDataAdapter();

        toDoAllListView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToDoData item = adapter.getItem(position);

                if (item != null) {
                    Log.d(TAG, "아이템 선택됨 : " + item.get_id());
                    Intent intent = new Intent(getActivity(), OneToDoList.class);
                    intent.putExtra("ToID", item.get_id());
                    startActivity(intent);
                    onResume();
                }

            }
        });

    }

    public void loadToDoListData() {
        AppConstants.println("loadToDoData called");

        String sql = "select _id, FINISH_DATE, CONTENTS, SUBJECT, MAX, NOW, START_DATE from " + ToDoDatabase.TABLE_TO_DO_LIST + " order by START_DATE desc";


        int recordCount = -1;
        ToDoDatabase database = ToDoDatabase.getInstance(context);

        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AppConstants.println("record count : " + recordCount + "\n");

            ArrayList<ToDoData> items = new ArrayList<ToDoData>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();


                int _id = outCursor.getInt(0);

                String finishDate=outCursor.getString(1);
                String contents = outCursor.getString(2);
                String subject = outCursor.getString(3);
                int max = outCursor.getInt(4);
                int now =outCursor.getInt(5);
                String startDate = outCursor.getString(6);
                String startDateStr = null;
                String finishDateStr=null;

                if (startDate != null && startDate.length() > 10) {
                    try {
                        Date inDate = AppConstants.dateFormat8.parse(startDate);
                        startDateStr = AppConstants.dateFormat3.format(inDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    startDateStr = "";
                }

                int finish = 0;
                if (finishDate != null && finishDate.length() > 7) {
                    try {
                        Date inDate = AppConstants.dateFormat9.parse(finishDate);
                        finishDateStr = AppConstants.dateFormat3.format(inDate);
                        String trimFinish=finishDate.trim();
                        finish=Integer.parseInt(trimFinish);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finishDateStr = "";
                }

                int dateNow=29991231;

                try {
                    long nowTime =System.currentTimeMillis();
                    Date nowDate=new Date(nowTime);
                    String nowDateStr=AppConstants.dateFormat9.format(nowDate).trim();
                    dateNow=Integer.parseInt(nowDateStr);


                }catch (Exception e){
                    e.printStackTrace();
                }

                Log.d(TAG, finish+","+dateNow);

                if(finish<=dateNow){
                    String sqlDelete ="delete from "+database.TABLE_TO_DO_LIST+ " where _id ='" + _id + "'";
                    database.exeSQL(sqlDelete);
                    Toast.makeText(context, subject+"이/가 목표일을 넘어 삭제됐습니다. 아쉽네요. ",Toast.LENGTH_LONG).show();
                }
                else {
                    AppConstants.println("#" + i + " -> " + _id + ", " +
                            subject + ", " + contents + ", " +
                            max + ", " + startDateStr + ", " + finishDateStr + ", " + max);

                    items.add(new ToDoData(_id, finishDateStr, contents, subject, max, now, startDateStr));
                }


            }
            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

    }
}