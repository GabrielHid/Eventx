package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.adapter.InviteAdapter;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Invite;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.InvitesRepository;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.InviteViewModel;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class InvitesActivity extends AppCompatActivity {
    InviteViewModel inviteViewModel;
    UserViewModel userViewModel;
    private InvitesRepository invitesRepository;
    private User user;
    private RecyclerView recyclerView;
    private InviteAdapter adapter;
    private List<Invite> inviteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        inviteList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerList);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getInvites() {
        inviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);

        inviteViewModel.getByUser(user.getId()).observe(this, new Observer<List<Invite>>() {
            @Override
            public void onChanged(List<Invite> activities) {

                inviteList = activities;
                adapter = new InviteAdapter(inviteList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InvitesActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    InvitesActivity.this.user = user;

                    getInvites();
                }
            }
        });
    }

}