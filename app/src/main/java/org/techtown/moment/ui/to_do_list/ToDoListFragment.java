package org.techtown.moment.ui.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.techtown.moment.R;
import org.techtown.moment.WriteToDoListActivity;

public class ToDoListFragment extends Fragment {
    FloatingActionButton writeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View toDoList = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        writeBtn=(FloatingActionButton) toDoList.findViewById(R.id.toDoListAddButton);
        writeBtn.setOnClickListener(this::onClick);

        return toDoList;
    }

    public void onClick(View v){
        Intent intent =new Intent(getActivity(), WriteToDoListActivity.class);
        startActivity(intent);
    }
}