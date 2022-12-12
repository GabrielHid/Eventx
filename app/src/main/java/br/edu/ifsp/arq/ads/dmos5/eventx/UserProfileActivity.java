package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenuItemView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitle;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtBirthDate;
    private TextView txtLocalidade;
    private Button btnEdtProfile;
    private Button btnLogout;
    private ImageView imageProfile;
    private Uri photoURI;
    private final int REQUEST_TAKE_PHOTO = 1;
    User user;


    private UserViewModel userViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        imageProfile = findViewById(R.id.header_profile_image);

        setComponents();
        loadUserLogged();
        setToolBar();
        setBtnUpdateUser();
        setBtnLogout();
        setImageProfile();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume(){
        super.onResume();

    }

    private void setComponents() {
        txtName = findViewById(R.id.txt_user_name);
        txtEmail = findViewById(R.id.txt_user_email);
        txtBirthDate = findViewById(R.id.txt_user_birthdate);
        txtLocalidade = findViewById(R.id.txt_user_local);
        btnEdtProfile = findViewById(R.id.btn_edt_profile);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = findViewById(R.id.toolbar_title);
        txtTitle.setText("Perfil");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBtnUpdateUser() {
        btnEdtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this,
                        UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setBtnLogout(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserLoginActivity.class);
                startActivity(intent);
                userViewModel.logout();
            }
        });
    }

    private void setImageProfile() {
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //14min
                imageProfile.setImageDrawable(new BitmapDrawable(bitmap));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadUserLogged() {
        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    startActivity(new Intent(UserProfileActivity.this,
                            UserLoginActivity.class));
                    finish();
                }else{
                    UserProfileActivity.this.user = user;

                    txtName.setText(user.getName());
                    txtEmail.setText((user.getEmail()));
                    txtBirthDate.setText(user.getBirthDate());
                    txtLocalidade.setText(user.getState() + " - " + user.getCountry().toUpperCase());

                }
            }
        });
    }
}
