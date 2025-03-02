package net.whgkswo.tesm.networking.payload.id;

public enum PayloadId {

    CONVERSATION_NBT_REQ("conversation_nbt_request"),
    CONVERSATION_NBT_RES("conversation_nbt_response"),
    RAYCASTING_NBT_REQ("raycasting_nbt_request"),
    RAYCASTING_NBT_RES("raycasting_nbt_response"),
    SET_NBT_REQ("set_nbt_request")
    ;

    private String id;

    PayloadId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
