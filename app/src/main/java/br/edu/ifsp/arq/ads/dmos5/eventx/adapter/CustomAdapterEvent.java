package br.edu.ifsp.arq.ads.dmos5.eventx.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.MainActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;

import br.edu.ifsp.arq.ads.dmos5.eventx.R;

public class CustomAdapterEvent extends RecyclerView.Adapter<ViewHolderEvent> {

    MainActivity listEventActivity;
    List<Event> events;

    public CustomAdapterEvent(MainActivity listEventActivity, List<Event> events){
        this.listEventActivity = listEventActivity;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_layout, parent, false);

        ViewHolderEvent viewHolder = new ViewHolderEvent(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvent holder, int position) {

        holder.mEventName.setText(events.get(position).getName().toString());

        if(events.size() > 0){
            if(events.get(position).getSituation().equals("InProgress")){
                holder.mBtnStatus.setBackgroundColor(Color.parseColor("#157347"));
            } else if(events.get(position).getSituation().equals("Finished")){
                holder.mBtnStatus.setBackgroundColor(Color.parseColor("#BB2D3B"));
            } else if(events.get(position).getSituation().equals("Canceled")){
                holder.mBtnStatus.setBackgroundColor(Color.parseColor("#FFCA2C"));
            }
        }

        holder.mStartEvent.setText(events.get(position).getStartDate());
        holder.mEndEvent.setText(events.get(position).getEndDate());
        holder.mDescription.setText(events.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
