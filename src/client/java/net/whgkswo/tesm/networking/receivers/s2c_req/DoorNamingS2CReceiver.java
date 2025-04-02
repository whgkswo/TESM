package net.whgkswo.tesm.networking.receivers.s2c_req;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.whgkswo.tesm.blocks.blockentity.DoorBlockEntity;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.gui.libgui.client_side.description.DoorNamingDesc;
import net.whgkswo.tesm.gui.libgui.client_side.screen.DoorNamingScreen;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;

public class DoorNamingS2CReceiver {
    public static void handle(DoorNaming payload, ClientPlayNetworking.Context context){
        String outsideName = payload.outsideName();
        String insideName = payload.insideName();
        boolean pushToOutside = payload.pushToOutside();

        MinecraftClient.getInstance().setScreen(new DoorNamingScreen(new DoorNamingDesc(payload.blockPos(), insideName, outsideName, pushToOutside)));
    }
}
