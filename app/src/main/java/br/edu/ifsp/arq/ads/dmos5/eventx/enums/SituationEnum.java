package br.edu.ifsp.arq.ads.dmos5.eventx.enums;

public enum SituationEnum {
    Active (1, "Active"),
    InProgress (2, "InProgress"),
    Finished (3, "Finished"),
    Canceled (4, "Canceled");

    private int id;
    private String situation;

    SituationEnum(int id, String situation){
        this.id = id;
        this.situation = situation;
    }

    public int getId(){
        return id;
    }

    public String getSituation(){
        return  situation;
    }

}
