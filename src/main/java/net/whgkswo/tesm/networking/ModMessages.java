package net.whgkswo.tesm.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.networking.packet.*;

public class ModMessages{
    public static final Identifier EXAMPLE_ID = new Identifier(TESMMod.MODID, "example");
    public static final Identifier FREEZE_ENTITY_ID = new Identifier(TESMMod.MODID, "freeze_entity");
    public static final Identifier UNFREEZE_ENTITY_ID = new Identifier(TESMMod.MODID,"unfreeze_entity");
    public static final Identifier GETNBT_ID = new Identifier(TESMMod.MODID,"getnbt");
    public static final Identifier GETNBT_RESPONSE_ID = new Identifier(TESMMod.MODID,"getnbt_response");
    public static final Identifier TICK_FREEZE_TOGGLE_ID = new Identifier(TESMMod.MODID, "tick_freeze_toggle");
    public static final Identifier USE_BLOCK_ID = new Identifier(TESMMod.MODID, "use_block");
    public static final Identifier CHUNK_UPDATE_ID = new Identifier(TESMMod.MODID, "chunk_update");

    // ID와 패킷 쌍 등록
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(FREEZE_ENTITY_ID, FreezeEntityC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UNFREEZE_ENTITY_ID, UnfreezeEntityC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GETNBT_ID, GetNBTC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TICK_FREEZE_TOGGLE_ID, TickFreezeToggleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(USE_BLOCK_ID, UseBlockC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CHUNK_UPDATE_ID, ChunkUpdateC2SPacket::receive);
    }

    public static void registerS2CPackets(){
        ServerPlayNetworking.registerGlobalReceiver(GETNBT_RESPONSE_ID, GetNBTC2SPacket::receive);
    }
}