package br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel;

import android.app.Application;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Optional;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Invite;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.InvitesRepository;

public class InviteViewModel extends AndroidViewModel {

    private InvitesRepository invitesRepository;

    public InviteViewModel(@NonNull Application application) {
        super(application);
        invitesRepository = new InvitesRepository(application);
    }

    public void createInvite(Invite invite){
        invitesRepository.insert(invite);
    }

    public void updateInvite(Invite invite){
        invitesRepository.update(invite);
    }

    public void deleteInvite(Invite invite){
        invitesRepository.delete(invite);
    }

    public LiveData<List<Invite>> getByUser(String userId){
        return invitesRepository.getByUser(userId);
    }


}
