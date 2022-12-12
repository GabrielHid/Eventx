package br.edu.ifsp.arq.ads.dmos5.eventx.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.AddEventActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.MainActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;

import br.edu.ifsp.arq.ads.dmos5.eventx.R;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;

public class CustomAdapterEvent extends RecyclerView.Adapter<ViewHolderEvent> {

    MainActivity listEventActivity;
    List<Event> events;
    User user;

    public CustomAdapterEvent(MainActivity listEventActivity, List<Event> events, User user){
        this.listEventActivity = listEventActivity;
        this.events = events;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolderEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_layout, parent, false);

        ViewHolderEvent viewHolder = new ViewHolderEvent(itemView);

        viewHolder.setOnClickListener(new ViewHolderEvent.ClickListener() {

            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(listEventActivity);

                    String[] options = {"Editar", "Remover"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (i == 0) {

                                listEventActivity.finish();

                                String id = events.get(position).getId();
                                String name = events.get(position).getName();
                                String description = events.get(position).getDescription();
                                String startDate = events.get(position).getStartDate();
                                String endDate = events.get(position).getEndDate();
                                String situation = events.get(position).getSituation();
                                String ownerId = events.get(position).getOwnerId();

                                Intent intent = new Intent(listEventActivity, AddEventActivity.class);

                                intent.putExtra("eId", id);
                                intent.putExtra("eName", name);
                                intent.putExtra("eDescription", description);
                                intent.putExtra("eStartDate", startDate);
                                intent.putExtra("eEndDate", endDate);
                                intent.putExtra("eSituation", situation);
                                intent.putExtra("eOwnerId", ownerId);

                                listEventActivity.startActivity(intent);
                            }

                            if (i == 1) {

                                listEventActivity.deleteEvent(events.get(position));
                            }
                        }
                    }).create().show();
                }
        });

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
            } else if(events.get(position).getSituation().equals("Active")){
                holder.mBtnStatus.setBackgroundColor(Color.parseColor("#027fe9"));
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
