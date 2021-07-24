package org.techtown.moment;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements OnItemClickListener {

    ArrayList<DiaryData> items = new ArrayList<DiaryData>();

    OnItemClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.diary_data_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryData item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(DiaryData item) {
        items.add(item);
    }

    public void setItems(ArrayList<DiaryData> items) {
        this.items = items;
    }

    public DiaryData getItem(int position) {
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
        ConstraintLayout layout;
        ImageView ImageView;
        TextView contentsTextView;
        TextView locationTextView;
        TextView dateTextView;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            layout= itemView.findViewById(R.id.diaryItemLayout);
            ImageView = itemView.findViewById(R.id.imagePreView);
            contentsTextView = itemView.findViewById(R.id.diaryTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateView);

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

        public void setItem(DiaryData item) {
            String picturePath = item.getPicture();
            String addressPath=item.getAddress();

            if (picturePath != null && !picturePath.equals("")) {
                ImageView.setVisibility(View.VISIBLE);
                ImageView.setImageURI(Uri.parse("file://" + picturePath));
            }

            contentsTextView.setText(item.getContents());

            if ( addressPath!= null && !addressPath.equals("")) {
                locationTextView.setText(item.getAddress());
            }

            dateTextView.setText(item.getCreateDateStr());

        }
        }
    }
