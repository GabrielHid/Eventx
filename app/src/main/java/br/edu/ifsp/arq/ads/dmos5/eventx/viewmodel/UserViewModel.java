package br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel;

import android.app.Application;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.UsersRepository;

public class UserViewModel extends AndroidViewModel {

    public static final String USER_ID = "USER_ID";

    private UsersRepository usersRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.usersRepository = new UsersRepository(application);
    }

    public void createUser(User user){
        usersRepository.createUser(user);
    }

    public LiveData<User> login(String email, String password){
        return usersRepository.login(email, password);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<User> isLogged(){
        Optional<String> id = Optional.ofNullable(
                PreferenceManager.getDefaultSharedPreferences(getApplication())
                        .getString(USER_ID, null)
        );
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return usersRepository.loadUser(id.get());
    }

    public void resetPassword(String email) {
        usersRepository.resetPassword(email);
    }

}
