package net.whgkswo.tesm.networking.payload.id;

public enum PayloadId {

    GENERAL_REQ("general_request"),
    CONVERSATION_NBT_REQ("conversation_nbt_request"),
    CONVERSATION_NBT_RES("conversation_nbt_response"),
    DOOR_NAMING("door_naming"),
    RAYCASTING_NBT_REQ("raycasting_nbt_request"),
    RAYCASTING_NBT_RES("raycasting_nbt_response"),
    SET_NBT_REQ("set_nbt_request"),
    USE_BLOCK_REQ("use_block_request")
    ;

    private String id;

    PayloadId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
