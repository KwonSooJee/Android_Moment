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

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class DiaryAllFragment extends Fragment {

    private static final String TAG = "DiaryAllFragment";

    DataAdapter adapter;
    RecyclerView diaryAllListView;

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

        View diaryAll = inflater.inflate(R.layout.fragment_diary_all, container, false);

        init((ViewGroup) diaryAll);

        loadDiaryListData();

        return diaryAll;
    }

    private void init(ViewGroup diaryAllView) {
        diaryAllListView = diaryAllView.findViewById(R.id.diaryAllListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diaryAllListView.setLayoutManager(layoutManager);

        adapter = new DataAdapter();

        diaryAllListView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DiaryData item = adapter.getItem(position);

                if (item != null) {
                    Log.d(TAG, "아이템 선택됨 : " + item.get_id());
                    Intent intent = new Intent(getActivity(), OneDayDiary.class);
                    intent.putExtra("ID", item.get_id());
                    startActivity(intent);
                    onResume();
                }

            }
        });



    }

    public void loadDiaryListData() {
        AppConstants.println("loadDiaryData called");

        String sql = "select _id, ADDRESS, CONTENTS, PICTURE, CREATE_DATE, MODIFY_DATE from " + DiaryDatabase.TABLE_DIARY + " order by CREATE_DATE desc";


        int recordCount = -1;
        DiaryDatabase database = DiaryDatabase.getInstance(context);

        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AppConstants.println("record count : " + recordCount + "\n");

            ArrayList<DiaryData> items = new ArrayList<DiaryData>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();


                int _id = outCursor.getInt(0);

                String address = outCursor.getString(1);
                String contents = outCursor.getString(2);
                String picture = outCursor.getString(3);
                String dateStr = outCursor.getString(4);
                String createDateStr = null;

                if (dateStr != null && dateStr.length() > 10) {
                    try {
                        Date inDate = AppConstants.dateFormat8.parse(dateStr);
                        String temp= String.valueOf(inDate);
                        createDateStr = AppConstants.dateFormat5.format(inDate);
                        Log.d(TAG,temp+","+createDateStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }

                AppConstants.println("#" + i + " -> " + _id + ", " +
                        address + ", " + contents + ", " +
                        picture + ", " + createDateStr);

                items.add(new DiaryData(_id, address, contents, picture, createDateStr));
            }
            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

    }

}