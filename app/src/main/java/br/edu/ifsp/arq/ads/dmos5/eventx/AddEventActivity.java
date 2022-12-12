package br.edu.ifsp.arq.ads.dmos5.eventx;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import br.edu.ifsp.arq.ads.dmos5.eventx.enums.SituationEnum;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.EventViewModel;
import br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel.UserViewModel;

public class AddEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    ProgressDialog pd;

    private TextInputEditText txtName;
    private TextInputEditText txtStart;
    private TextInputEditText txtEnd;
    private TextInputEditText txtDescription;
    private Spinner spnStatus;
    private Button btnCadastrar;
    private TextView txtTitle;

    private UserViewModel userViewModel;
    private EventViewModel eventViewModel;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        pd = new ProgressDialog(this);

        setToolBar();
        setComponents();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            setToolBar();
            setComponents();
            editScreen(bundle);
        }

        setBtnCadastrar(bundle);
    }

    private void editScreen(Bundle bundle) {
            btnCadastrar.setText("Editar");
            txtTitle.setText("Editar Evento");

            spnStatus = findViewById(R.id.sp_status);
            spnStatus.setVisibility(View.VISIBLE);

            SituationEnum[] situations = SituationEnum.values();
            for(int index = 0; index < situations.length; index++){
                if(situations[index].equals(SituationEnum.valueOf(bundle.getString("eSituation")))){
                    spnStatus.setSelection(index);
                }
            }

            txtName.setText(bundle.getString("eName"));
            txtStart.setText(bundle.getString("eStartDate"));
            txtEnd.setText(bundle.getString("eEndDate"));
            txtDescription.setText(bundle.getString("eDescription"));
    }

    private void setComponents() {
        txtName = findViewById(R.id.txt_edt_event_name);
        txtStart = findViewById(R.id.txt_edt_initial_date);
        txtEnd = findViewById(R.id.txt_edt_end_date);
        txtDescription = findViewById(R.id.txt_edt_event_descricao);
        btnCadastrar = findViewById(R.id.btn_add_event);
    }

    private void setBtnCadastrar(Bundle bundle) {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(bundle != null){
                        updateEvent(bundle);
                    } else{
                        addEvent();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateEvent(Bundle bundle) {
        if(isValid()){
            if(user.getId() != null){
                Event event = new Event(
                        bundle.getString("eId"),
                        txtName.getText().toString(),
                        txtDescription.getText().toString(),
                        txtStart.getText().toString(),
                        txtEnd.getText().toString(),
                        spnStatus.getSelectedItem().toString(),
                        user.getId()
                );

                eventViewModel.updateEvent(event, AddEventActivity.this);

                pd.dismiss();

                finish();
                startActivity(new Intent(AddEventActivity.this, MainActivity.class));
            }
        }
    }

    private void addEvent() throws ParseException {
        if(isValid()){

            String eventId = UUID.randomUUID().toString();

            if(user.getId() != null){
                Event event = new Event(
                        eventId,
                        txtName.getText().toString(),
                        txtDescription.getText().toString(),
                        txtStart.getText().toString(),
                        txtEnd.getText().toString(),
                        SituationEnum.Active.toString(),
                        user.getId()
                );

                eventViewModel.createEvent(event, AddEventActivity.this);

                pd.dismiss();

                finish();
                startActivity(new Intent(AddEventActivity.this, MainActivity.class));
            }
        }
    }

    private boolean isValid() {

        boolean valid = true;

        if(txtName.getText().toString().trim().isEmpty()){
            txtName.setError("Preencha o campo Nome");
            valid = false;
        } else{
            txtName.setError(null);
        }

        if(txtStart.getText().toString().trim().isEmpty()){
            txtStart.setError("Preencha o campo Início");
            valid = false;
        } else{
            txtStart.setError(null);
        }

        if(txtEnd.getText().toString().trim().isEmpty()){
            txtEnd.setError("Preencha o campo Fim");
            valid = false;
        } else{
            txtEnd.setError(null);
        }

        if(txtDescription.getText().toString().trim().isEmpty()){
            txtDescription.setError("Preencha o campo Descrição");
            valid = false;
        } else{
            txtDescription.setError(null);
        }

        return valid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadUserLogged() {
        userViewModel.isLogged().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null){
                    startActivity(new Intent(AddEventActivity.this,
                            UserLoginActivity.class));
                    finish();
                }else{
                    AddEventActivity.this.user = user;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        setToolBar();
        loadUserLogged();
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtTitle = findViewById(R.id.toolbar_title);
        txtTitle.setText(getString(R.string.txt_title_new_event));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}