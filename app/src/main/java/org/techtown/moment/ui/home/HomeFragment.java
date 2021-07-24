package org.techtown.moment.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.techtown.moment.R;
import org.techtown.moment.WriteDiaryActivity;

public class HomeFragment extends Fragment {

    FloatingActionButton writeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View home = inflater.inflate(R.layout.fragment_home, container, false);

        writeBtn=(FloatingActionButton) home.findViewById(R.id.homeWriteBtn);
        writeBtn.setOnClickListener(this::onClick);
        return home;
    }

    public void onClick(View v){
                Intent intent =new Intent(getActivity(),WriteDiaryActivity.class);
                startActivity(intent);
    }

}