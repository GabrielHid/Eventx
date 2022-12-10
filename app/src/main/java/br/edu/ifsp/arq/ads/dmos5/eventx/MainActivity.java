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
    User userLogged = new User();

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
        setNavigationView();
        setTextLogin();
        getEventList();
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
                                    document.getString("ownerId"),
                                    participants
                            );

                            events.add(event);
                        }

                        adapter = new CustomAdapterEvent(MainActivity.this, events);
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
                                Toast.makeText(MainActivity.this, "Minha conta", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_my_events:
                                Toast.makeText(MainActivity.this, "Meus eventos", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_events:
                                Toast.makeText(MainActivity.this, "Novo evento", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_settings:
                                Toast.makeText(MainActivity.this, "Configuração", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_logout:
                                Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
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
    }

}
