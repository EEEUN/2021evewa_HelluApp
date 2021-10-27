package com.example.HelluApp.DailyStamp;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.HelluApp.DailyStamp.OnNoteItemClickListener;
import com.example.HelluApp.DailyStamp.daily_stamp_write_note;
import com.example.HelluApp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
//이 파일은 지금 사용되지 않습니다.
/*
public class daily_stamp_write_noteAdapter extends RecyclerView.Adapter<daily_stamp_write_noteAdapter.ViewHolder> implements OnNoteItemClickListener{
    List<daily_stamp_write_note> items;
    Context context;
    int itemLayout;
    OnNoteItemClickListener listener;

    public daily_stamp_write_noteAdapter(List items) {
        this.items=items;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.daily_stamp_write_item, viewGroup, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        daily_stamp_write_note item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(daily_stamp_write_note item){
        items.add(item);
    }

    public  void setItems(ArrayList<daily_stamp_write_note> items){
        this.items = items;
    }

    public daily_stamp_write_note getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnNoteItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView daily_image;
        //ImageView pictureImageView;
        TextView daily_date;
        TextView daily_text;
        public ViewHolder(View itemView, final OnNoteItemClickListener listener) {
            super(itemView);
            daily_image = itemView.findViewById(R.id.daily_write_image);
            daily_date = itemView.findViewById(R.id.daily_write_date);
            daily_text = itemView.findViewById(R.id.daily_write_text);

            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }
        public void setItem(daily_stamp_write_note item){
            String picturePath = item.getPicture();
            if(picturePath != null && !picturePath.equals("")){
                daily_image.setVisibility(View.VISIBLE);
                daily_image.setImageURI(Uri.parse("file://"+picturePath));
            } else {
                daily_image.setVisibility(View.VISIBLE);
                daily_image.setImageResource(R.drawable.no_image);
            }

            daily_text.setText(item.getContents());

            daily_date.setText(item.getCreateDateStr());
        }
    }

}
 */
