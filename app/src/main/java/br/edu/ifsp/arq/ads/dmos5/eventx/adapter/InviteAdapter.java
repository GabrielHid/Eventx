package br.edu.ifsp.arq.ads.dmos5.eventx.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.InvitesActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.R;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Invite;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.InvitesRepository;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.InviteViewModel;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.MyViewHolder>{

    private List<Invite> inviteList;
    private InvitesRepository inviteViewModel;
    private InvitesActivity teste;
    public InviteAdapter(List<Invite> inviteList){
        this.inviteList = inviteList;
    }
    @Override
    public InviteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_invite,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(InviteAdapter.MyViewHolder holder, final int position) {
        final Invite invite = inviteList.get(position);
        holder.eventDate.setText("Teste");
        //holder.eventDate.setText(event.getStartDate().toString());
    }
    @Override
    public int getItemCount() {
        return inviteList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView  eventName;
        private TextView  eventDate;
        private Event event;
        private CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.txt_invite_event);
            eventDate = itemView.findViewById(R.id.txt_invite_date);
            cardView = itemView.findViewById(R.id.carView);
        }
    }
}
