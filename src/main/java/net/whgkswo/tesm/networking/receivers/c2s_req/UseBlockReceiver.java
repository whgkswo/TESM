package net.whgkswo.tesm.networking.receivers.c2s_req;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.libgui.ExampleGuiDesc;
import net.whgkswo.tesm.networking.payload.data.c2s_req.UseBlockReq;

public class UseBlockReceiver {
    public static void handle(UseBlockReq payload, ServerPlayNetworking.Context context){
        GlobalVariables.player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                ((syncId, playerInventory, player) -> new ExampleGuiDesc(
                        syncId, playerInventory, ScreenHandlerContext.EMPTY
                )),
                Text.literal("Hello, World!")

        ));

        /*BlockPos hitResultPos = payload.blockPos();

        // 목적지 좌표의 블럭이 높이가 낮으면 기준을 한 칸 내리기
        if(BlockPosUtil.isLowBlock(hitResultPos)){
            hitResultPos = hitResultPos.down();
        }
        if(!BlockPosUtil.isSteppable(hitResultPos)){
            player.sendMessage(Text.literal(String.format("(%s) 좌표는 엔티티가 딛고 설 수 없습니다.",
                    hitResultPos.toShortString())), false);
            return;
        }
        try{
            *//*new PathfinderV2("인두리온", hitResultPos);*//*
            new PathfinderV3("인두리온", hitResultPos);
        }catch (EntityNotFoundExeption e){
            // 아무것도 안함
        }*/
    }
}
