package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.Invite;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.InvitesRepository;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class InviteConfirmation extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView txtTitle;
    private TextView txtName;
    private TextView txtDescription;
    private TextView txtDate;
    private Button btnEdtInvite;
    private Button btnRecused;
    private InvitesRepository invitesRepository;
    private String userId;
    private Invite invate;



    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_confirmation);

        userId = getIntent().getExtras().getString("user");

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setComponents();
        setToolBar();
        //setBtnUpdateInvite();
        setBtnRecused();
        getEvent(userId);
    }

    private void setComponents() {
        txtName = findViewById(R.id.txt_user_name);
        txtName = findViewById(R.id.txt_invite_name);
        txtDescription = findViewById(R.id.txt_invite_desc);
        txtDate = findViewById(R.id.txt_invite_date);
        btnEdtInvite = findViewById(R.id.btn_accept);
        btnRecused = findViewById(R.id.btn_recused);

        txtName.setText("Tese");
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = findViewById(R.id.toolbar_title);
        txtTitle.setText("Convite");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /*private void setBtnUpdateInvite() {
        btnEdtInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InviteConfirmation.this,
                        InviteConfirmation.class);
                startActivity(intent);
            }
        });
    }
*/
    private void setBtnRecused(){

        btnRecused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invite invite = invitesRepository.getById("0A1FHjQ7DKIqf6tp12ou");

                invite.setConfirmation(true);
                invitesRepository.update(invite);
            }
        });
    }

    public void getEvent (String id){

    }
}