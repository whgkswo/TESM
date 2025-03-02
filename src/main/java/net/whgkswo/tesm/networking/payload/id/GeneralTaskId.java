package net.whgkswo.tesm.networking.payload.id;

public enum GeneralTaskId {
    RESET_QUESTS("reset_quests")
    ;
    private String id;

    GeneralTaskId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
