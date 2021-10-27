package com.example.HelluApp.Metaverse;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HelluApp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class metaverse_noteAdapter extends RecyclerView.Adapter<metaverse_noteAdapter.ViewHolder> {
    ArrayList<metaverse_note> items = new ArrayList<metaverse_note>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.metaverse_item, viewGroup, false);

        Button button = itemView.findViewById(R.id.trainingClassEnterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(inflater.getContext(), metaverse_trainingroom.class);
                inflater.getContext().startActivity(intent);
            }
        });

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        metaverse_note item = items.get(position);
        holder.setItem(item);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(metaverse_note item) {
        items.add(item);
    }

    public void setItems(ArrayList<metaverse_note> items) {
        this.items = items;
    }

    public metaverse_note getItem(int position){
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView trainerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.trainingClassName);
            trainerName = itemView.findViewById(R.id.trainerTName);

        }

        public void setItem(metaverse_note item) {
            className.setText(item.getTrainingClass());
            trainerName.setText(item.getTrainerName());
        }


    }
}
