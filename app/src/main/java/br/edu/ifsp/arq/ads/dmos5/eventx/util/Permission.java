package br.edu.ifsp.arq.ads.dmos5.eventx.util;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.edu.ifsp.arq.ads.dmos5.eventx.MainActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;

public class Permission {

    User user = new User();

    public User verifyPermission(User userLogged, FirebaseFirestore db){

        db.collection("user")
                .whereEqualTo("id", userLogged.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user.setName(document.get("name").toString());
                                user.setRole(document.get("role").toString());
                                user.setEmail(document.get("email").toString());
                            }
                        }
                    }
                });

        return user;
    }

}
