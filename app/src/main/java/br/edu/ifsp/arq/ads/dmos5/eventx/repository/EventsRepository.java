package br.edu.ifsp.arq.ads.dmos5.eventx.repository;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifsp.arq.ads.dmos5.eventx.AddEventActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.User;

public class EventsRepository {

    FirebaseFirestore db;

    public void insert(Event event, AddEventActivity eventActivity){
        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("id", event.getId());
        data.put("name", event.getName());
        data.put("description", event.getDescription());
        data.put("startDate", event.getStartDate());
        data.put("endDate", event.getEndDate());
        data.put("situation", event.getSituation());
        data.put("ownerId", event.getOwnerId());

        db.collection("event").document(event.getId()).set(data);

        Toast.makeText(eventActivity.getBaseContext(), "Evento criado com sucesso", Toast.LENGTH_SHORT).show();
    }


    public void update(Event event, AddEventActivity eventActivity) {
        db = FirebaseFirestore.getInstance();

        db.collection("event").document(event.getId())
                .update("name", event.getName(),
                        "description", event.getDescription(),
                        "startDate", event.getStartDate(),
                        "endDate", event.getEndDate(),
                        "situation", event.getSituation())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(eventActivity.getBaseContext(), "Evento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventActivity.getBaseContext(), "Ocorreu um erro ao atualizar o evento: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void delete(Event event, AddEventActivity eventActivity) {
        db = FirebaseFirestore.getInstance();

        db.collection("event").document(event.getId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       Toast.makeText(eventActivity.getBaseContext(), "Evento deletado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(eventActivity.getBaseContext(), "Ocorreu um erro ao deletar o evento: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public Event getById(String id){
        Event event = new Event();
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("event").document(id);
        userRef.get().addOnSuccessListener(snapshot -> {
            Event event2 = snapshot.toObject(Event.class);
            event.setId(event2.getId());

        });
        return event;
    }
}
