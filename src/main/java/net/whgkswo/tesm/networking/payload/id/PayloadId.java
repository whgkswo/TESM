package net.whgkswo.tesm.networking.payload.id;

public enum PayloadId {

    BLOCK_DATA_REQ,
    CONVERSATION_NBT_REQ,
    CONVERSATION_NBT_RES,
    DOOR_NAMING,
    NULL_RES,
    RAYCASTING_NBT_REQ,
    RAYCASTING_NBT_RES,
    SET_NBT_REQ,
    SIMPLE_REQ,
    USE_BLOCK_REQ
    ;

    public String getLowercaseName(){
        return this.name().toLowerCase();
    }
}
