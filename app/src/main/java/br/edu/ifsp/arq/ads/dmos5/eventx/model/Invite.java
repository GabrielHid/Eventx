package br.edu.ifsp.arq.ads.dmos5.eventx.model;

import androidx.room.Ignore;

import java.io.Serializable;
import java.util.UUID;

public class Invite implements Serializable {
    private String id;
    private String user;
    private String eventId;
    private Event inviteEvent;
    private Boolean confirmation;

    public Invite(String user, String eventId, Boolean confirmation, Event inviteEvent) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.eventId = eventId;
        this.confirmation = confirmation;
        this.inviteEvent = inviteEvent;
    }

    @Ignore
    public Invite() {
        this("", "", false, new Event());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Event getInviteEvent() {
        return inviteEvent;
    }

    public void setInviteEvent(Event inviteEvent) {
        this.inviteEvent = inviteEvent;
    }
}
