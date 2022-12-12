package br.edu.ifsp.arq.ads.dmos5.eventx.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.edu.ifsp.arq.ads.dmos5.eventx.AddEventActivity;
import br.edu.ifsp.arq.ads.dmos5.eventx.model.Event;
import br.edu.ifsp.arq.ads.dmos5.eventx.repository.EventsRepository;

public class EventViewModel extends AndroidViewModel {

    private EventsRepository eventsRepository;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventsRepository = new EventsRepository();
    }

    public void createEvent(Event event, AddEventActivity eventActivity){
        eventsRepository.insert(event, eventActivity);
    }

    public void updateEvent(Event event, AddEventActivity eventActivity){
        eventsRepository.update(event, eventActivity);
    }

    public void deleteEvent(Event event, AddEventActivity eventActivity){
        eventsRepository.delete(event, eventActivity);
    }

    public Event getById(String id){
        return eventsRepository.getById(id);
    }



}
