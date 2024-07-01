package net.whgkswo.tesm;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

public class TestClassClient {
    public static void testClassClient(){
        /*ClientPlayNetworking.registerGlobalReceiver(TestClass.DIRT_BROKEN, (client, handler, buf, responseSender) -> {
            int totalDirtBlocksBroken = buf.readInt();
            client.execute(() -> {
                client.player.sendMessage(Text.literal("Total dirt blocks broken: " + totalDirtBlocksBroken));
            });
        });*/
    }
}

