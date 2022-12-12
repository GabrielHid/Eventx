package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class UserRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtTitle;
    private LinearLayout llAccount;
    private TextInputEditText txtName;
    private TextInputEditText txtSurname;
    private TextInputEditText txtBirthDate;
    private TextInputEditText txtState;
    private TextInputEditText txtCountry;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private Button btnRegister;
    private Boolean isRegister;
    private User user;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        isRegister = true;
        setToolBar();
        setBtnRegister();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onResume(){
        super.onResume();

        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User userData) {

                if(userData != null){
                    user = userData;
                    llAccount.setVisibility(View.GONE);
                    btnRegister.setText("Alterar");
                    isRegister = false;
                } else {
                    llAccount.setVisibility(View.VISIBLE);
                    btnRegister.setText("Cadastrar");
                    isRegister = true;
                }

                setInfoUser();
            }

        });
    }

    private void setBtnRegister() {
        txtName = findViewById(R.id.txt_edt_name);
        txtSurname = findViewById(R.id.txt_edt_surname);
        txtBirthDate = findViewById(R.id.txt_edt_birthdate);
        txtState = findViewById(R.id.txt_edt_state);
        txtCountry = findViewById(R.id.txt_edt_coutry);
        txtEmail = findViewById(R.id.txt_edt_email);
        txtPassword = findViewById(R.id.txt_edt_password);
        btnRegister = findViewById(R.id.btn_user_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {

                    if(isRegister){
                        user = new User(
                                txtName.getText().toString(),
                                txtSurname.getText().toString(),
                                txtEmail.getText().toString(),
                                txtPassword.getText().toString(),
                                "Usuário comum",
                                txtBirthDate.getText().toString(),
                                txtState.getText().toString(),
                                txtCountry.getText().toString()
                        );

                        if (user.getPassword().length() >= 6){
                            Toast.makeText(UserRegisterActivity.this,
                                    R.string.msg_erro_password, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        userViewModel.createUser(user);
                        finish();
                    } else{
                        user.setName(txtName.getText().toString());
                        user.setSurname(txtSurname.getText().toString());
                        user.setBirthDate(txtBirthDate.getText().toString());
                        user.setState(txtState.getText().toString());
                        user.setCountry(txtCountry.getText().toString());


                        userViewModel.updateUser(user);
                        finish();
                    }

                }
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if(txtName.getText().toString().trim().isEmpty()){
            txtName.setError("Preencha o campo do nome");
            isValid = false;
        }else{
            txtName.setError(null);
        }
        if(isRegister && txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Preencha o campo do e-mail");
            isValid = false;
        }else{
            txtEmail.setError(null);
        }
        if(isRegister && txtPassword.getText().toString().trim().isEmpty()){
            txtPassword.setError("Preencha o campo da senha");
            isValid = false;
        }else{
            txtPassword.setError(null);
        }
        return isValid;
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = findViewById(R.id.toolbar_title);
        llAccount = findViewById(R.id.linearl_account);

        if(isRegister){
            txtTitle.setText(getString(R.string.txt_title_new_user));
        } else {
            txtTitle.setText("Alterar Usuário");
        }
    }

    private void setInfoUser(){
        if(isRegister){
            txtName.setText("");
            txtSurname.setText("");
            txtBirthDate.setText("");
            txtState.setText("");
            txtCountry.setText("");
        } else {
            txtName.setText(user.getName());
            txtSurname.setText(user.getSurname());
            txtBirthDate.setText(user.getBirthDate());
            txtState.setText(user.getState());
            txtCountry.setText(user.getCountry());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}