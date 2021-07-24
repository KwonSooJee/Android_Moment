package org.techtown.moment.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.techtown.moment.AppConstants;
import org.techtown.moment.DataAdapter;
import org.techtown.moment.DiaryData;
import org.techtown.moment.DiaryDatabase;
import org.techtown.moment.OnItemClickListener;
import org.techtown.moment.OneDayDiary;
import org.techtown.moment.R;
import org.techtown.moment.WriteDiaryActivity;

import java.util.ArrayList;
import java.util.Date;

public class CalendarFragment extends Fragment{

    FloatingActionButton writeBtn;
    CalendarView calendarView;

    private static final String TAG = "DiaryDayFragment";

    DataAdapter adapter;
    RecyclerView diaryDayListView;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View cal = inflater.inflate(R.layout.fragment_calendar, container, false);

        writeBtn=(FloatingActionButton) cal.findViewById(R.id.calWriteBtn);
        writeBtn.setOnClickListener(this::onClick);

        CalendarFragment calendarFragment=this;

        context=getActivity();

        calendarView =cal.findViewById(R.id.calendarView);

        diaryDayListView=cal.findViewById(R.id.diaryDayView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                init();
                loadDiaryListData(year,month,dayOfMonth);
            }
        });

        return cal;
    }


    public void onClick(View v){
        Intent intent =new Intent(getActivity(), WriteDiaryActivity.class);
        startActivity(intent);
    }

    public void init(){
        diaryDayListView = diaryDayListView.findViewById(R.id.diaryDayView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diaryDayListView.setLayoutManager(layoutManager);

        adapter = new DataAdapter();

        diaryDayListView.setAdapter(adapter);
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


    public void loadDiaryListData(int year,int month,int dayOfMonth){

        Log.d("DiaryDayOfMonth","loadDiaryData called");

        String selectDateStr = (String.format("%4d%02d%02d", year, month+1, dayOfMonth));

        String sql_check = "select _id, ADDRESS, CONTENTS, PICTURE, CREATE_DATE, MODIFY_DATE from " + DiaryDatabase.TABLE_DIARY + " order by CREATE_DATE desc";

        int recordCount = -1;
        DiaryDatabase database = DiaryDatabase.getInstance(context);

        if (database != null) {
            Cursor outCursor = database.rawQuery(sql_check);
            ArrayList<DiaryData> items=new ArrayList<DiaryData>();
            recordCount = outCursor.getCount();

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
                        createDateStr = AppConstants.dateFormat9.format(inDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }

                if(selectDateStr.equals(createDateStr)){
                    items.add(new DiaryData(_id, address, contents, picture, createDateStr));
                }
            }
            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    }

}
