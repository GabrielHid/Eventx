package br.edu.ifsp.arq.ads.dmos5.eventx.repository;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Invite;

public class InvitesRepository {

    private FirebaseFirestore firestore;

    public InvitesRepository(Application application) {
        firestore = FirebaseFirestore.getInstance();

    }

    public void insert(Invite invite){
        firestore.collection("invite").add(invite);
    }

    public void update(Invite invite){
        firestore.collection("invite").document(invite.getId())
                .set(invite);
    }

    public void delete(Invite invite){
        firestore.collection("invite").document(invite.getId())
                .delete();
    }

    public LiveData<List<Invite>> getByUser(String userId) {
        MutableLiveData<List<Invite>> liveData = new MutableLiveData<>();
        List<Invite> invites = new ArrayList<Invite>();

        Query query = firestore.collection("invite").whereEqualTo("user", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Invite invite = doc.toObject(Invite.class);
                        invite.setId(doc.getId());
                        Event eventt = getEvent(invite.getEventId());
                        invite.setInviteEvent(eventt);
                        invites.add(invite);
                    }
                }

                liveData.postValue(invites);
                liveData.setValue(invites);
                }
            });

        return liveData;
    }

    public Event getEvent(String id){
        Event test = new EventsRepository().getById(id);
        return test;
    }

    public Invite getById(String id){
        Invite invit = new Invite();
        DocumentReference userRef = firestore.collection("invite").document(id);
        userRef.get().addOnSuccessListener(snapshot -> {
            Invite invite2 = snapshot.toObject(Invite.class);
            invit.setId(invite2.getId());

        });
        return invit;
    }
}
