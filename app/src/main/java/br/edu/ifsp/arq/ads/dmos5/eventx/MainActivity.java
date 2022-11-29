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

import com.google.android.material.navigation.NavigationView;

import com.google.android.material.navigation.NavigationView;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView txtTitle;
    private TextView txtLogin;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setToolBar();
        setDrawerLayout();
        setNavigationView();
        setTextLogin();
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
                    txtLogin.setText(user.getName()
                            + " " + user.getSurname());
                    String image = PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                            .getString(MediaStore.EXTRA_OUTPUT, null);
                }
            }
        });
    }
}
