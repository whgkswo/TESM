package net.whgkswo.tesm.networking.response_handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.GlobalVariablesClient;

public class GetNbtByConversationResponseHandler {
    public static void handle(PacketByteBuf response){
        int id = response.readInt();
        Entity partner = GlobalVariables.world.getEntityById(id);
        GlobalVariablesClient.convPartnerTempName = response.readString();
        GlobalVariablesClient.convPartnerName = response.readString();
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(()->{
            client.setScreen(new ConversationScreen(partner));
        });
    }
}
