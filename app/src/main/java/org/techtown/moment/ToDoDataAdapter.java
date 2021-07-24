package org.techtown.moment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoDataAdapter extends RecyclerView.Adapter<ToDoDataAdapter.ViewHolder> implements OnItemClickListener {

    ArrayList<ToDoData> items = new ArrayList<ToDoData>();

    OnItemClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.todo_data_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoData item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ToDoData item) {
        items.add(item);
    }

    public void setItems(ArrayList<ToDoData> items) {
        this.items = items;
    }

    public ToDoData getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onItemClick(View view, int position) {
        if (listener != null) {
            listener.onItemClick(view, position);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView nameTextView;
        TextView contentsTextView;
        SeekBar seekBar;
        TextView dateTextView;
        ToDoDatabase database;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            layout= itemView.findViewById(R.id.ToDoItemLayout);
            contentsTextView = itemView.findViewById(R.id.textView);
            nameTextView = itemView.findViewById(R.id.ToDoNameTextView);
            dateTextView = itemView.findViewById(R.id.finishView);
            seekBar=itemView.findViewById(R.id.seekBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(listener!=null){
                            listener.onItemClick(itemView,pos);
                        }
                    }
                }
            });
        }

        public void setItem(ToDoData item) {

            nameTextView.setText(item.getSubject());
            contentsTextView.setText(item.getContents());
            dateTextView.setText(item.getFinishDate());
            int max=item.getMax();
            int now=item.getNow();
            seekBar.setMax(max);
            seekBar.setProgress(now);

        }
        }
    }
