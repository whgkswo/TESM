package net.whgkswo.tesm.gui.screen.templete;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.whgkswo.tesm.networking.ModMessages;

public class CustomScreen extends Screen {
    public CustomScreen(){
        super(Text.literal("GUI 템플릿 (Freeze)"));
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public void init(){
        // 틱 프리즈 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(true); // freezeOn = true
        ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
    }
    @Override
    public void close(){
        // 틱 언프리즈 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(false); // freezeOn = false
        ClientPlayNetworking.send(ModMessages.TICK_FREEZE_TOGGLE_ID, buf);
        super.close();
    }
}
