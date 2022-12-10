package br.edu.ifsp.arq.ads.dmos5.eventx.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.edu.ifsp.arq.ads.dmos5.eventx.R;

public class ViewHolderEvent extends RecyclerView.ViewHolder{

    TextView mEventName, mStartEvent, mEndEvent, mDescription;
    Button mBtnStatus, mBtnParticipate;
    View mView;

    public ViewHolderEvent(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        mEventName = mView.findViewById(R.id.txt_layout_event_name);
        mStartEvent = mView.findViewById(R.id.txt_layout_start_event);
        mEndEvent = mView.findViewById(R.id.txt_layout_end_event);
        mDescription = mView.findViewById(R.id.txt_layout_description);

        mBtnStatus = mView.findViewById(R.id.btn_status);
        mBtnParticipate = mView.findViewById(R.id.btn_participar);
    }

}