package org.techtown.moment.ui.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.moment.AppConstants;
import org.techtown.moment.DiaryData;
import org.techtown.moment.DataAdapter;
import org.techtown.moment.DiaryDatabase;
import org.techtown.moment.OnItemClickListener;
import org.techtown.moment.OneDayDiary;
import org.techtown.moment.R;

import java.util.ArrayList;
import java.util.Date;

public class SearchFragment extends Fragment {

    SearchView searchView;
    private static final String TAG = "SearchFragment";

    DataAdapter adapter;
    RecyclerView diarySearchListView;

    Context context;

    String searchText;

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

        View search = inflater.inflate(R.layout.fragment_search, container, false);

        ViewGroup diarySearchView =(ViewGroup) search;
        diarySearchListView = diarySearchView.findViewById(R.id.diarySearchView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        diarySearchListView.setLayoutManager(layoutManager);

        adapter = new DataAdapter();

        diarySearchListView.setAdapter(adapter);

        searchView=search.findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,query);
                searchText=query;
                loadDiaryListData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


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


        return search;
    }

    public void loadDiaryListData() {
        AppConstants.println("loadDiaryData called");

        String sql = "select * from " + DiaryDatabase.TABLE_DIARY + " where CONTENTS like ('%"+searchText+"%') order by CREATE_DATE desc";


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

                Log.d(TAG, contents);

                if (dateStr != null && dateStr.length() > 10) {
                    try {
                        Date inDate = AppConstants.dateFormat8.parse(dateStr);
                        createDateStr = AppConstants.dateFormat5.format(inDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }
                AppConstants.println("#" + i + " -> " + _id + ", " +
                        address + ", " + contents + ", " +
                        picture + ", " + createDateStr);

                Log.d(TAG, "address : " + address + ", contents : " + contents + ", picture : " + picture + ", createDate : " + createDateStr);

                items.add(new DiaryData(_id, address, contents, picture, createDateStr));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }

    }
}