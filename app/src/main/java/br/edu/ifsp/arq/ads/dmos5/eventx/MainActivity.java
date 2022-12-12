package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.adapter.CustomAdapterEvent;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.util.Permission;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView txtTitle;
    private TextView txtLogin;
    UserViewModel userViewModel;

    private FirebaseFirestore db;
    ProgressDialog pd;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapterEvent adapter;

    List<Event> events = new ArrayList<>();
    User user;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        pd = new ProgressDialog(this);

        mRecyclerView = findViewById(R.id.recycler_view_events);

        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        setFloatActionButton();
        setToolBar();
        setDrawerLayout();
        loadUserLogged();
        setTextLogin();
        getEventList();
        setNavigationView();
    }

    private void setFloatActionButton() {

        FloatingActionButton floatButton = findViewById(R.id.btn_add_event);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            }
        });

    }

    private void getEventList() {

        db = FirebaseFirestore.getInstance();

        pd.setTitle("Carregando eventos...");
        pd.show();

        db.collection("event").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        events.clear();
                        pd.dismiss();

                        List<User> participants = new ArrayList<>();

                        for(DocumentSnapshot document : task.getResult()){

                            Event event = new Event(
                                    document.getString("id"),
                                    document.getString("name"),
                                    document.getString("description"),
                                    document.getString("startDate"),
                                    document.getString("endDate"),
                                    document.getString("situation"),
                                    document.getString("ownerId")
                            );

                            event.setParticipants(participants);

                            events.add(event);
                        }

                        adapter = new CustomAdapterEvent(MainActivity.this, events, user);
                        mRecyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setNavigationView() {
        navigationView = findViewById(R.id.nav_view);



        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()){
                            case R.id.nav_home:
                                intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_account:
                                intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_my_events:
                                intent = new Intent(MainActivity.this, InviteConfirmation.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_events:
                                intent = new Intent(MainActivity.this, AddEventActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_settings:
                                Toast.makeText(MainActivity.this, "Configuração", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_logout:
                                intent = new Intent(MainActivity.this, InviteConfirmation.class);
                                startActivity(intent);
                                userViewModel.logout();
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void setDrawerLayout() {
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.toggle_open,
                R.string.toggle_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = findViewById(R.id.toolbar_title);
        txtTitle.setText(getString(R.string.app_name));
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void setTextLogin() {
        txtLogin = navigationView.getHeaderView(0)
                .findViewById(R.id.header_profile_name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    if(user.getRole().equals("admin")){
                        FloatingActionButton floatButton = findViewById(R.id.btn_add_event);
                        floatButton.setVisibility(View.VISIBLE);
                    }
                    txtLogin.setText(user.getName()
                            + " " + user.getSurname());
                    String image = PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                            .getString(MediaStore.EXTRA_OUTPUT, null);

                }
            }
        });
        getEventList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEventList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadUserLogged() {
        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    startActivity(new Intent(MainActivity.this,
                            UserLoginActivity.class));
                    finish();
                }else{
                    MainActivity.this.user = user;
                    if(!user.getRole().equals("admin")){
                        NavigationMenuItemView createEvent = drawerLayout.findViewById(R.id.nav_events);
                        createEvent.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void deleteEvent(Event event){
        pd.setTitle("Deletando...");
        pd.show();

        db.collection("event").document(event.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(MainActivity.this, "Produto deletado!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(MainActivity.this, "Erro ao deletar produto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
